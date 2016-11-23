package com.example.yang.meetyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.accounts.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2016/9/24.
 */
public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String FIRST_USE = "first_use";

    ListView activityListView;
    List<Huodong> mHuodongList = new ArrayList<>();
    EditText  mEditText;
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;

    private SharedPreferences sp;

    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_page);
        activityListView = (ListView) findViewById(R.id.lv_activity);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(FIRST_USE, true)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
           for(int i =0;i<10;i++) {
            Huodong activity = new Huodong();
            activity.setKind(getResources().getDrawable(R.mipmap.kobi));
            activity.setPublisherId("201430614243");
            activity.setPublishTime("2016.09.30");
            activity.setTheme("C10约球的走起");
            mHuodongList.add(activity);
        }

        activityListView.setAdapter(new ActivityAdapter(HomePageActivity.this,R.layout.home_page_listview_item, mHuodongList));

        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent a = new Intent(HomePageActivity.this, ActivityContentActivity.class);
                startActivity(a);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home_page:
                Toast.makeText(this,"当前所处页面就是主页",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_concern:
                Intent b = new Intent(HomePageActivity.this, ConcernActivity.class);
                startActivity(b);
                break;
            case R.id.tv_publish:
                Intent c = new Intent(HomePageActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Intent d = new Intent(HomePageActivity.this, PersonalCenterActivity.class);
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
