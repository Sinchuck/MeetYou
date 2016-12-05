package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.RegexUtils;
import com.example.yang.meetyou.views.CleanEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SignUpActivity";

    private static int gender = 0;
    private int status = 0;

    private CleanEditText mAccountEt;
    private CleanEditText mPasswordEt;
    private CleanEditText mNicknameEt;
    private CleanEditText mPhoneNumberEt;

    private Button mCreateAccountBtn;

    private Spinner mGenderSpinner;

    String account;
    String password;
    String nickname;
    String phoneNumber;

    final OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    private void initViews() {

        mAccountEt = (CleanEditText) findViewById(R.id.et_sing_up_account);
        mAccountEt.setImeOptions(EditorInfo.IME_ACTION_NEXT); //下一步
        mPasswordEt = (CleanEditText) findViewById(R.id.et_sing_up_password);
        mPasswordEt.setImeOptions(EditorInfo.IME_ACTION_NEXT); //下一步
        mNicknameEt = (CleanEditText) findViewById(R.id.et_nickname);
        mNicknameEt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPhoneNumberEt = (CleanEditText) findViewById(R.id.et_phone_number);
        mPhoneNumberEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPhoneNumberEt.setImeOptions(EditorInfo.IME_ACTION_GO);
        mPhoneNumberEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    singUp();
                }
                return false;
            }
        });

        mGenderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapter);
        mGenderSpinner.setOnItemSelectedListener(this);

        mCreateAccountBtn = (Button) findViewById(R.id.btn_create_account);
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });
    }

    private void singUp() {

        account = mAccountEt.getText().toString().trim();
        password = mPasswordEt.getText().toString().trim();
        nickname = mNicknameEt.getText().toString().trim();
        phoneNumber = mPhoneNumberEt.getText().toString().trim();

        if (checkInfo(account, password, nickname)) {
            new GetStatus().execute();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.i(TAG, "位置" + pos);
        gender = genderNumber(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    private int genderNumber(int pos) {
        return pos;
    }

    private boolean checkInfo(String account, String password, String nickname) {
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, "请使用你的学生账号注册", Toast.LENGTH_SHORT).show();
        } else if (!RegexUtils.checkAccount(account)) {
            Toast.makeText(this, "账号格式错误", Toast.LENGTH_SHORT).show();
        } else if (password == null || password.trim().equals("")) {
            Toast.makeText(this, "请输入你的密码", Toast.LENGTH_SHORT).show();
        } else if (!RegexUtils.checkPassword(password)) {
            Toast.makeText(this, "密码需包含英文字母和数字，且在6到20位之间",
                    Toast.LENGTH_LONG).show();
        } else if (nickname == null || nickname.trim().equals("")) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
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
            String requestURL = "http://139.199.180.51/meetyou/public/register?user_account=" + account
                    + "&user_passwd=" + password + "&user_nickName=" + nickname
                    +"&user_sex=" + gender + "&user_contacts=" + phoneNumber;

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
            if (status == 201) {
                Toast.makeText(SignUpActivity.this, "该用户已注册", Toast.LENGTH_SHORT).show();
            } else if (status == 202) {
                Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
