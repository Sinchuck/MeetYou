package com.example.yang.meetyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/12/5.
 */
public class CommentListActivity extends AppCompatActivity {
    private static final int SHOW_TOAST = 11;
    private static final String TAG = "CommentListActivity";


    private RecyclerView mRecyclerView;
    private CommentListAdapter mCommentListAdapter;
    private List<Comment> mCommentList = new ArrayList<>();

    private TextView comment;
    private Bitmap headImage;

    public static String activity_id = "";
    final OkHttpClient mClient = new OkHttpClient();
    private String msg;

    private Bitmap[] bitmaps;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(CommentListActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String requestURL = "http://139.199.180.51/meetyou/public/getComment?activity_id="+activity_id;
                final Request request = new Request.Builder()
                        .get()
                        .tag(this)
                        .url(requestURL)
                        .build();

                Response response;
                try {
                    response = mClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        try {
                            String response2 = response.body().string();
                            JSONObject jsonObject = new JSONObject(response2);
                            Log.i(TAG, response2);
                            int  status = jsonObject.getInt("msgCode");
                            Log.i("123", status + "");
                            msg = jsonObject.getString("msg");

                            if (status == 501) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray.length() == 0) {

                                }else{
                                    bitmaps = new Bitmap[jsonArray.length()];
                                    for(int i = 0;i<jsonArray.length();i++){
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        Comment comment = new Comment();
                                        String id = data.getString("id");
                                        String storey = data.getString("storey");
                                        String content = data.getString("content");
                                        String comment_time = data.getString("commentTime");
                                        String senderImage = data.getString("senderImage");
                                        String nickname = "";
                                        if (data.getString("commentType").equals("0")) {
                                             nickname = data.getString("senderNickName") + ":";
                                        }else{
                                            nickname = data.getString("senderNickName") + " 回复 " + data.getString("receiverNickName") + ":";
                                        }
                                        comment.setCommentId(id);
                                        comment.setStorey(storey);
                                        comment.setContent(content);
                                        comment.setCommentTime(comment_time);
                                        comment.setNickname(nickname);
                                        comment.setUserHeads(senderImage);
                                        mCommentList.add(comment);

                                    }
                                }
                            }else if(status == 502){
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                            }

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comment = (TextView) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance(CommentListActivity.this);
                commentDialogFragment.show(getSupportFragmentManager(), "CommentDialogFragment");
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        activity_id = bundle.getString(ActivityContentActivity.ACTIVITY_ID);
        Log.i(TAG, activity_id);

        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CommentListAdapter());

    }

    class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                   CommentListActivity.this).inflate(R.layout.comment, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            Comment comment = mCommentList.get(position);

            holder.bindComment(comment.getUserHeads(), comment.getNickname(), comment.getContent(), comment.getCommentTime());
        }

        @Override
        public int getItemCount()
        {
            return mCommentList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            ImageView user_head;
            TextView user_nickname;
            TextView user_comment;
            TextView comment_time;
            public MyViewHolder(View view)
            {
                super(view);
                user_head = (ImageView) view.findViewById(R.id.comment_user_head);
                user_nickname = (TextView) view.findViewById(R.id.comment_user_nickname);
                user_comment = (TextView) view.findViewById(R.id.comment_user_comment);
                comment_time = (TextView) view.findViewById(R.id.comment_time_tv);
            }

            public void bindComment(String user_image, String nickname, String comment,String user_comment_time) {
                new DownloadImageTask().execute(user_image);
                user_nickname.setText(nickname);
                user_comment.setText(comment);
                comment_time.setText(user_comment_time);
            }

            private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

                public DownloadImageTask() {

                }

                protected Bitmap doInBackground(String... urls) {
                    String urldisplay = urls[0];
                    Bitmap mIcon11 = null;
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                    return mIcon11;
                }

                protected void onPostExecute(Bitmap result) {
                    if(result != null){
                        user_head.setImageBitmap(result);
                    }
                }
            }
        }
    }

}
