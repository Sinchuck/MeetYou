package com.example.yang.meetyou.userMessageCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.yang.meetyou.ActivityHasPublishedByUserActivity;
import com.example.yang.meetyou.R;

/**
 * Created by Yang on 2016/9/26.
 */
public class OthersPersonalMessageActivity extends AppCompatActivity {
    TextView otherPublishedActivityTextView;
    TextView mActivityNameTextView;
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

    }
}
