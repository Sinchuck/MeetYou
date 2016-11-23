package com.example.yang.meetyou.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yang.meetyou.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText mSecurityProblemAnswerEt;
    private Button mVerifyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mSecurityProblemAnswerEt = (EditText) findViewById(R.id.et_security_problem_answer);
        String answer = mSecurityProblemAnswerEt.getText().toString();

        mVerifyBtn = (Button) findViewById(R.id.btn_commit_security_problem_and_answer);
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    Toast.makeText(ForgetPasswordActivity.this, "密保问题回答正确",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetPasswordActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
