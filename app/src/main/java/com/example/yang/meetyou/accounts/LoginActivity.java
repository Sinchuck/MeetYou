package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.PreferenceUtil;
import com.example.yang.meetyou.utils.RegexUtils;
import com.example.yang.meetyou.views.CleanEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private CleanEditText mAccountEt;
    private CleanEditText mPasswordEt;

    private Button mLoginButton;
    private Button mSignUpButton;

    private TextView mForgetPasswordTextView;

    private CheckBox mRememberPasswordCb;

    final OkHttpClient mClient = new OkHttpClient();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    int status;

    String account;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        if (!PreferenceUtil.getString(LoginActivity.this, PreferenceUtil.ACCOUNT).equals("")) {
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }

        initViews();

        clickLogin();

        clickSignUp();
    }

    private void initViews() {
        mAccountEt = (CleanEditText) findViewById(R.id.et_account);
        mAccountEt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPasswordEt = (CleanEditText) findViewById(R.id.et_password);
        mPasswordEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPasswordEt.setImeOptions(EditorInfo.IME_ACTION_GO);
        mPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                }
                return false;
            }
        });

        mLoginButton = (Button) findViewById(R.id.bt_login);
        mSignUpButton = (Button) findViewById(R.id.bt_sign_up);

        mForgetPasswordTextView = (TextView) findViewById(R.id.tv_forget_password);
        mForgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        mRememberPasswordCb = (CheckBox) findViewById(R.id.cb_remember_password);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void clickLogin() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }

        });
    }

    private void login() {
        account = mAccountEt.getText().toString().trim();
        password = mPasswordEt.getText().toString().trim();

        if (checkInput(account, password)) {
                mEditor = mSharedPreferences.edit();
                mEditor.putBoolean(HomePageActivity.FIRST_USE, false);
                mEditor.commit();

            new GetStatus().execute();
        }
    }

    private void clickSignUp() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkInput(String account, String password) {
        //账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, "请输入你的账号", Toast.LENGTH_SHORT).show();
        } else if (!RegexUtils.checkAccount(account)) {
                Toast.makeText(this, "账号格式错误", Toast.LENGTH_SHORT).show();
        } else if (password == null || password.trim().equals("")) {
            Toast.makeText(this, "请输入你的密码", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private class GetStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String requestURL = "http://139.199.180.51/meetyou/public/login?user_account=" + account
                    + "&user_passwd=" + password;

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
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        status = jsonObject.getInt("msgCode");
                        Log.i(TAG, status + "");
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean sta) {
            if (status == 101) {
                Toast.makeText(LoginActivity.this, "用户账号不存在", Toast.LENGTH_SHORT).show();
            } else if (status == 103) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                PreferenceUtil.setString(LoginActivity.this, PreferenceUtil.ACCOUNT,account);
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            } else if (status == 102){
                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            }
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
