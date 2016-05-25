package biz.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import biz.home.assistActivity.SettingThanks;
import biz.home.assistActivity.UseAppItems;
import biz.home.yibuTask.UpdateAppTask;

/**
 * Created by lmw on 2015/7/27.
 */
public class SettingIntroduceActivity extends Activity implements View.OnClickListener{
    private ImageView back;
    private TextView title,tv_app_version;
    private LinearLayout sendMessage,sendMail,openCompanyWeb,laws,update,thanks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_introduce);
        initView();
        initData();
    }

    private void initView(){
        back=(ImageView)findViewById(R.id.setting_introduce_back_img);
        title=(TextView)findViewById(R.id.setting_introduce_title);
        sendMessage= (LinearLayout) findViewById(R.id.setting_introduce_sendMessage_linear);
        sendMail= (LinearLayout) findViewById(R.id.setting_introduce_sendMail);
        openCompanyWeb= (LinearLayout) findViewById(R.id.setting_introduce_web);
        laws= (LinearLayout) findViewById(R.id.setting_introduce_laws);
        update= (LinearLayout) findViewById(R.id.setting_introduce_update);
        thanks= (LinearLayout) findViewById(R.id.setting_introduce_thanks);
        tv_app_version= (TextView) findViewById(R.id.app_version_text);

        sendMail.setOnClickListener(this);
        back.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        openCompanyWeb.setOnClickListener(this);
        laws.setOnClickListener(this);
        update.setOnClickListener(this);
        thanks.setOnClickListener(this);
    }

    private void initData(){
        tv_app_version.setText("版本 "+SplashActivity.appVersion);
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_introduce_back_img:
                finish();
                break;
            case R.id.setting_introduce_sendMessage_linear:
//                Intent intent=new Intent(SettingIntroduceActivity.this, ProjectReleaseActivity.class);
//                startActivity(intent);
                break;
            case R.id.setting_introduce_sendMail:
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:bobking@4350.biz"));
                data.putExtra(Intent.EXTRA_SUBJECT, "关于Magic神奇秘书的建议");
                data.putExtra(Intent.EXTRA_TEXT, "  我觉得，");
                startActivity(data);
                break;
            case R.id.setting_introduce_web:
                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("http://www.4350.biz");
//                intent.setData(content_url);
//                startActivity(intent);
                intent=new Intent(SettingIntroduceActivity.this, UseAppItems.class);
                intent.putExtra("URL","companyWebsize");
                startActivity(intent);
                break;
            case R.id.setting_introduce_laws:
                intent=new Intent(SettingIntroduceActivity.this, UseAppItems.class);
                intent.putExtra("URL","userLaws");
                startActivity(intent);
                break;
            case R.id.setting_introduce_update:
//                Toast.makeText(getApplicationContext(),"点击了检查更新",Toast.LENGTH_LONG).show();
                UpdateAppTask task=new UpdateAppTask(this,UpdateAppTask.SETTINGREOURCE);
                task.execute(1000);
                break;
            case R.id.setting_introduce_thanks:
                intent=new Intent(SettingIntroduceActivity.this, SettingThanks.class);
                startActivity(intent);
                break;
        }
    }
}
