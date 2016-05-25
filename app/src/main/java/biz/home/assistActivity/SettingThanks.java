package biz.home.assistActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import biz.home.R;

/**
 * Created by admin on 2015/12/28.
 */
public class SettingThanks extends Activity implements View.OnClickListener{
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_thanks);
        iv_back= (ImageView) findViewById(R.id.setting_thanks_back_img);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_thanks_back_img:
                this.finish();
                break;
            default:
                break;
        }
    }
}
