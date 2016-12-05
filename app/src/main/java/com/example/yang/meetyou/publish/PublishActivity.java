package com.example.yang.meetyou.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.ConcernActivity;
import com.example.yang.meetyou.HomePageActivity;
import com.example.yang.meetyou.R;
import com.example.yang.meetyou.userMessageCenter.PersonalCenterActivity;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public static EditText theme_et;
    public static  EditText content_et;
    private TextView sure_publish;



    private DatePickerDialog mDatePickerDialog;
    private String dateString;
    private String activity_kind;
    private String num_of_activity_person;
    private  String theme;
    private  String content;
    private String msg;
    private int status;

    final OkHttpClient mClient = new OkHttpClient();

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
        sure_publish = (TextView) findViewById(R.id.tv_sure_publish);

        theme_et.setText("");
        content_et.setText("");

        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
        theme_et.setOnClickListener(this);
        content_et.setOnClickListener(this);
        activity_kind_tv.setOnClickListener(this);
        num_of_person_tv.setOnClickListener(this);
        activity_time_tv.setOnClickListener(this);
        sure_publish.setOnClickListener(this);
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
        final String[] items = new String[6];

        items[0]="体育";
        items[1]= "手游";
        items[2]="桌游";
        items[3]="游戏";
        items[4]="学习";
        items[5]="其他";
        activity_kind = "";
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
                        if (activity_kind.equals("")) {
                            activity_kind ="体育";
                        }
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
        final String[] items = new String[10];
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
            case R.id.tv_sure_publish:
                theme = theme_et.getText().toString();
                content = content_et.getText().toString();
                if (activity_kind.equals("") || theme.equals("") || dateString.equals("") || content.equals("") || num_of_activity_person.equals("")) {
                    Toast.makeText(PublishActivity.this,"请填写完所有信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                new GetStatus().execute();
                break;

        }
    }

    private class GetStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            int type = 1;
            switch (activity_kind) {
                case "体育":
                    type = 1;
                    break;
                case "手游":
                    type = 2;
                    break;
                case "桌游":
                    type = 3;
                    break;
                case "游戏":
                    type = 4;
                    break;
                case "学习":
                    type = 5;
                    break;
                case "其他":
                    type = 6;
                    break;
            }

            String requestURL = "http://139.199.180.51/meetyou/public/release?activity_tag="+type+"&activity_theme="+theme+"&activity_time="+dateString
                    +"&activity_details="+content +"&activity_participants_max_count="+num_of_activity_person+"&user_account="+ PreferenceUtil.getString(PublishActivity.this, PreferenceUtil.ACCOUNT);

            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url(requestURL)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        status = jsonObject.getInt("msgCode");
                        Log.i("123", status + "");
                         msg = jsonObject.getString("msg");
                        Log.i("123", msg);
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean sta) {
            if (status == 406) {
                Toast.makeText(PublishActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else if (status == 405) {
                Toast.makeText(PublishActivity.this, msg, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PublishActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
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
