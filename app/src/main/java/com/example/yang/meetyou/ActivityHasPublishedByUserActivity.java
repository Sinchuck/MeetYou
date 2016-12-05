package com.example.yang.meetyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2016/9/26.
 */
public class ActivityHasPublishedByUserActivity extends AppCompatActivity {
    ListView mActivityHasPublishedListView;
    List<Huodong> mHuodongHasPublishedList = new ArrayList<>();

    TextView  mActivityNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_published_by_user);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("用户已发布的活动");
        mActivityHasPublishedListView = (ListView) findViewById(R.id.lv_activity_has_published_by_user);
        for (int i = 0; i < 10; i++) {
            Huodong activity = new Huodong();
//            activity.setKind(getResources().getDrawable(R.mipmap.kobi));
//            activity.setPublisherId("201430614243");
//            activity.setPublishTime("2016.09.30");
//            activity.setTheme("C10约球的走起");
            mHuodongHasPublishedList.add(activity);
        }
        mActivityHasPublishedListView.setAdapter(new ActivityAdapter
                (ActivityHasPublishedByUserActivity.this, R.layout.home_page_listview_item, mHuodongHasPublishedList));

        mActivityHasPublishedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent a = new Intent(ActivityHasPublishedByUserActivity.this,ActivityContentActivity.class);
                startActivity(a);
            }
        });

    }


}
