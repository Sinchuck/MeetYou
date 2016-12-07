package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.R;
import com.example.yang.meetyou.views.CleanEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private CleanEditText mAccount;
    private CleanEditText mOldPasswordEt;
    private CleanEditText mNewPasswordEt;
    private CleanEditText mNewPasswordAgainEt;

    private Button mChangePasswordBtn;

    private String account = "";
    private String oldPassworld = "";
    private String newPassworld = "";

    final OkHttpClient mClient = new OkHttpClient();

    private int status = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAccount = (CleanEditText) findViewById(R.id.et_account);
        mAccount.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mOldPasswordEt = (CleanEditText) findViewById(R.id.et_old_password);
        mOldPasswordEt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mNewPasswordEt = (CleanEditText) findViewById(R.id.et_new_password);
        mNewPasswordEt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mNewPasswordAgainEt = (CleanEditText) findViewById(R.id.et_new_password_again);
        mNewPasswordAgainEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mNewPasswordAgainEt.setImeOptions(EditorInfo.IME_ACTION_GO);
        mNewPasswordAgainEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clickChange();
                return true;
            }
        });

        mChangePasswordBtn = (Button) findViewById(R.id.btn_change_password);
        mChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChange();
            }
        });
    }

    private void clickChange() {
        String newPassword = mNewPasswordEt.getText().toString();
        String newPasswordAgain = mNewPasswordAgainEt.getText().toString();


        if (!isInputSame(newPassword, newPasswordAgain)) {
            Toast.makeText(ChangePasswordActivity.this, "两次输入不一样，请重新输入",
                    Toast.LENGTH_SHORT).show();
            mNewPasswordEt.setText("");
            mNewPasswordAgainEt.setText("");
        } else {
            oldPassworld = mOldPasswordEt.getText().toString();
            newPassworld = mNewPasswordAgainEt.getText().toString();
            account = mAccount.getText().toString();
            new GetStatus().execute();
        }
    }

    private class GetStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String requestURL = "http://139.199.180.51/meetyou/public/changePassWd?user_account=" +
                    account + "&user_oldPasswd=" + oldPassworld + " &user_newPasswd=" + newPassworld;
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
            if (status == 303) {
                Toast.makeText(ChangePasswordActivity.this, "用户旧密码输入错误", Toast.LENGTH_SHORT).show();
            } else if (status == 304) {
                Toast.makeText(ChangePasswordActivity.this, "用户密码更改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean isInputSame(String newPassword, String newPasswordAgain) {
        return newPassword.equals(newPasswordAgain);
    }
}
