package com.example.yang.meetyou.userMessageCenter;

import android.content.Intent;
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

import com.example.yang.meetyou.ActivityHasPublishedByUserActivity;
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

    ImageView mOthersImage;

    TextView otherPublishedActivityTextView;
    TextView mActivityNameTextView;
    TextView mOthersNickname;
    TextView mOthersAccount;
    TextView mOthersGender;
    TextView mOthersContactInfo;
    TextView mOthersSignature;
    TextView mWatchOthers;

    OkHttpClient mClient = new OkHttpClient();

    String userAccount;
    String msg;
    String requestURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_personal_message);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("个人详细信息");
        otherPublishedActivityTextView = (TextView) findViewById(R.id.tv_activity_published);
        otherPublishedActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(OthersPersonalMessageActivity.this, ActivityHasPublishedByUserActivity.class);
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

        Bundle bundle = getIntent().getExtras();
        userAccount = bundle.getString("userAccount");

        getOthersMessage();

    }

    protected void getOthersMessage() {

        final String usingAccount = PreferenceUtil.getString(OthersPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestURL = " http://139.199.180.51/meetyou/public/userInfo?user_account=" + userAccount
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

                default:
                    break;
            }
        }
    };
}
