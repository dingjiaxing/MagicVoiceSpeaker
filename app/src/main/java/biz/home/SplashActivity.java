package biz.home;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import biz.home.R;
import biz.home.api.notification.NotificationBroadcastReceiver;
import biz.home.application.myUtils.ShakeListenerUtils;
import biz.home.assistActivity.GuideViewpagerActivity;
import biz.home.model.ContactInfo;
import biz.home.model.VoiceFileRecord;
import biz.home.util.ContactInfoDao;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.UserSettingDao;
import biz.home.util.VoiceFileDao;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by adg on 2015/7/20.
 */
public class SplashActivity extends Activity {
    private NotificationBroadcastReceiver mReceiver;
    private ShakeListenerUtils shakeUtils;
    private SensorManager mSensorManager; //定义sensor管理器, 注册监听器用
    UserSettingDao userSettingDao;
    private Context context;
    NotificationManager nm = null;
    private String uid="";
    MagicUserInfoDao userDao;
    //数据库中的声音文件上传记录
    List<VoiceFileRecord> vfrList;
    //用来操作数据库中声音文件存储记录的dao
    VoiceFileDao vfDao;
    //获取手机联系人用到的
    private String[] columns = { ContactsContract.Contacts._ID,// ���IDֵ
            ContactsContract.Contacts.DISPLAY_NAME,// �������
            ContactsContract.CommonDataKinds.Phone.NUMBER,// ��õ绰
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, };

    ContactInfoDao dao;
    private SharedPreferences preferences;

    private static final String TAG = "SplashActivity";
    /**
     * 最长等待时间
     */
    private int splashPeriod = 1000;
    /**
     * 最小等待时间
     */
    private int minWaitePeriod = 500;
    /**
     * 登录数据保存
     */
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /**
     * app 的版本号
     */
    public static  String appVersion="1.1.1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assets_splash);


        context=this;
        userSettingDao=new UserSettingDao(context);
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        dao=new ContactInfoDao(getApplicationContext());
        try{
//            if(userSettingDao.find(userDao.findTelephone()).getStateItem4()==1){
                shakeUtils = new ShakeListenerUtils(this);
                //获取传感器管理服务
                mSensorManager = (SensorManager) this
                        .getSystemService(Service.SENSOR_SERVICE);
                //加速度传感器
                mSensorManager.registerListener(shakeUtils,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                        //根据不同应用，需要的反应速率不同，具体根据实际情况设定
                        SensorManager.SENSOR_DELAY_FASTEST);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String id=JPushInterface.getRegistrationID(this);
        Log.i(TAG, "onCreate getRegistrationID:"+id);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (preferences.getBoolean("firststart", true)) {
                    editor = preferences.edit();
                    //����¼��־λ����Ϊfalse���´ε�¼ʱ������ʾ�״ε�¼����
                    editor.putBoolean("firststart", false);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, GuideViewpagerActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, SpeakToitActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();

                }

            }
        }, splashPeriod);

