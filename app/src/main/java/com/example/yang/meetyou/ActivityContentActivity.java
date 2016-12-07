package com.example.yang.meetyou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.model.HuodongDetailsJson;
import com.example.yang.meetyou.userMessageCenter.OthersPersonalMessageActivity;
import com.example.yang.meetyou.utils.DownloadImageTask;
import com.example.yang.meetyou.utils.PreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/26.
 */
public class ActivityContentActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "ActivityContentActivity";
    private static  final  int SHOW_TOAST = 11;
    private static final int SET_VISIBLITY = 12;
    public static final String ACTIVITY_ID = "ACTIVITY_ID";


    HuodongDetailsJson huodongDetailsJson;

    String activityId;
    String user_account;
    String huodongDetails;
    String othersUserAccount;

    LinearLayout mHeadsLinear;
    TextView mActivityNameTextView;

    OkHttpClient mClient = new OkHttpClient();
    Gson mGson = new Gson();

    TextView mUserNickname;
    TextView mUserAccount;
    TextView mActivityTag;
    TextView mActivityTheme;
    TextView mActivityTime;
    TextView mMaxNumber;
    TextView mActivityDetails;
    ImageView mHeads;
    TextView concern;
    TextView comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("活动详情");
        mHeadsLinear = (LinearLayout)findViewById(R.id.linear_other_heads);
        mHeadsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userAccount", othersUserAccount);
                Intent a = new Intent(ActivityContentActivity.this, OthersPersonalMessageActivity.class);
                a.putExtras(bundle);
                startActivity(a);
            }
        });

        mUserNickname = (TextView) findViewById(R.id.content_user_nickname);
        mUserAccount = (TextView) findViewById(R.id.content_user_account);
        mActivityTag = (TextView) findViewById(R.id.content_activity_tag);
        mActivityTheme = (TextView) findViewById(R.id.content_activity_theme);
        mActivityTime = (TextView) findViewById(R.id.content_activity_time);
        mMaxNumber = (TextView) findViewById(R.id.content_max_number);
        mActivityDetails = (TextView) findViewById(R.id.content_activity_details);
        mHeads = (ImageView) findViewById(R.id.content_releaser_head);
        concern = (TextView) findViewById(R.id.content_concern_tv);
        comment = (TextView) findViewById(R.id.content_comment_tv);

        concern.setOnClickListener(this);
        comment.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        activityId = bundle.getString("activityId");
        user_account = PreferenceUtil.getString(ActivityContentActivity.this, PreferenceUtil.ACCOUNT);
        huodongDetails = "http://139.199.180.51/meetyou/public/activityInfo?activity_id="
                + activityId + "&user_account=" + user_account;

        new GetActivityData().execute();
    }

    private class GetActivityData extends AsyncTask<Void, Void, HuodongDetailsJson> {

        @Override
        protected HuodongDetailsJson doInBackground(Void... params) {
            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url(huodongDetails)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        huodongDetailsJson = mGson.fromJson(response.body().string(),
                                HuodongDetailsJson.class);
                        Log.i(TAG, huodongDetailsJson.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return huodongDetailsJson;
        }

        @Override
        protected void onPostExecute(HuodongDetailsJson detailsJson) {
            super.onPostExecute(detailsJson);
            mUserAccount.setText(detailsJson.getActivityInfo().activity_releaser_account);
            mUserNickname.setText(detailsJson.getActivityInfo().activity_releaser_nickName);
            mActivityDetails.setText(detailsJson.getActivityInfo().activity_details);
            mMaxNumber.setText(detailsJson.getActivityInfo().activity_participants_max_count);
            mActivityTag.setText(detailsJson.getActivityInfo().activity_tag);
            mActivityTheme.setText(detailsJson.getActivityInfo().activity_theme);
            mActivityTime.setText(detailsJson.getActivityInfo().activity_time);
            new DownloadImageTask(mHeads).execute(detailsJson.getActivityInfo().activity_releaser_headPic);
            if (detailsJson.getActivityInfo().isParticipated.equals("no")) {
                concern.setText("参加");
            }else{
                concern.setText("取消参加");
            }

            othersUserAccount = detailsJson.getActivityInfo().activity_releaser_account;
        }
    }


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(ActivityContentActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_VISIBLITY:
                    if (concern.getText().toString().equals("参加")) {
                        concern.setText("取消参加");
                    }else{
                        concern.setText("参加");
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_concern_tv:
                if (concern.getText().toString().equals("参加")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String account = PreferenceUtil.getString(ActivityContentActivity.this, PreferenceUtil.ACCOUNT);
                            String requestURL = " http://139.199.180.51/meetyou/public/participate?activity_id="+activityId+"&user_account=" + account;

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
                                    int status = jsonObject.getInt("msgCode");
                                    Log.i("123", status + "");
                                    String msg = jsonObject.getString("msg");

                                        if (status == 401) {
                                            handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                                            handler.obtainMessage(SET_VISIBLITY,msg).sendToTarget();
                                        }else if(status == 402){
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
                }else if (concern.getText().toString().equals("取消参加")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String account = PreferenceUtil.getString(ActivityContentActivity.this, PreferenceUtil.ACCOUNT);
                            String requestURL = " http://139.199.180.51/meetyou/public/participateCancel?activity_id="+activityId+"&user_account=" + account;

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
                                        String msg = jsonObject.getString("msg");

                                        if (status == 407) {
                                            handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                                            handler.obtainMessage(SET_VISIBLITY,msg).sendToTarget();
                                        }else if(status == 408){
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
                break;
            case R.id.content_comment_tv:
                Intent i = new Intent(ActivityContentActivity.this, CommentListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ACTIVITY_ID, activityId);
                i.putExtras(bundle);
                startActivity(i);
                break;
        }
    }
}
