package com.example.yang.meetyou.userMessageCenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.DownloadImageTask;
import com.example.yang.meetyou.utils.OkHttpClientManager;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/26.
 */
public class MyselfPersonalMessageActivity extends AppCompatActivity implements View.OnClickListener{
    private final static int TOOK_PHOTO = 1;
    private final static int SELET_IN_PHONE = 2;
    private final static int REQUEST_TO_PHOTOCUTED_AND_SEND_TO_SEVER = 3;
    public static final String PICTURE_FILE="MeetYou/heads.jpg";
    private static final String TAG = "PersonalMessageActivity";

    private static  final  int SHOW_TOAST = 11;
    private static final int SET_ACCOUNT =12;
    private static final int SET_NICKNAME = 13;
    private static final int SET_HEADS = 14;
    private static final int SET_SEX =15;
    private static final int SET_CONTACT = 16;
    private static final int SET_SIGNATURE = 17;
    private static final int SET_PRIVACY = 18;


    private RelativeLayout nickname_rl;
    private RelativeLayout sex_rl;
    private RelativeLayout signature_rl;
    private RelativeLayout head_rl;
    private TextView sex_tv;
    private ImageView head_iv;
    public static TextView nickname_tv;
    public static TextView account_tv;
    public static TextView signature_tv;
    public static TextView contacts_tv;
    public static Button privacy_bt;
    private static RelativeLayout  contact_rl;
    private RelativeLayout privacy_rl;

    private String sex ="";
    private PopupWindow mPopWindow;
    private Uri  imageUri;

    final OkHttpClient mClient = new OkHttpClient();


    private String msg;

    private int index = 2;
    private int privacy_index =0;

