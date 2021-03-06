package com.example.yang.meetyou.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.homePage.ActivityContentActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/11/19.
 */
public class CommentDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int SHOW_TOAST = 11;
    private static final String TAG = "CommentDialogFragment";


    private TextView title;
    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText comment_edit;
    private static Context mContext;

    private String comment;
    final OkHttpClient mClient = new OkHttpClient();
    private String msg;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(mContext,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

     public static CommentDialogFragment newInstance(Context context) {

        Bundle args = new Bundle();
        CommentDialogFragment fragment = new CommentDialogFragment();
        fragment.setArguments(args);
         mContext = context;

        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_comment_dialog, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_save_nickname_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_nickname_bt);
        comment_edit = (EditText) view.findViewById(R.id.edit_user_nickname);
        title = (TextView) view.findViewById(R.id.title_nick_name_dialog);
        title.setText("发表用户评论");
        comment_edit.setHint("请输入你的评论");
        sure_tv.setText("发表");

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);


        return view;
    }

    private void saveComment() {
        comment = comment_edit.getText().toString();
        if (comment.equals("")) {
            handler.obtainMessage(SHOW_TOAST, "评论不可为空").sendToTarget();
            return;
        }

        setComment();


    }

    private void setComment() {
        final String account = PreferenceUtil.getString(getActivity(), PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String requestURL = "http://118.89.37.26/meetyou/public/comment?activity_id="+CommentListActivity.activity_id
                        +"&user_account="+account+"&comment_id=-100&content="+comment;
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
                            if (status == 503) {
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                            }else if(status == 504){
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_save_nickname_bt:
                this.dismiss();
                break;
            case R.id.save_nickname_bt:
                saveComment();
                this.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("activityId", CommentListActivity.activity_id);
                Intent i = new Intent(getActivity(), ActivityContentActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
