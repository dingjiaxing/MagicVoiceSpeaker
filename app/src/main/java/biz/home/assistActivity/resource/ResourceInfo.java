package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import biz.home.R;
import biz.home.api.myComponent.Topbar;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2016/1/28.
 */
public class ResourceInfo extends Activity implements OnClickListener{
    Boolean collectResult=false;
    private static final  int UPDATESEEKBAR=1101;   //更新声音进度条
    private static final  int RESOURCECANCELCOLLECT=1102;     //收藏资源
    private static final  int RESOURCEADDCOLLECT=1103;     //收藏资源
    private static final  int RESOURCECALL=1104;    //打电话
    private static final String TAG ="ResourceInfo" ;
    final String mimeType = "text/html";    //页面类型
    final String encoding = "utf-8";    //编码方式
    private Topbar topbar;          //顶部标题栏
    private TextView tv_title,tv_person,tv_time,tv_play_time,tv_all_time,tv_browse_count;
    private WebView et_content;     //内容显示浏览器
    private Button btn_call,btn_collect;    //打电话和收藏按钮
    private SeekBar seekBar;    //进度条
    private MagicResultResourceInfo resourceInfo; //资源信息
    private ToggleButton iv_play;   //声音的播放按钮
    private MediaPlayer mediaPlayer;    //媒体播放器，用来播放音乐
    private Handler handler;
    private Context context;    //上下文对象
    private LinearLayout voiceLine;     //声音的线性布局
    private String userId;

    class DelayThread extends Thread{
        int milliseconds;
        public DelayThread(int i){
            milliseconds=i;
        }

        @Override
        public void run() {
            while (true){
                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.what=UPDATESEEKBAR;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.resource_info);
            findById();
            context=this;
            init();
            Intent intent=getIntent();
            resourceInfo= (MagicResultResourceInfo) intent.getSerializableExtra("resourceInfo");
            if(resourceInfo.getVoicePath()==null){
                voiceLine.setVisibility(View.GONE);
            }
            tv_title.setText(resourceInfo.getTitle());
            tv_person.setText(resourceInfo.getRealName());
            tv_time.setText(resourceInfo.getDateTime());
            tv_browse_count.setText(resourceInfo.getBrowseCount()+"");
            if(resourceInfo.isFavoritesStatus()){
                btn_collect.setText("取消收藏");
            }else {
                btn_collect.setText("收藏资源");
            }
            et_content.getSettings().setDefaultTextEncodingName(encoding);


            //et_content.loadData(fmtString(resourceInfo.getContent()),mimeType,encoding);
            et_content.loadDataWithBaseURL("",resourceInfo.getContent(),mimeType,encoding,"");
//        player=new Player(seekBar);
            mediaPlayer=new MediaPlayer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        mediaPlayer=MediaPlayer.create(context,Uri.parse(resourceInfo.getVoicePath()));
                        int time=mediaPlayer.getDuration();
                        tv_all_time.setText(getVoiceTimeTextfromMilliTime(time));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },100);