    protected void getUserMessage () {

        final String account = PreferenceUtil.getString(MyselfPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String requestURL = " http://139.199.180.51/meetyou/public/userInfo?user_account=" + account;

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
                            String response2 = response.body().string();
                            JSONObject jsonObject = new JSONObject(response2);
                            Log.i(TAG, response2);
                            int  status = jsonObject.getInt("msgCode");
                            Log.i("123", status + "");
                            msg = jsonObject.getString("msg");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            if (status == 301) {
                                handler.obtainMessage(SET_HEADS, jsonObject1.getString("user_image")).sendToTarget();
                                handler.obtainMessage(SET_NICKNAME, jsonObject1.getString("user_nickName")).sendToTarget();
                                handler.obtainMessage(SET_ACCOUNT, jsonObject1.getString("user_account")).sendToTarget();
                                handler.obtainMessage(SET_SEX, jsonObject1.getString("user_sex")).sendToTarget();
                                handler.obtainMessage(SET_CONTACT,jsonObject1.getString("user_contacts")).sendToTarget();
                                handler.obtainMessage(SET_SIGNATURE, jsonObject1.getString("user_description")).sendToTarget();
                                handler.obtainMessage(SET_PRIVACY,jsonObject1.getString("user_privacy")).sendToTarget();
                            }else if(status == 302){
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                            }

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
            }
        }).start();

    }


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(MyselfPersonalMessageActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_NICKNAME:
                    nickname_tv.setText(msg.obj.toString());
                    break;
                case SET_ACCOUNT:
                    account_tv.setText(msg.obj.toString());
                    break;
                case SET_SEX:
                    sex_tv.setText(msg.obj.toString());
                    break;
                case SET_CONTACT:
                    contacts_tv.setText(msg.obj.toString());
                    break;
                case SET_SIGNATURE:
                    signature_tv.setText(msg.obj.toString());
                    break;
                case SET_PRIVACY:
                    if (msg.obj.toString().equals("0")) {
                        privacy_bt.setText("是");
                    }else{
                        privacy_bt.setText("否");
                    }
                    break;


                case SET_HEADS:
                    new DownloadImageTask(head_iv).execute(msg.obj.toString());
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myself_personal_meaasge);
        nickname_rl = (RelativeLayout) findViewById(R.id.rl_name);
        sex_rl = (RelativeLayout) findViewById(R.id.rl_sex);
        signature_rl = (RelativeLayout) findViewById(R.id.rl_signature);
        head_rl = (RelativeLayout) findViewById(R.id.rl_head);
        privacy_rl = (RelativeLayout) findViewById(R.id.rl_user_privacy);

        head_iv = (ImageView) findViewById(R.id.iv_head);
        contact_rl = (RelativeLayout) findViewById(R.id.rl_contact);
        nickname_tv = (TextView) findViewById(R.id.tv_nickname);
        account_tv = (TextView) findViewById(R.id.tv_account);
        sex_tv = (TextView) findViewById(R.id.tv_user_sex);
        contacts_tv =(TextView)findViewById(R.id.tv_user_contact);
        signature_tv = (TextView) findViewById(R.id.tv_user_signature);
        privacy_bt = (Button) findViewById(R.id.bt_privacy);



        getUserMessage();

        File tmpDir = new File(Environment.getExternalStorageDirectory()+ "/picture.");
        if(!tmpDir.exists())
        {
            tmpDir.mkdir();
        }
        File realImg = new File(tmpDir.getAbsolutePath() + "avater2.jpg");
        imageUri = Uri.fromFile(realImg);


        privacy_rl.setOnClickListener(this);
        nickname_rl.setOnClickListener(this);
        sex_rl.setOnClickListener(this);
        signature_rl.setOnClickListener(this);
        head_rl.setOnClickListener(this);
        contact_rl.setOnClickListener(this);
    }

    public void showSexDialog() {
        final String[] items = new String[3];
        items[0] = "男";
        items[1]="女";
        items[2]= "未知";
        sex = "未知";
        index = 2;
        AlertDialog.Builder builder = new AlertDialog.Builder(MyselfPersonalMessageActivity.this)
                .setTitle("你的性别是？")
                .setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex = items[which];
                        index = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String account = PreferenceUtil.getString(MyselfPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String requestURL = "http://139.199.180.51/meetyou/public/updateUserInfo?operation=SEX&user_account="+account+"&value="+index;

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
//                                            Log.i(TAG, response.body().string());
                                            int  status = jsonObject.getInt("msgCode");
                                            if (status == 305) {
                                                handler.obtainMessage(SHOW_TOAST, jsonObject.getString("msg")).sendToTarget();
                                                handler.obtainMessage(SET_SEX, jsonObject.getJSONObject("data").getString("sex")).sendToTarget();

                                            }else{
                                                handler.obtainMessage(SHOW_TOAST, jsonObject.getString("msg")).sendToTarget();
                                            }
                                        } catch (JSONException je) {
                                            je.printStackTrace();
                                        }
                                    } else {
                                        throw new IOException("Unexpected code " + response);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyselfPersonalMessageActivity.this,"取消",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(MyselfPersonalMessageActivity.this).inflate(R.layout.pop_change_head, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置点击窗口外边窗口消失
        mPopWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPopWindow.setFocusable(false);

        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        Button tv1 = (Button)contentView.findViewById(R.id.took_photo);
        Button tv2 = (Button)contentView.findViewById(R.id.select_in_phone);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        //显示PopupWindow
        View rootview = LayoutInflater.from(MyselfPersonalMessageActivity.this).inflate(R.layout.myself_personal_meaasge, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

    public void cutImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",100 );
        intent.putExtra("outputY", 100);
        intent.putExtra("outputFormat",  Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_TO_PHOTOCUTED_AND_SEND_TO_SEVER);
    }

    private void sendPrivacy() {
        if (privacy_bt.getText().toString().equals("是")) {
            privacy_index = 1;
        }else {
            privacy_index = 0;
        }
        final String account = PreferenceUtil.getString(MyselfPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String requestURL = "http://139.199.180.51/meetyou/public/changePrivacy?user_account="+account+"&user_privacy=+"+privacy_index;

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
//                                            Log.i(TAG, response.body().string());
                            int  status = jsonObject.getInt("msgCode");
                            if (status == 307) {
                                handler.obtainMessage(SHOW_TOAST, jsonObject.getString("msg")).sendToTarget();
                                handler.obtainMessage(SET_PRIVACY, jsonObject.getJSONObject("data").getInt("privacy")).sendToTarget();
                            }else{
                                handler.obtainMessage(SHOW_TOAST, jsonObject.getString("msg")).sendToTarget();
                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_name:
                NickNameDialogFragment nickNameDialog = NickNameDialogFragment.newInstance(MyselfPersonalMessageActivity.this);
                nickNameDialog.show(this.getSupportFragmentManager(),"NickNameDialogFragment");
                break;
            case R.id.rl_sex:
                showSexDialog();
                break;
            case R.id.rl_signature:
                SignatureDialogFragment signatureDialogFragment = SignatureDialogFragment.newInstance(MyselfPersonalMessageActivity.this);
                signatureDialogFragment.show(getSupportFragmentManager(),"SignatureDialogFragment");
                break;
            case R.id.rl_head:
                showPopupWindow();
                break;
            case R.id.took_photo:
                mPopWindow.dismiss();
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                it.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(it, TOOK_PHOTO);
                break;
            case R.id.select_in_phone:
                mPopWindow.dismiss();
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, SELET_IN_PHONE);
                break;
            case R.id.rl_contact:
                ContactDialogFragment contactDialogFragment = ContactDialogFragment.newInstance(MyselfPersonalMessageActivity.this);
                contactDialogFragment.show(this.getSupportFragmentManager(), "ContactDialogFragment");
                break;
            case R.id.rl_user_privacy:
                sendPrivacy();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            mPopWindow = null;
        }
        return super.onTouchEvent(event);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TOOK_PHOTO:
                    //根据所拍图片保存的文件路径获取拍照获得的Bitmap
//                    Bitmap b = data.getExtras().getParcelable("data");
                    Bitmap b = BitmapFactory.decodeFile(imageUri.getPath());
                    Uri uriCapture = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), b, null,null));
                    cutImage(uriCapture);
                    break;
                case SELET_IN_PHONE:
                    Uri uriSelectInPhone = data.getData();
                    cutImage(uriSelectInPhone);
                    break;
                case REQUEST_TO_PHOTOCUTED_AND_SEND_TO_SEVER:
                    Bundle bundle = data.getExtras();
                   Bitmap q = bundle.getParcelable("data");//获得截取头像的Bitmap
