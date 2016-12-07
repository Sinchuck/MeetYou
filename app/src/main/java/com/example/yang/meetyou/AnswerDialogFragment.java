package com.example.yang.meetyou;

import android.content.Context;
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
public class AnswerDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int SHOW_TOAST = 11;
    private static final String TAG = "AnswerDialogFragment";


    private TextView title;
    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText comment_edit;
    private static Context mContext;

    private String comment;
    final OkHttpClient mClient = new OkHttpClient();
    private String msg;

    public static String commentId = "";

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

     public static AnswerDialogFragment newInstance(Context context,String id) {

        Bundle args = new Bundle();
        AnswerDialogFragment fragment = new AnswerDialogFragment();
        fragment.setArguments(args);
         mContext = context;
         commentId = id;

        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_nickname_dialog, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_save_nickname_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_nickname_bt);
        comment_edit = (EditText) view.findViewById(R.id.edit_user_nickname);
        title = (TextView) view.findViewById(R.id.title_nick_name_dialog);
        title.setText("回复用户评论");
        comment_edit.setHint("请输入你的回复");
        sure_tv.setText("回复");

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);


        return view;
    }

    private void saveComment() {
        comment = comment_edit.getText().toString();
        if (comment.equals("")) {
            handler.obtainMessage(SHOW_TOAST, "回复不可为空").sendToTarget();
            return;
        }

        setComment();


    }

    private void setComment() {
        final String account = PreferenceUtil.getString(getActivity(), PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String requestURL = "http://139.199.180.51/meetyou/public/comment?activity_id="+CommentListActivity.activity_id
                        +"&user_account="+account+"&comment_id="+commentId+"&content="+comment;
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
//                Intent i = new Intent(getActivity(), ActivityContentActivity.class);
//                startActivity(i);
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
