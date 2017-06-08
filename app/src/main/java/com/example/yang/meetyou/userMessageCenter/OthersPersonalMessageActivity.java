package com.example.yang.meetyou.userMessageCenter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.DownloadImageTask;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/26.
 */
public class OthersPersonalMessageActivity extends AppCompatActivity {

    private static final String TAG = "OthersMsgActivity";

    private static final int SHOW_TOAST = 11;
    private static final int SET_ACCOUNT =12;
    private static final int SET_NICKNAME = 13;
    private static final int SET_HEADS = 14;
    private static final int SET_SEX =15;
    private static final int SET_CONTACT = 16;
    private static final int SET_SIGNATURE = 17;
    private static final int SET_IS_FOLLOWED = 18;
    private static final int SET_IS_PRIVACY = 19;

    ImageView mOthersImage;

    TextView otherPublishedActivityTextView;
    TextView mOthersNickname;
    TextView mOthersAccount;
    TextView mOthersGender;
    TextView mOthersContactInfo;
    TextView mOthersSignature;
    TextView mWatchOthers;

    OkHttpClient mClient = new OkHttpClient();

    String othersAccount;
    String msg;
    String requestURL;
    String method;
    String userAccount;
    String[] requestString = new String[3];

    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_personal_message);
        otherPublishedActivityTextView = (TextView) findViewById(R.id.tv_activity_published);
        otherPublishedActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userAccount", othersAccount);
                Intent a = new Intent(OthersPersonalMessageActivity.this,
                        ActivityHasPublishedByUserActivity.class);
                a.putExtras(bundle);
                startActivity(a);
            }
        });

        mOthersImage = (ImageView) findViewById(R.id.iv_others_image);
        mOthersNickname = (TextView) findViewById(R.id.others_nickname);
        mOthersAccount = (TextView) findViewById(R.id.others_account);
        mOthersGender = (TextView) findViewById(R.id.others_gender);
        mOthersContactInfo = (TextView) findViewById(R.id.tv_others_contact);
        mOthersSignature = (TextView) findViewById(R.id.others_signature);
        mWatchOthers = (TextView) findViewById(R.id.watch_others);
        mWatchOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWatchOthers.getText().toString().equals("关注")) {
                    method = "followUser";
                    requestString[0] = method;
                    userAccount = PreferenceUtil.getString(OthersPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);
                    requestString[1] = userAccount;
                    requestString[2] = othersAccount;
                    new GetConcernStatus().execute(requestString);
                } else if (mWatchOthers.getText().toString().equals("取消关注")) {
                    method = "unfollowUser";
                    requestString[0] = method;
                    userAccount = PreferenceUtil.getString(OthersPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);
                    requestString[1] = userAccount;
                    requestString[2] = othersAccount;
                    new GetConcernStatus().execute(requestString);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        othersAccount = bundle.getString("userAccount");

        getOthersMessage();

    }

    private class GetConcernStatus extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            String requestURL = " http://118.89.37.26/meetyou/public/" + params[0] + "?user_account="
                    + params[1] + "&follow_user_account=" + params[2];

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
                        status = jsonObject.getInt("msgCode");
                        String msg = jsonObject.getString("msg");

                        if (status == 309) {
                            Log.i(TAG, "关注成功");
                        } else if(status == 310) {
                            Log.i(TAG, "关注失败");
                        } else if (status == 311) {
                            Log.i(TAG, "取消关注成功");
                        } else if (status == 312) {
                            Log.i(TAG, "取消关注失败");
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
            return status;
        }

        @Override
        protected void onPostExecute(Integer sta) {
            if (status == 309) {
                mWatchOthers.setText("取消关注");
            } else if (status == 310) {
                Toast.makeText(OthersPersonalMessageActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
            } else if (status == 311) {
                mWatchOthers.setText("关注");
            } else if (status == 312) {
                Toast.makeText(OthersPersonalMessageActivity.this, "取消关注失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void getOthersMessage() {

        final String usingAccount = PreferenceUtil.getString(OthersPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestURL = "http://118.89.37.26/meetyou/public/otherUserInfo?user_account=" + othersAccount
                    + "&using_account=" + usingAccount;

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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            if (status == 301) {
                                handler.obtainMessage(SET_HEADS, jsonObject1.getString("user_image")).sendToTarget();
                                handler.obtainMessage(SET_NICKNAME, jsonObject1.getString("user_nickName")).sendToTarget();
                                handler.obtainMessage(SET_ACCOUNT, jsonObject1.getString("user_account")).sendToTarget();
                                handler.obtainMessage(SET_SEX, jsonObject1.getString("user_sex")).sendToTarget();
                                handler.obtainMessage(SET_CONTACT,jsonObject1.getString("user_contacts")).sendToTarget();
                                handler.obtainMessage(SET_SIGNATURE, jsonObject1.getString("user_description")).sendToTarget();
                                handler.obtainMessage(SET_IS_FOLLOWED, jsonObject1.getString("isFollowed")).sendToTarget();
                                handler.obtainMessage(SET_IS_PRIVACY, jsonObject1.getString("user_privacy")).sendToTarget();
                                Log.i(TAG, jsonObject1.getString("isFollowed"));
                            }else if(status == 302){
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                            }

                            Log.i("123", msg);
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

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(OthersPersonalMessageActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_NICKNAME:
                    mOthersNickname.setText(msg.obj.toString());
                    break;
                case SET_ACCOUNT:
                    mOthersAccount.setText(msg.obj.toString());
                    break;
                case SET_SEX:
                    mOthersGender.setText(msg.obj.toString());
                    break;
                case SET_CONTACT:
                    mOthersContactInfo.setText(msg.obj.toString());
                    break;
                case SET_SIGNATURE:
                    mOthersSignature.setText(msg.obj.toString());
                    break;

                case SET_HEADS:
                    new DownloadImageTask(mOthersImage).execute(msg.obj.toString());
                    break;

                case SET_IS_FOLLOWED:
                    Log.i("123", msg.obj.toString());
                    if (msg.obj.toString().equals("no")) {
                        mWatchOthers.setText("关注");
                    } else {
                        mWatchOthers.setText("取消关注");
                    }
                    break;

                case SET_IS_PRIVACY:
                    if (msg.obj.toString().equals("0")) {

                    } else {
                        otherPublishedActivityTextView.setText("用户已隐藏个人信息");
                        otherPublishedActivityTextView.setClickable(false);
                    }

                default:
                    break;
            }
        }
    };
}