//                    File targetFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + PICTURE_FILE);
//                    Bitmap q = BitmapFactory.decodeFile(targetFile.getPath());

                    String account = PreferenceUtil.getString(MyselfPersonalMessageActivity.this, PreferenceUtil.ACCOUNT);

                    try {
                       File file = new File(imageUri.getPath());
                        OkHttpClientManager.postAsyn("http://139.199.180.51/meetyou/public/uploadImage",//
                                new OkHttpClientManager.ResultCallback<String>() {
                                    @Override
                                    public void onError(com.squareup.okhttp.Request request, Exception e) {

                                    }

                                    @Override
                                    public void onResponse(String result) {
                                        try {
                                            Log.i("123", result);
                                            JSONObject jsonObject = new JSONObject(result);
                                            int status = jsonObject.getInt("msgCode");
                                            Log.i("123", status + "");
                                            msg = jsonObject.getString("msg");
                                            Log.i("123", msg);
                                            String url = jsonObject.getJSONObject("data").getString("headPic");
                                            new DownloadImageTask(head_iv).execute(url);
                                        } catch (JSONException je) {
                                            je.printStackTrace();
                                        }
                                    }
                                },//
                                file,//
                                "file",//
                                new OkHttpClientManager.Param[]{
                                        new OkHttpClientManager.Param("user_account", account)}
                        );
                    } catch (Exception io) {
                        io.printStackTrace();
                    }
                    break;
            }
        }
    }
}
