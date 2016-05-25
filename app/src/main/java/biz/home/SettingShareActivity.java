package biz.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import biz.home.R;

/**
 * Created by ksdhc on 2015/7/27.
 */
public class SettingShareActivity extends Activity implements View.OnClickListener,
        View.OnLongClickListener{
    private ImageView back,weixin,weixinFriend,qq,qqSpace,note;
    private EditText noteText;
    private TextView title;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_share);
        initView();
        initData();
    }

    private void initView(){
        back=(ImageView)findViewById(R.id.setting_share_back_img);
        title=(TextView)findViewById(R.id.setting_share_title);
        weixin=(ImageView)findViewById(R.id.setting_share_weixin);
        weixinFriend=(ImageView)findViewById(R.id.setting_share_weixin_friends);
        qq=(ImageView)findViewById(R.id.setting_share_qq);
        qqSpace=(ImageView)findViewById(R.id.setting_share_qq_space);
        note=(ImageView)findViewById(R.id.setting_share_note);
        noteText=(EditText)findViewById(R.id.setting_share_text);
        noteText.setFocusableInTouchMode(false);
        back.setOnClickListener(this);
        weixin.setOnClickListener(this);
        weixinFriend.setOnClickListener(this);
        qq.setOnClickListener(this);
        qqSpace.setOnClickListener(this);
        note.setOnClickListener(this);
        noteText.setOnLongClickListener(this);
    }

    private void initData(){
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
    }

    @Override
    public void onClick(View v) {
        text=noteText.getText().toString();
        Intent intent;
        switch (v.getId()){
            case R.id.setting_share_back_img:
                finish();
                break;
            case R.id.setting_share_weixin:
//                intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                intent.putExtra(Intent.EXTRA_TEXT, text);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, getTitle()));
                shareToWxFriend();
                break;
            case R.id.setting_share_weixin_friends:
//                intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_STREAM, "http://www.4350.biz/4350.ico");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                intent.putExtra(Intent.EXTRA_TEXT, text);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, getTitle()));
                shareToTimeLine();
                break;
            case R.id.setting_share_qq:
                shareToQQFriend();
                break;
            case R.id.setting_share_qq_space:
                break;
            case R.id.setting_share_note:
                Intent email =  new  Intent(android.content.Intent.ACTION_SEND);
                email.setType( "plain/text" );
                String  emailSubject =  "共享软件" ;
                //设置邮件默认地址
                // email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
                //设置邮件默认标题
                email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
                //设置要默认发送的内容
                email.putExtra(android.content.Intent.EXTRA_TEXT, text);
                //调用系统的邮件系统
                startActivityForResult(Intent.createChooser(email,  "请选择邮件发送软件" ), 1001 );
                break;
        }
    }
    private void shareToQQFriend() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "这是分享内容");
        startActivity(intent);
    }
    private void shareToWxFriend() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");

        intent.putExtra(Intent.EXTRA_TEXT, "这是分享内容");
        intent.putExtra(Intent.EXTRA_STREAM, "http://www.weixin.com");
        startActivity(intent);
    }
    private void shareToTimeLine() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);

        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        File file = new File("mnt/sdcard/psb.jpg");
//        Uri uri=new Uri("http://www.4350.biz/4350.ico") ;
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        // intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
        // ArrayList<Uri> uris = new ArrayList<Uri>();
        // for (int i = 0; i < images.size(); i++) {
        // Uri data = Uri.fromFile(new File(thumbPaths.get(i)));
        // uris.add(data);
        // }
        // intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        intent.setType("image/*");

        startActivity(intent);
    }
    public  void shareToMessage(){
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        sendIntent.putExtra("sms_body", "这是短信内容");
        startActivity(sendIntent);
    }

    @Override
    public boolean onLongClick(View v) {
        new AlertDialog.Builder(this)
                .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String info=noteText.getText().toString();
                        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData data=ClipData.newPlainText("info",info);
                        cmb.setPrimaryClip(data);
                        Toast.makeText(getBaseContext(), "粘贴文本成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        return true;
    }
}
