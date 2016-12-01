package com.example.yang.meetyou;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.publish.PublishActivity;
import com.example.yang.meetyou.userMessageCenter.OthersPersonalMessageActivity;
import com.example.yang.meetyou.userMessageCenter.PersonalCenterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2016/9/25.
 */
public class ConcernActivity extends AppCompatActivity implements View.OnClickListener {
    ListView mActivityListView;
    ListView mFriendListView;
    List<Huodong> mHuodongList = new ArrayList<>();
    List<Person> mPersonList = new ArrayList<>();
    TextView tv_concernActivity;
    TextView tv_concernFriend;
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concern);
        mActivityListView = (ListView) findViewById(R.id.list_view_concern_activity);
        mFriendListView = (ListView)findViewById(R.id.list_view_concern_friend);
        tv_concernActivity = (TextView) findViewById(R.id.tv_concernActivity);
        tv_concernFriend = (TextView) findViewById(R.id.tv_concernFriend);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);


        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);

        tv_concernActivity.setTextColor(Color.rgb(34,144, 175));
        tv_concernFriend.setTextColor(Color.rgb(82, 71, 71));
        tv_concernActivity.setOnClickListener(this);
        tv_concernFriend.setOnClickListener(this);
        
        for (int i = 0; i < 10; i++) {
            Huodong activity = new Huodong();
            activity.setKind(getResources().getDrawable(R.mipmap.kobi));
            activity.setPublisherId("201430614243");
            activity.setPublishTime("2016.09.30");
            activity.setTheme("C10约球的走起");
            mHuodongList.add(activity);
        }
        for (int i = 0; i < 10; i++) {
            Person person = new Person(getResources().getDrawable(R.mipmap.heads), "我是谁");
            mPersonList.add(person);
        }
        mActivityListView.setAdapter(new ActivityAdapter(ConcernActivity.this, R.layout.home_page_listview_item, mHuodongList));
        mActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent a = new Intent(ConcernActivity.this,ActivityContentActivity.class);
                startActivity(a);
            }
        });
        mFriendListView.setAdapter(new PersonAdapter(ConcernActivity.this, R.layout.concern_friend_list_item, mPersonList));
        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent b = new Intent(ConcernActivity.this, OthersPersonalMessageActivity.class);
                startActivity(b);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_concernActivity:
                mFriendListView.setVisibility(view.GONE);
               mActivityListView.setVisibility(View.VISIBLE);
                tv_concernActivity.setTextColor(Color.rgb(34,144, 175));
                tv_concernFriend.setTextColor(Color.rgb(82, 71, 71));

                break;
            case R.id.tv_concernFriend:
                mActivityListView.setVisibility(View.GONE);
                mFriendListView.setVisibility(view.VISIBLE);
                tv_concernFriend.setTextColor(Color.rgb(34,144, 175));
                tv_concernActivity.setTextColor(Color.rgb(82, 71, 71));

                break;

            case R.id.tv_home_page:
                Intent a = new Intent(ConcernActivity.this, HomePageActivity.class);
                startActivity(a);
                break;
            case R.id.tv_concern:
                    Toast.makeText(this, "当前所处页面就是关注页面", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.tv_publish:
                Intent c = new Intent(ConcernActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Intent d = new Intent(ConcernActivity.this, PersonalCenterActivity.class);
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

