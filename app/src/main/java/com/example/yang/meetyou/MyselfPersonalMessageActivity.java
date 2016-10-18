package com.example.yang.meetyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Yang on 2016/9/26.
 */
public class MyselfPersonalMessageActivity extends AppCompatActivity{
    TextView mActivityNameTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myself_personal_meaasge);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("个人详细信息");
    }
}
