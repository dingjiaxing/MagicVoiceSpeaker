package biz.home.util;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import biz.home.application.App;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by lmw on 2015/7/17.
 */
public class VoiceUtil {
    public final static String BACK_INFO="color";
    private MediaRecorder voiceRecorder;    //
    private MediaPlayer voicePlayer;
    private String path;
    private Handler handler;    //用来给主类传值

    private int time=0;

    //利用百度语音将传输过来的文本读出来
    public static void read(String text){

    }

    public VoiceUtil(Handler handler){
        this.handler=handler;
    }

    private final Handler mHandler = new Handler(); //用于自身的回调
    private Runnable mUpdateImgTimer = new Runnable() {     //用于自身的回调
        public void run() {
            updataImg();
        }
    };

    public boolean startSaveVoice(){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sDateFormat.format(new java.util.Date());
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            File fileDir=null;
            try {
                fileDir= new File(Environment.getExternalStorageDirectory()
                        +File.separator+"speaktoit"+File.separator+"temp");
                if (!fileDir.exists())
                    fileDir.mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }
            path=fileDir+File.separator+date+".mp3";
        }
        voiceRecorder=new MediaRecorder();
        voiceRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        voiceRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        voiceRecorder.setOutputFile(path);
        voiceRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            voiceRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(App.getContext(),"无法打开麦克风！",Toast.LENGTH_SHORT).show();
            return false;
        }
        voiceRecorder.start();
        updataImg();
        return true;
    }

    public boolean stopSaveVoice(){
        if(voiceRecorder!=null) {
            voiceRecorder.setOnErrorListener(null);
            voiceRecorder.setPreviewDisplay(null);
            try {
                voiceRecorder.stop();
            }catch(Exception e){
                voiceRecorder.reset();
                voiceRecorder.release();
                voiceRecorder = null;
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
                return false;
            }
            voiceRecorder.reset();
            voiceRecorder.release();
            voiceRecorder = null;
            time=0;
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            return true;
        }
        return false;
    }

    public boolean playVoice(){
        voicePlayer = new MediaPlayer();
        try{
            voicePlayer.setDataSource(path);
            voicePlayer.prepare();
            voicePlayer.start();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getPath(){
        return this.path;
    }

    private void updataImg() {
        if(voiceRecorder!=null) {
            Message msg;
            Bundle data;
            int radio = voiceRecorder.getMaxAmplitude() / 600;
            int db = 0;
            if (radio > 0)
                db = (int) (20 * Math.log10(radio));
            if(db==0){
                time++;
                if(time==20)
                {
                    Toast.makeText(App.getContext(),"声音录取完毕",Toast.LENGTH_SHORT).show();
                    stopSaveVoice();
                    return;
                }
            }
            switch (db / 4) {
                case 0:
                    System.out.println("DB:" + db);
                    msg = new Message();
                    data = new Bundle();
                    data.putInt(BACK_INFO, 1);
                    msg.setData(data);
                    msg.what = 1;
                    handler.sendMessage(msg);
                    break;
                default:
                    System.out.println("DB:" + db);
                    msg = new Message();
                    data = new Bundle();
                    data.putInt(BACK_INFO, 2 * db);
                    msg.setData(data);
                    msg.what = 1;
                    handler.sendMessage(msg);
                    time=0;
                    break;
            }
            mHandler.postDelayed(mUpdateImgTimer, 150);
        }
    }
}
