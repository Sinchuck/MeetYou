package com.example.yang.meetyou.userMessageCenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;

/**
 * Created by Yang on 2016/9/26.
 */
public class MyselfPersonalMessageActivity extends AppCompatActivity implements View.OnClickListener{
    private final static int TOOK_PHOTO = 1;
    private final static int SELET_IN_PHONE = 2;
    private final static int REQUEST_TO_PHOTOCUTED_AND_SEND_TO_SEVER = 3;
    public static final String PICTURE_FILE="MeetYou/heads.jpg";


    private TextView mActivityNameTextView;
    private RelativeLayout nickname_rl;
    private RelativeLayout sex_rl;
    private RelativeLayout signature_rl;
    private RelativeLayout head_rl;
    private TextView sex_tv;
    private ImageView head_iv;

    private String sex ="";
    private PopupWindow mPopWindow;
    private Uri  imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myself_personal_meaasge);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        nickname_rl = (RelativeLayout) findViewById(R.id.rl_name);
        sex_rl = (RelativeLayout) findViewById(R.id.rl_sex);
        sex_tv = (TextView) findViewById(R.id.user_sex_tv);
        signature_rl = (RelativeLayout) findViewById(R.id.rl_signature);
        head_rl = (RelativeLayout) findViewById(R.id.rl_head);
        head_iv = (ImageView) findViewById(R.id.iv_head);

//        String path = Environment.getExternalStorageDirectory().getPath() + "/" + PICTURE_FILE;
//        Log.i("147258", path);
//        File file = new File(path);
        File tmpDir = new File(Environment.getExternalStorageDirectory()+ "/picture.");
        if(!tmpDir.exists())
        {
            tmpDir.mkdir();
        }
        File realImg = new File(tmpDir.getAbsolutePath() + "avater2.jpg");
        imageUri = Uri.fromFile(realImg);


        nickname_rl.setOnClickListener(this);
        sex_rl.setOnClickListener(this);
        signature_rl.setOnClickListener(this);
        head_rl.setOnClickListener(this);
        mActivityNameTextView.setText("个人详细信息");
    }

    public void showSexDialog() {
        final String[] items = new String[3];
        items[0] = "男";
        items[1]="女";
        items[2]= "未知";
        sex = "未知";
        AlertDialog.Builder builder = new AlertDialog.Builder(MyselfPersonalMessageActivity.this)
                .setTitle("你的性别是？")
                .setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex = items[which];
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyselfPersonalMessageActivity.this,"确定",Toast.LENGTH_LONG).show();
                        sex_tv.setText(sex);
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
        intent.putExtra("outputX",180 );
        intent.putExtra("outputY", 180);
        intent.putExtra("outputFormat",  Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_TO_PHOTOCUTED_AND_SEND_TO_SEVER);
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

//                    if(data == null)
//                    {
//                        return;
//                    }else{
//                        Bundle cameraBundle = data.getExtras();
//                        if(cameraBundle!=null){
//                            Bitmap cameraBitmap = cameraBundle.getParcelable("data");
//                            File tmpDir = new File(Environment.getExternalStorageDirectory()+ "/picture.");
//                            if(!tmpDir.exists())
//                            {
//                                tmpDir.mkdir();
//                            }
//                            File realImg = new File(tmpDir.getAbsolutePath() + "avater2.jpg");
//                            try {
//                                FileOutputStream fos = new FileOutputStream(realImg);
//                                cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
//                                fos.flush();
//                                fos.close();
//                                Uri cameraFromFile = Uri.fromFile(realImg);
//                                //裁剪图片
//                                cutImage(cameraFromFile);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
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

                    head_iv.setImageBitmap(q);
                    break;

            }
        }
    }
}
