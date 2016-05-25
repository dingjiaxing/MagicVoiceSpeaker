package biz.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import biz.home.R;

/**
 * 神奇秘书形象设定
 * Created by lmw on 2015/7/27.
 */
public class SettingImageryActivity extends Activity implements View.OnClickListener{
    private ImageView back;         //返回按钮
    private TextView title;         //标题
    private LinearLayout backgroud,imagery,voice;   //背景，秘书形象，声音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_imagery);
        initView();
        initData();
    }

    private void initView(){
        back=(ImageView)findViewById(R.id.setting_imagery_back_img);                    //返回按钮
        title=(TextView)findViewById(R.id.setting_imagery_title);                       //标题
        backgroud=(LinearLayout)findViewById(R.id.setting_imagery_changeBackgroud);   //背景
        imagery=(LinearLayout)findViewById(R.id.setting_imagery_changeImagery);        //秘书形象
        voice=(LinearLayout)findViewById(R.id.setting_imagery_changeVoice);             //秘书声音
        backgroud.setOnClickListener(this);     //背景监听
        imagery.setOnClickListener(this);       //秘书形象 监听
        voice.setOnClickListener(this);         //声音监听
        back.setOnClickListener(this);          //返回按钮监听
    }

    private void initData(){
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_imagery_back_img:
                finish();
                break;
            case R.id.setting_imagery_changeBackgroud:
                Toast.makeText(this,"更换背景功能即将推出，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_imagery_changeImagery:
                Toast.makeText(this,"更换秘书形象功能即将推出，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_imagery_changeVoice:
                Toast.makeText(this,"更换秘书嗓音即将推出，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
