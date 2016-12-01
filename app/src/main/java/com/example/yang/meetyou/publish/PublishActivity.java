package com.example.yang.meetyou.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.ConcernActivity;
import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.userMessageCenter.PersonalCenterActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yang on 2016/9/25.
 */
public class PublishActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mHomePage;
    private TextView mConcern;
    private TextView mPublish;
    private TextView mPersonalCenter;
    private TextView activity_kind_tv;
    private TextView num_of_person_tv;
    private TextView activity_time_tv;
    private EditText theme_et;
    private EditText content_et;


    private DatePickerDialog mDatePickerDialog;
    private String dateString;
    private String activity_kind;
    private String num_of_activity_person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);
        theme_et = (EditText) findViewById(R.id.et_theme);
        content_et = (EditText) findViewById(R.id.et_content);
        activity_kind_tv = (TextView) findViewById(R.id.tv_activity_kind);
        num_of_person_tv = (TextView) findViewById(R.id.tv_num_of_activity_person);
        activity_time_tv = (TextView) findViewById(R.id.tv_activity_time);


        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
        theme_et.setOnClickListener(this);
        content_et.setOnClickListener(this);
        activity_kind_tv.setOnClickListener(this);
        num_of_person_tv.setOnClickListener(this);
        activity_time_tv.setOnClickListener(this);
    }

    public void showActivityTimeDialog() {
        mDatePickerDialog = new DatePickerDialog();
        mDatePickerDialog.setSelectedDate(new Date());
        mDatePickerDialog.show(this.getSupportFragmentManager(), "Date");
        mDatePickerDialog.setOnDatePickerClickListener(new DatePickerDialog.OnDatePickerClickListener() {
            @Override
            public void onCancelClick() {
                mDatePickerDialog.dismiss();
            }

            @Override
            public void onAcceptClick(Date date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                dateString = df.format(date);

                activity_time_tv.setText(dateString);
                mDatePickerDialog.dismiss();
            }
        });
    }

    public void showActivityKindDialog() {
        final String[] items = new String[7];
        items[0] = "未知";
        items[1]="体育";
        items[2]= "手游";
        items[3]="桌游";
        items[4]="游戏";
        items[5]="学习";
        items[6]="其他";
        activity_kind = "未知";
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this)
                .setTitle("活动的类别是？")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity_kind = items[which];
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PublishActivity.this,"确定",Toast.LENGTH_LONG).show();
                        activity_kind_tv.setText(activity_kind);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PublishActivity.this,"取消",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }


    public void showActivityNumPersonDialog() {
        final String[] items = new String[11];
        items[0] = "1";
        items[1]="2";
        items[2]= "3";
        items[3]="4";
        items[4]="5";
        items[5]="6";
        items[6]="7";
        items[7]="8";
        items[8] = "9";
        items[9] = "10";
        items[10] = "10+";

        num_of_activity_person = "1";
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this)
                .setTitle("活动的人数是？")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        num_of_activity_person  = items[which];
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PublishActivity.this,"确定",Toast.LENGTH_LONG).show();
                        num_of_person_tv.setText(num_of_activity_person);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PublishActivity.this,"取消",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }

    public void showEditThemeFragment() {
        EditThemeFragment editThemeFragment = EditThemeFragment.newInstance();
        editThemeFragment.show(this.getFragmentManager(), "EditThemeDialogFragment");
    }
    public void showEditContentFragment() {
        EditActivityContentDialogFragment editContentFragment = EditActivityContentDialogFragment.newInstance();
        editContentFragment.show(this.getFragmentManager(), "EditActivityContentDialogFragment");
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
            case R.id.tv_activity_time:
                showActivityTimeDialog();
                break;
            case R.id.tv_activity_kind:
                showActivityKindDialog();
                break;
            case R.id.tv_num_of_activity_person:
                showActivityNumPersonDialog();
                break;
            case R.id.et_theme:
                showEditThemeFragment();
                break;
            case R.id.et_content:
                showEditContentFragment();
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