//        pref = PreferenceManager.getDefaultSharedPreferences(this);
//        new initTask().execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getQueryData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    if(userSettingDao.find(userDao.findTelephone()).getStateItem5()==1){
                        notification();
                        /*
                        String service = Context.NOTIFICATION_SERVICE;
                        nm = (NotificationManager) context.getSystemService(service);          //获得系统级服务，用于管理消息
                        Notification n = new Notification();                                        //定义一个消息类
                        n.icon = R.drawable.app_icon_small;                                         //设置图标
                        n.tickerText = "正在打开神奇小秘书";                                        // 设置消息
//                n.number=1;
                        n.flags=Notification.FLAG_ONGOING_EVENT;
//                n.when = System.currentTimeMillis();                             //设置时间
// Notification n1 =new Notification(icon,tickerText,when);    //也可以用这个构造创建
                        Intent intent = new Intent(context, SpeakToitActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0,intent, 0);       //消息触发后调用
                        n.setLatestEventInfo(context, "Magic神奇小秘书", "点击进入神奇小秘书",pi); //设置事件信息就是拉下标题后显示的内容
                        nm.notify(1, n);      //发送通知
                        */
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();
        userDao=new MagicUserInfoDao(this);
        if(userDao.getCount()>0){
            uid=userDao.findUid();
        }
        if(!uid.equals("")){
            //在数据库中查询是否有残留的amr文件未上传到服务器
            vfDao=new VoiceFileDao(this);
            int count =vfDao.getCount();
            if(count>0){
                vfrList=vfDao.find();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try{
                            if(null!=vfrList){
                                for(int i=0;i<vfrList.size();i++){
                                    String s= HttpHelp.uploadVoiceFile(uid,vfrList.get(i).getMessageId(),vfrList.get(i).getPath());
                                    Boolean b= Boolean.valueOf(HttpHelp.getJsonString(s));
                                    if(b){
                                        vfDao.delete(vfrList.get(i).getMessageId());
                                        File file1=new File(vfrList.get(i).getPath());
                                        file1.delete();
                                    }else{

                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    protected class initTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                //这里可以进行一些初始化任务，现在只是简单的沉睡三秒
//                Thread.sleep(splashPeriod);
                //初始化任务，将目前的手机中的联系人列表存到安卓本地数据库
                getQueryData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {

//            startActivity(new Intent(SplashActivity.this, SpeakToitActivity.class));
//            finish();
        }
    }
    //获取手机内存储的联系人列表
    private String getQueryData() {

        StringBuilder sb = new StringBuilder();// ���ڱ����ַ�
        ContentResolver resolver = getApplicationContext().getContentResolver();// ���ContentResolver����
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);// ��ѯ��¼
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(columns[0]);// ���IDֵ������
            int displayNameIndex = cursor.getColumnIndex(columns[1]);// �����������
            int id = cursor.getInt(idIndex);// ���id
            String displayName = cursor.getString(displayNameIndex);// 联系人姓名
//            displayName= String.valueOf(ContactInfoDao.getPinYin(displayName));
            Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, columns[3] + "=" + id, null, null);
            while (phone.moveToNext()) {
                int phoneNumberIndex = phone.getColumnIndex(columns[2]);// ��õ绰����
                String phoneNumber = phone.getString(phoneNumberIndex);// 联系人电话
                ContactInfo ci=new ContactInfo(phoneNumber,displayName);
                Log.i("", "getQueryData phoneNumber" + phoneNumber + ";name:" + displayName);
                if(dao.find(phoneNumber)==null){
                    try{
                        dao.add(ci);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        dao.update(ci);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                sb.append(displayName + ": " + phoneNumber + "\n");// �������
                System.out.println("CallTask:getQueryData():"+sb);
                Log.i(TAG, "getQueryData "+sb.toString());
            }
        }
        cursor.close();// �ر��α�
        return sb.toString();
    }

    @Override
    public void finish() {
        super.finish();
    }
    //和通知栏的快速打开app有关的函数
    private void notification() {
        unregeisterReceiver();
        intiReceiver();

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_fast_open);
        // remoteViews.setTextViewText(R.id.tv_up, "首都机场精品无线");
        // remoteViews.setTextViewText(R.id.tv_down, "已免费接入");

        Intent intent = new Intent(NotificationBroadcastReceiver.ACTION_BTN);
        intent.putExtra(NotificationBroadcastReceiver.INTENT_NAME, NotificationBroadcastReceiver.INTENT_BTN_LOGIN);
        //PendingIntent intentpi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intentpi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notificationVoiceImage, intentpi);

        Intent intent2 = new Intent();
        intent2.setClass(this, SpeakToitActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intentContent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setOngoing(false);
        builder.setAutoCancel(false);
        builder.setContent(remoteViews);
        builder.setTicker("正在使用神奇小秘书");
        builder.setSmallIcon(R.drawable.app_icon_small);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.contentIntent = intentContent;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void intiReceiver() {
        mReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationBroadcastReceiver.ACTION_BTN);
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
    }

    private void unregeisterReceiver() {
        if (mReceiver != null) {
            getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
