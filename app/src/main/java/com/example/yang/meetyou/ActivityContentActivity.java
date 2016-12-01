package com.example.yang.meetyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yang.meetyou.userMessageCenter.OthersPersonalMessageActivity;

/**
 * Created by Yang on 2016/9/26.
 */
public class ActivityContentActivity extends AppCompatActivity {
    LinearLayout mHeadsLinear;
    TextView mActivityNameTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("活动详情");
        mHeadsLinear = (LinearLayout)findViewById(R.id.linear_other_heads);
        mHeadsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(ActivityContentActivity.this, OthersPersonalMessageActivity.class);
                startActivity(a);
            }
        });
    }

}
