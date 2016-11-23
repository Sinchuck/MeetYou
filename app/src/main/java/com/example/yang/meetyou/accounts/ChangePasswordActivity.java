package com.example.yang.meetyou.accounts;

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

public class ChangePasswordActivity extends AppCompatActivity {

    private CleanEditText mNewPasswordEt;
    private CleanEditText mNewPasswordAgainEt;

    private Button mChangePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
            // TODO:这里编写请求服务器代码，更改密码
        }
    }

    private boolean isInputSame(String newPassword, String newPasswordAgain) {
        return newPassword.equals(newPasswordAgain);
    }
}
