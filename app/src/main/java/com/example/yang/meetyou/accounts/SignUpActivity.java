package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.RegexUtils;
import com.example.yang.meetyou.views.CleanEditText;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private CleanEditText mAccountEt;
    private CleanEditText mPasswordEt;
    private CleanEditText mNicknameEt;

    private Button mCreateAccountBtn;

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
        mNicknameEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mNicknameEt.setImeOptions(EditorInfo.IME_ACTION_GO);
        mNicknameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    singUp();
                }
                return false;
            }
        });
        mCreateAccountBtn = (Button) findViewById(R.id.btn_create_account);
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });
    }

    private void singUp() {
        if (commitInfo()) {
            Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean commitInfo() {
        String account = mAccountEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String nickname = mNicknameEt.getText().toString().trim();

        if (checkInfo(account, password, nickname)) {
            // TODO:发送给服务器处理，注册成功后返回true，失败返回false
        }
        return true;
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
}
