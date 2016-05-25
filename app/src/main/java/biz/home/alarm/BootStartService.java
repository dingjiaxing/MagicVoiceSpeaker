package biz.home.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import biz.home.bean.AlarmInfo;
import biz.home.util.AlarmInfoDao;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

/**
 * Created by admin on 2015/11/27.
 */
public class BootStartService extends Service {
    private Context context;
    private AlarmInfoDao dao;
    private String TAG="BootStartService";
    //提醒的频率
    public static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    public static final int WEEK = 1000 * 60 * 60 * 24*7;// 一周
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            context=this;
            JPushInterface.init(this);
//            startService(new Intent(this,PushService.class));
            Log.i(TAG, "onCreate 监听到 开机启动");
//            Toast.makeText(context,"magic -BootStartService剑听到了开机启动",Toast.LENGTH_SHORT).show();
            dao=new AlarmInfoDao(this);
            AlarmInfo[] alarms=dao.find();
            for(int i=0;i<alarms.length;i++){
                int repeat=alarms[i].getIsRepeat();
                String content=alarms[i].getContent();
                int id=alarms[i].getAlarmId();
                Date date2=new Date();
                date2.setTime(Long.valueOf(alarms[i].getTime()));
                String s=alarms[i].getDateTime();
                Toast.makeText(context,"magic -BootStartService repeat"+repeat+";content"+content+";time"+alarms[i].getTime(),Toast.LENGTH_SHORT).show();

                //提醒一次
                if(repeat==0){
                    Intent intent = new Intent(context, OneShotAlarm.class);
                    intent.putExtra("content",content);
                    PendingIntent sender = PendingIntent.getBroadcast(
                            context,Integer.valueOf(id) , intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                String s="2015-08-21 19:56:00";
//                s=date+" "+time;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    try {
                    date2=sdf.parse(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date2);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                    String timeMillis = String.valueOf(calendar.getTimeInMillis());
                    alarms[i]=new AlarmInfo(id,timeMillis,s,content,0);
                }
                //重复提醒
                else{
                    //每天都提醒，提醒频率为每天
                    if(repeat==INTERVAL){
                        Intent intent = new Intent(context, RepeatingAlarm.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context,
                                Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        // Schedule the alarm!
                        AlarmManager am = (AlarmManager) context
                                .getSystemService(Context.ALARM_SERVICE);

//                    String s="2015-08-21 19:56:00";
//                    s=date+" "+time;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        try {
//                        date2=sdf.parse(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date2);

                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                INTERVAL, sender);

                        String timeMillis = String.valueOf(calendar.getTimeInMillis());
                        alarms[i]=new AlarmInfo(id,timeMillis,s,content,INTERVAL);
                    }
                    //每周提醒一次
                    else if(repeat==WEEK){
                        Intent intent = new Intent(context, RepeatingAlarm.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context,
                                Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        // Schedule the alarm!
                        AlarmManager am = (AlarmManager) context
                                .getSystemService(Context.ALARM_SERVICE);
//                    String s="2015-08-21 19:56:00";
//                    s=date+" "+time;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        try {
//                        date2=sdf.parse(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date2);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                WEEK, sender);
                        String timeMillis = String.valueOf(calendar.getTimeInMillis());
                        alarms[i]=new AlarmInfo(id,timeMillis,s,content,WEEK);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mMediaPlayer=new MediaPlayer();
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
        */

    }
}
