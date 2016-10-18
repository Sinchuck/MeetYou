package com.example.yang.meetyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yang on 2016/9/25.
 */
public class PublishActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);


        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home_page:
                Intent c = new Intent(PublishActivity.this, HomePageActivity.class);
                startActivity(c);

                break;
            case R.id.tv_concern:
                Intent b = new Intent(PublishActivity.this, ConcernActivity.class);
                startActivity(b);
                break;
            case R.id.tv_publish:
                Toast.makeText(this,"当前所处页面就是发布页",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_personalCenter:
                Intent d = new Intent(PublishActivity.this, PersonalCenterActivity.class);
                startActivity(d);
                break;
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
