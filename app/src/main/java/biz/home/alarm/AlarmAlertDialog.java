package biz.home.alarm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import biz.home.R;

/**
 * Created by admin on 2015/8/21.
 */
public class AlarmAlertDialog extends Activity implements View.OnClickListener {
    private Button button;
    private String TAG="AlertDialog";
    MediaPlayer mMediaPlayer;
    private TextView content2;
    Vibrate vi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.setCancelable(false);// 设置点击屏幕Dialog不消失
        AlarmAlertDialog.this.setFinishOnTouchOutside(false);
        Log.i(TAG, "onCreate ：进入alertDialog提醒框");
        setContentView(R.layout.alert_dialog);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        Log.i(TAG, "onCreate content:"+content);
        findById();
        setOnClick();
        if(content==null){
            content="您叫我提醒您的！";
        }
        content2.setText(content);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
// 如果为空，才构造，不为空，说明之前有构造过
        mMediaPlayer=new MediaPlayer();
        if(mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setLooping(false); //循环播放
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        vi=new Vibrate(getApplicationContext());
        vi.playVibrate(-1);


    }
    public void findById(){

        button=(Button)findViewById(R.id.close_btn);
        content2=(TextView)findViewById(R.id.content2);

    }
    public void setOnClick(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                vi.Stop();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