            setOnClick();
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    try {
                        switch (msg.what){
                            case UPDATESEEKBAR:
                                if(mediaPlayer!=null){
                                    seekBar.setProgress(seekBar.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration());
                                    int time=mediaPlayer.getCurrentPosition();
                                    //                    Log.i(TAG, "handleMessage: mediaPlayer的播放处位置"+time);
                                    //                    Log.i(TAG, "handleMessage: mediaPlayer.getDuration()"+mediaPlayer.getDuration());
                                    //                    Log.i(TAG, "handleMessage:mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration() ");
                                    tv_play_time.setText(getVoiceTimeTextfromMilliTime(time));
                                    if(tv_play_time.getText().toString()!="00:00"&&time==mediaPlayer.getDuration()){
                                        iv_play.setChecked(false);
                                    }

                                }
                                break;
                            case RESOURCECALL:
                                break;
                            case RESOURCECANCELCOLLECT:
                                if(collectResult){
                                    Toast.makeText(context,"取消成功",Toast.LENGTH_SHORT).show();
                                    btn_collect.setText("收藏资源");
                                }else{
                                    Toast.makeText(context,"取消失败",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case RESOURCEADDCOLLECT:
                                if(collectResult){
                                    Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                                    btn_collect.setText("取消收藏");
                                }else{
                                    Toast.makeText(context,"收藏失败",Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }

                    } catch (Exception e) {
    //                    e.printStackTrace();
                    }

                }
            };
            DelayThread delayThread=new DelayThread(500);
            delayThread.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(TAG, "onCompletion: 播放完成");
                    iv_play.setChecked(false);
                    tv_play_time.setText("00:00");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void init() {
        MagicUserInfoDao dao=new MagicUserInfoDao(context);
        userId=dao.findUid();
    }

    private void setOnClick() {
        btn_call.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        iv_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    iv_play.setBackground(getResources().getDrawable(R.drawable.resource_music_pause_icon));
                    try {
                        Log.i(TAG, "onCheckedChanged: 点击了播放按钮");
                        if(mediaPlayer==null){
                            Log.i(TAG, "onCheckedChanged: 重置音乐");
                            seekBar.setSecondaryProgress(20);
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(context, Uri.parse(resourceInfo.getVoicePath()));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }else{
                            Log.i(TAG, "onCheckedChanged: 继续播放");
                            mediaPlayer.start();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context,"音频不可用",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }else{
                    Log.i(TAG, "onCheckedChanged: 停止播放");
                    iv_play.setBackground(getResources().getDrawable(R.drawable.resource_music_play_icon));
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                    }else{
                        Toast.makeText(context,"音频不可用",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void findById() {
        topbar= (Topbar) findViewById(R.id.resource_title_bar);
        tv_title= (TextView) findViewById(R.id.resource_title_text);
        et_content= (WebView) findViewById(R.id.resource_info_webview);
        btn_call= (Button) findViewById(R.id.resource_button_call);
        seekBar= (SeekBar) findViewById(R.id.resource_seek_bar);
        tv_person= (TextView) findViewById(R.id.resource_info_person);
        tv_time= (TextView) findViewById(R.id.resource_info_time);
        iv_play= (ToggleButton) findViewById(R.id.resource_info_voice_play_icon);
        tv_play_time= (TextView) findViewById(R.id.resource_info_play_time);
        tv_all_time= (TextView) findViewById(R.id.resource_info_all_time);
        btn_collect= (Button) findViewById(R.id.resource_info_btn_collect);
        voiceLine= (LinearLayout) findViewById(R.id.resource_info_voice_line);
        tv_browse_count= (TextView) findViewById(R.id.resource_info_browse_count);
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resource_button_call:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MagicInfo info=new MagicInfo();
                        info.setUid(userId);
                        info.setPostId(resourceInfo.getPostId());
                        info.setTitle(resourceInfo.getTitle());
                        info.setPublisher(resourceInfo.getRealName());
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                        String s= HttpHelp.send(info);
                    }
                }).start();
                final Uri callUri = Uri. parse ( "tel:"+resourceInfo.getTelephone() );
                Intent in = new Intent (Intent.ACTION_CALL , callUri);
                startActivity(in);
                break;
            case R.id.resource_info_voice_play_icon:
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
////                        player.playUrl(resourceInfo.getVoicePath());
//                        player.mediaPlayer.start();
//                    }
//                }).start();
                break;
            case R.id.resource_info_btn_collect:
                if(btn_collect.getText().toString().equals("收藏资源")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MagicInfo info=new MagicInfo();
                            info.setUid(userId);
                            info.setPostId(resourceInfo.getPostId());
                            info.setTitle(resourceInfo.getTitle());
                            info.setPublisher(resourceInfo.getRealName());
                            info.setApi(MagicInfoApiEnum.ADDRESOURCEFAVORITES);
                            String s= HttpHelp.send(info);
                            if(!s.equals("")){
                                if(Boolean.valueOf(HttpHelp.getJsonString(s))){
                                    collectResult=true;
                                }else{
                                    collectResult=false;
                                }
                            }else {
                                collectResult=false;
                            }
                            Message msg=new Message();
                            msg.what=RESOURCEADDCOLLECT;
                            handler.sendMessage(msg);

                        }
                    }).start();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MagicInfo info=new MagicInfo();
                            info.setUid(userId);
                            info.setPostId(resourceInfo.getPostId());
                            info.setTitle(resourceInfo.getTitle());
                            info.setPublisher(resourceInfo.getRealName());
                            info.setApi(MagicInfoApiEnum.REMOVERESOURCEFAVORITES);
                            String s= HttpHelp.send(info);
                            if(!s.equals("")){
                                if(Boolean.valueOf(HttpHelp.getJsonString(s))){
                                    collectResult=true;
                                }else{
                                    collectResult=false;
                                }
                            }else {
                                collectResult=false;
                            }
                            Message msg=new Message();
                            msg.what=RESOURCECANCELCOLLECT;
                            handler.sendMessage(msg);

                        }
                    }).start();
                }

                break;
        }
    }
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener{
        int progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
//            this.progress = progress * player.mediaPlayer.getDuration()
//                    / seekBar.getMax();
            this.progress = progress * mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            mediaPlayer.seekTo(progress);
        }
    }
    public static String getVoiceTimeTextfromMilliTime(int time){
        String result="";
        int minute=time/1000/60;
        int second=time/1000%60;
        String m="",s="";
        if(minute<10){
            m=0+""+minute;
        }else {
            m=""+minute;
        }
        if(second<10){
            s=0+""+second;
        }else {
            s=""+second;
        }
        result=m+":"+s;
        return result;
    }



}
