package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.RegexUtils;
import com.example.yang.meetyou.views.CleanEditText;

public class LoginActivity extends AppCompatActivity {

    private CleanEditText mAccountEt;
    private CleanEditText mPasswordEt;
    private Button mLoginButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

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
        String account = mAccountEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        if (checkInput(account, password)) {
            //这里需补填请求服务器代码
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
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

    private void clickSignUp() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
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
        } else {                //这里需要判断密码是否正确
            return true;
        }
        return false;
    }
}
