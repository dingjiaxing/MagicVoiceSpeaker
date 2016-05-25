package biz.home.assistActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import biz.home.SpeakToitActivity;
import biz.home.api.jpush.MyJPushReceiver;
import biz.home.bean.ArtificialAnswer;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicResultEnum;
import biz.home.model.MagicUserInfo;
import biz.home.util.DeviceIdUtil;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2015/10/30.
 */
public class PollingService extends Service {
    public static Boolean stopPollingFlag=false;
    private static final String TAG ="PollingService";
    Thread pollingThread;
    MagicUserInfoDao dao;

    MagicInfo info;

    public static int timeDelay=1000*8;

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            context=this;
            dao=new MagicUserInfoDao(this);
            final String uid=dao.findUid();
            Log.i(TAG, "onCreate uid:"+uid);
//        uid="64FF30BDD77F48D6B51FB517B20BFD89";
            info=new MagicInfo();
            info.setUid(uid);


            info.setApi(MagicInfoApiEnum.DIALOGMESSAGE);
            //获取轮询频率，即相邻两次访问之间的时间间隔
//            SharedPreferences sp=getSharedPreferences("frequency", MODE_PRIVATE);
            SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(PollingService.this);
            timeDelay=sp.getInt("frequency",5*1000);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    MagicInfo info=new MagicInfo();
                    info.setApi(MagicInfoApiEnum.FREQUENCY);
                    info.setDeviceId(DeviceIdUtil.comDeviceId(getApplicationContext()));
                    String str=HttpHelp.send(info);
                    Log.i(TAG, "run str:"+str);
                    if(!"null".equals(HttpHelp.getJsonString(str))){
                        String s=HttpHelp.getJsonString(str);
                        try {
                            timeDelay=Integer.valueOf(s);
                            SharedPreferences sp=getSharedPreferences("frequency", MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putInt("frequency", timeDelay);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            pollingThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        Log.i(TAG, "run stopPollingFlag:"+stopPollingFlag);
                        Log.i(TAG, "run SpeakToitActivity.isForeground:"+SpeakToitActivity.isForeground);
                        try {
                            Thread.sleep(timeDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(stopPollingFlag || (!SpeakToitActivity.isForeground)){
//                            stopPollingFlag=false;
                            continue;
                        }
                        Log.i(TAG, "run timeDelay:"+timeDelay);
                        info.setApi(MagicInfoApiEnum.DIALOGMESSAGE);
                        info.setDeviceId(DeviceIdUtil.comDeviceId(getApplicationContext()));
                        if(info.getUid()==null){
                            String uid=dao.findUid();
                            info.setUid(uid);
                        }
                        String s= HttpHelp.send(info);
                        System.out.println("" + s);
//                        {"json":""}   {"json":""}
                        if(!s.trim().toString().equals("{\"json\":\"\"}")){
                            List<MagicResult> list=HttpHelp.transferList(s);
                            MagicResult mr=new MagicResult();
                            if(list!=null){
                                for(int i=0;i<list.size();i++){
                                    mr=list.get(i);
//                                    Log.i(TAG, "run 第"+i+"次循环拿到的结果："+mr.getResultText().getContent());
                                    Log.i(TAG, "run 轮询拿到的结果："+s);
//                                    timeDelay=mr.getFrequency();
                                    Log.i(TAG, "run timeDelay:"+timeDelay);
                                    ArtificialAnswer aa=null;
                                    if(mr.getType().equals(MagicResultEnum.TEXT)){
                                        aa=new ArtificialAnswer("text",mr.getQuestion(),mr.getResultText().getContent(),"");
                                        aa.setMessageId(mr.getMessageId());
                                    }else if(mr.getType().equals(MagicResultEnum.URL)){
                                        aa=new ArtificialAnswer("url",mr.getQuestion()==null?"":mr.getQuestion(),mr.getResultUrl().getContent(),mr.getResultUrl().getUrl());
                                        aa.setMessageId(mr.getMessageId());
                                    }
                                    if(aa!=null){
                                        SpeakToitActivity.artificialAnswerList.add(aa);
                                    }
                                }
                            }
                        }

                    }
                }
            });
            pollingThread.start();
        }catch (Exception e){
            e.printStackTrace();
            pollingThread.start();
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    public class MyBinder extends Binder {
        public PollingService getService(){
            return PollingService.this;
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void startPolling(){
        try{
//            if(!pollingThread.isAlive()) {
            Log.i(TAG, "startPolling 开启轮询");
            stopPollingFlag=false;



//            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void stopPolling(){
        try{
//            if(pollingThread.isAlive()){
//                pollingThread.stop();
//            }
            stopPollingFlag=true;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
