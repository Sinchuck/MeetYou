package com.example.yang.meetyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.accounts.LoginActivity;

/**
 * Created by Yang on 2016/9/25.
 */
public class PersonalCenterActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;
    LinearLayout mHeadsLinear;
    TextView mCancelText;
    TextView mActivityHasPublishedByUserText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);
        mHeadsLinear = (LinearLayout)findViewById(R.id.linear_personal);
        mCancelText = (TextView) findViewById(R.id.tv_cancel);
        mActivityHasPublishedByUserText = (TextView) findViewById(R.id.tv_activity_has_published_by_user);

        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
        mHeadsLinear.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
        mActivityHasPublishedByUserText.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home_page:
                Intent d = new Intent(PersonalCenterActivity.this, HomePageActivity.class);
                startActivity(d);
                break;
            case R.id.tv_concern:
                Intent b = new Intent(PersonalCenterActivity.this, ConcernActivity.class);
                startActivity(b);
                break;
            case R.id.tv_publish:
                Intent c = new Intent(PersonalCenterActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Toast.makeText(this,"当前所处页面就是个人中心页面",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.linear_personal:
                Intent e = new Intent(PersonalCenterActivity.this, MyselfPersonalMessageActivity.class);
                startActivity(e);
                break;
            case  R.id.tv_cancel:
                Intent f = new Intent(PersonalCenterActivity.this, LoginActivity.class);
                startActivity(f);
                break;
            case R.id.tv_activity_has_published_by_user:
                startActivity(new Intent(PersonalCenterActivity.this, ActivityHasPublishedByUserActivity.class));
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
