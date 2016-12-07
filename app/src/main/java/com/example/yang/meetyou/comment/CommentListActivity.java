package com.example.yang.meetyou.comment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.example.yang.meetyou.homePage.ActivityContentActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.model.Comment;
import com.example.yang.meetyou.model.CommentJson;
import com.example.yang.meetyou.utils.DownloadImageTask;
import com.example.yang.meetyou.utils.PreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private static final int SET_VISIBILITY = 12;
    private static final int REFRESH_COMMENT = 13;
    private static final String TAG = "CommentListActivity";


    private RecyclerView mRecyclerView;
    private List<Comment> mCommentList = new ArrayList<>();

    private TextView comment;
    private Bitmap headImage;
    private TextView tip;

    public static String activity_id = "";
    final OkHttpClient mClient = new OkHttpClient();
    private String msg ="";

    private Bitmap[] bitmaps;
    private Gson mGson = new Gson();
    private CommentListAdapter mCommentListAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(CommentListActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_VISIBILITY:
                    if (tip.getVisibility() == View.GONE) {
                        tip.setVisibility(View.VISIBLE);
                    }else {
                        tip.setVisibility(View.INVISIBLE);
                    }
                    break;
                case REFRESH_COMMENT:
                    mRecyclerView.setAdapter(new CommentListAdapter());
                    break;
                default:
                    break;
            }
        }
    };
//    protected void onStart() {
//        super.onStart();
//        getComment();
//
//    }

    public  void getComment() {
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
                            String response2 = response.body().string();
                            Log.i(TAG, response2);
                            CommentJson commentJson  = mGson.fromJson(response2, CommentJson.class);
                            if (commentJson.getMsgCode() == 501) {
                                mCommentList = commentJson.getData();
                                handler.obtainMessage(REFRESH_COMMENT,commentJson.getMsg()).sendToTarget();
                            }else if(commentJson.getMsgCode() == 502){
                                handler.obtainMessage(SHOW_TOAST,commentJson.getMsg()).sendToTarget();
                            }
                        if (commentJson.getMsgCode() == 507) {
                            handler.obtainMessage(SET_VISIBILITY,commentJson.getMsg()).sendToTarget();
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
        tip = (TextView) findViewById(R.id.tip_tv);
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
        getComment();

        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CommentListActivity.this));
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
            final Comment comment = mCommentList.get(position);

            holder.bindComment(comment.getUserHeads(), comment.getNickname(), comment.getContent(), comment.getCommentTime(),
                    comment.getStorey());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String account = PreferenceUtil.getString(CommentListActivity.this, PreferenceUtil.ACCOUNT);
                    if (account.equals(comment.getSenderAccount())) {
                       showDeleteCommentDialog(comment);
                    }else{
                        AnswerDialogFragment.newInstance(CommentListActivity.this,comment.getCommentId()).show(getSupportFragmentManager()," AnswerDialogFragment");
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mCommentList.size();
        }

        public void showDeleteCommentDialog(final Comment comment1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommentListActivity.this)
                    .setTitle("确定删除此条评论？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    String requestURL = "http://139.199.180.51/meetyou/public/deleteComment?comment_id=" + comment1.getCommentId();
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
                                                if (status == 505) {
                                                    handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("activityId", CommentListActivity.activity_id);
                                                    Intent i = new Intent(CommentListActivity.this, ActivityContentActivity.class);
                                                    i.putExtras(bundle);
                                                    startActivity(i);
                                                }else if(status == 506){
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
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }
        class MyViewHolder extends RecyclerView.ViewHolder
        {
            ImageView user_head;
            TextView user_nickname;
            TextView user_comment;
            TextView comment_time;
            TextView comment_storey;
            public MyViewHolder(View view)
            {
                super(view);
                user_head = (ImageView) view.findViewById(R.id.comment_user_head);
                user_nickname = (TextView) view.findViewById(R.id.comment_user_nickname);
                user_comment = (TextView) view.findViewById(R.id.comment_user_comment);
                comment_time = (TextView) view.findViewById(R.id.comment_time_tv);
                comment_storey = (TextView) view.findViewById(R.id.comment_storey);
            }

            public void bindComment(String user_image, String nickname, String comment,String user_comment_time,
                                    String commentStorey) {
                new DownloadImageTask(user_head).execute(user_image);
                user_nickname.setText(nickname);
                user_comment.setText(comment);
                comment_time.setText(user_comment_time);
                comment_storey.setText(commentStorey);
            }
        }
    }

}
