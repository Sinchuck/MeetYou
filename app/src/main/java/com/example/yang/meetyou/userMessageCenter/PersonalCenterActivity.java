package com.example.yang.meetyou.userMessageCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.ActivityHasPublishedByUserActivity;
import com.example.yang.meetyou.ConcernActivity;
import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.accounts.LoginActivity;
import com.example.yang.meetyou.publish.PublishActivity;
import com.example.yang.meetyou.utils.DownloadImageTask;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/25.
 */
public class PersonalCenterActivity extends AppCompatActivity implements View.OnClickListener{
    private static  final  int SHOW_TOAST = 11;
    private static final int SET_ACCOUNT =12;
    private static final int SET_NICKNAME = 13;
    private static final int SET_HEADS = 14;
    private static final String TAG = "PersonalCenterActivity";


    private TextView mHomePage;
    private TextView mConcern;
    private TextView mPublish;
    private TextView mPersonalCenter;
    private LinearLayout mHeadsLinear;
    private TextView mCancelText;
    private TextView mActivityHasPublishedByUserText;

    private TextView account_tv;
    private TextView nickname_tv;
    private ImageView head_iv;

    final OkHttpClient mClient = new OkHttpClient();

    private String userAccount;
    private String msg;
    protected void onStart () {
        super.onStart();
        final String account = PreferenceUtil.getString(PersonalCenterActivity.this, PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String requestURL = " http://139.199.180.51/meetyou/public/userInfo?user_account=" + account;

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
                            msg = jsonObject.getString("msg");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            if (status == 301) {
                                userAccount = jsonObject1.getString("user_account");
                                handler.obtainMessage(SET_NICKNAME, jsonObject1.getString("user_nickName")).sendToTarget();
                                handler.obtainMessage(SET_ACCOUNT, jsonObject1.getString("user_account")).sendToTarget();
                                handler.obtainMessage(SET_HEADS, jsonObject1.getString("user_image")).sendToTarget();
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
                    Toast.makeText(PersonalCenterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_NICKNAME:
                    nickname_tv.setText(msg.obj.toString());
                    break;
                case SET_ACCOUNT:
                    account_tv.setText(msg.obj.toString());
                    break;
                case SET_HEADS:
                    new DownloadImageTask(head_iv).execute(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);
        mHeadsLinear = (LinearLayout)findViewById(R.id.linear_personal);
        mCancelText = (TextView) findViewById(R.id.tv_cancel);
        mActivityHasPublishedByUserText = (TextView) findViewById(R.id.tv_activity_has_published_by_user);
        nickname_tv = (TextView) findViewById(R.id.tv_nickname);
        account_tv = (TextView) findViewById(R.id.tv_account);
        head_iv = (ImageView) findViewById(R.id.iv_head);


        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
        mHeadsLinear.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
        mActivityHasPublishedByUserText.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home_page:
                Intent d = new Intent(PersonalCenterActivity.this, HomePageActivity.class);
                startActivity(d);
                break;
            case R.id.tv_concern:
                Intent b = new Intent(PersonalCenterActivity.this, ConcernActivity.class);
                startActivity(b);
                break;
            case R.id.tv_publish:
                Intent c = new Intent(PersonalCenterActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Toast.makeText(this,"当前所处页面就是个人中心页面",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.linear_personal:
                Intent e = new Intent(PersonalCenterActivity.this, MyselfPersonalMessageActivity.class);
                startActivity(e);
                break;
            case  R.id.tv_cancel:
                PreferenceUtil.setString(PersonalCenterActivity.this, PreferenceUtil.ACCOUNT, "");
                Intent f = new Intent(PersonalCenterActivity.this, LoginActivity.class);
                startActivity(f);
                break;
            case R.id.tv_activity_has_published_by_user:
                Bundle bundle = new Bundle();
                bundle.putString("userAccount", userAccount);
                Intent intent = new Intent(PersonalCenterActivity.this, ActivityHasPublishedByUserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}