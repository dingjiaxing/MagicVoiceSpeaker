package biz.home;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.ToggleButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import biz.home.api.notification.NotificationBroadcastReceiver;
import biz.home.application.myUtils.ShakeListenerUtils;
import biz.home.model.UserInfo;
import biz.home.model.UserSetting;
import biz.home.util.DatabaseUtil;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.UserSettingDao;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmw on 2015/7/27.
 */
public class SettingCommunicateActivity extends Activity implements View.OnClickListener{
    public final static String ACTION_BTN = "com.example.notification.btn.login";

    public final static String INTENT_NAME = "btnid";

    public final static int INTENT_BTN_LOGIN = 1;

    NotificationBroadcastReceiver mReceiver;

    ShakeListenerUtils shakeUtils;
    SensorManager mSensorManager;
    private Context context;
    private ImageView back;
    private TextView title,complete;
    private List<ToggleButton> stateGroup=new ArrayList<>();
    private List<LinearLayout> layoutGroup=new ArrayList<>();

    private String telephone;
    MagicUserInfoDao dao2;
    UserSettingDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_communicate);
        context=this;
        shakeUtils = new ShakeListenerUtils(this);
        initView();
        initData();
    }

    /**
     * Initialize components of the view.
     */
    private void initView(){
        back=(ImageView)findViewById(R.id.setting_communicate_back_img);                    //“返回”按钮
        title=(TextView)findViewById(R.id.setting_communicate_title);                       //标题
        complete=(TextView)findViewById(R.id.setting_communicate_complete);                //“完成按钮”
        stateGroup.add((ToggleButton)findViewById(R.id.setting_communicate_check_item1));      //stateGroup中添加“会话模式”
        stateGroup.add((ToggleButton)findViewById(R.id.setting_communicate_check_item2));      //stateGroup中添加“开启应用即打开麦克风”
        stateGroup.add((ToggleButton)findViewById(R.id.setting_communicate_check_item3));      //stateGroup中添加“神奇秘书语音说话”
        stateGroup.add((ToggleButton) findViewById(R.id.setting_communicate_check_item4));      //stateGroup中添加“开启摇一摇”
        stateGroup.add((ToggleButton) findViewById(R.id.setting_communicate_check_item5));      //stateGroup中添加“从状态上开启”
        stateGroup.add((ToggleButton) findViewById(R.id.setting_communicate_check_item6));      //stateGroup中添加“漂浮小工具”
        layoutGroup.add((LinearLayout)findViewById(R.id.setting_communicate_item1));         //layoutGroup中添加“会话模式”
        layoutGroup.add((LinearLayout)findViewById(R.id.setting_communicate_item2));         //layoutGroup中添加“开启应用即打开麦克风”
        layoutGroup.add((LinearLayout)findViewById(R.id.setting_communicate_item3));         //layoutGroup中添加“神奇秘书语音说话”
        layoutGroup.add((LinearLayout) findViewById(R.id.setting_communicate_item4));         //layoutGroup中添加“开启摇一摇”
        layoutGroup.add((LinearLayout) findViewById(R.id.setting_communicate_item5));         //layoutGroup中添加“从状态上开启”
        layoutGroup.add((LinearLayout) findViewById(R.id.setting_communicate_item6));         //layoutGroup中添加“漂浮小工具”
        complete.setOnClickListener(this);                         //“完成”按钮设置监听
        back.setOnClickListener(this);                              //“返回”按钮设置监听
    }

    /**
     * Initialize dates of components
     */
    private void initData(){
        dao2=new MagicUserInfoDao(getApplicationContext());
        dao=new UserSettingDao(getApplicationContext());
        telephone=dao2.findTelephone();
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
        //Complete Button will be visible after change the state
//        try{
//            dao.createUserSetting();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        try{
//            dao.deleteAll();
            if(dao.getCount()==0){
                UserSetting setting=new UserSetting(dao2.findTelephone(),0,0,1,0,0,0);
                dao.add(setting);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(dao.getCount()!=0){
            setInfo(dao.find(telephone));
        }
        for(int i=0;i<stateGroup.size();i++){
            stateGroup.get(i).setOnClickListener(this);
        }
    }

    /**
     * Getting state form UserInfo
     */
    private int getState(UserSetting userInfo,int i){
        int state=0;
        switch(i){
            case 0:
                state=userInfo.getStateItem1();
                break;
            case 1:
                state=userInfo.getStateItem2();
                break;
            case 2:
                state=userInfo.getStateItem3();
                break;
            case 3:
                state=userInfo.getStateItem4();
                /*
                try{
                    if(state==0){
                        mSensorManager.unregisterListener(shakeUtils);
                    }else {
                        //获取传感器管理服务
                        mSensorManager = (SensorManager) this
                                .getSystemService(Service.SENSOR_SERVICE);
                        //加速度传感器
                        mSensorManager.registerListener(shakeUtils,
                                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                                //根据不同应用，需要的反应速率不同，具体根据实际情况设定
                                SensorManager.SENSOR_DELAY_NORMAL);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                */
                break;
            case 4:
                state=userInfo.getStateItem5();
                NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);          //获得系统级服务，用于管理消息
                if(state==0){
                    nm.cancelAll();
                }else{
                    notification();
                    /*
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
                break;
            case 5:
                state=userInfo.getStateItem6();
                break;
        }
        return state;
    }

    /**
     * 设置状态
     * Setting state to UserInfo
     */
    private void setState(UserSetting userInfo,int i,int state){
//        ContentValues values=new ContentValues();
//        values.put("stateitem"+(i+1),state);
//        userInfo.setStateItem1(state);
//        DataSupport.updateAll(UserInfo.class, values, "tel=?", telephone);
        int num=i+1;
        switch (num){
            case 1:
                userInfo.setStateItem1(state);
                break;
            case 2:
                userInfo.setStateItem2(state);
                break;
            case 3:
                userInfo.setStateItem3(state);
                break;
            case 4:
                userInfo.setStateItem4(state);
                break;
            case 5:
                userInfo.setStateItem5(state);
                break;
            case 6:
                userInfo.setStateItem6(state);
                break;
        }
        dao.update(userInfo);
    }

    /**
     * 初始化
     * Initialize state of different functions.
     */
    private void setInfo(UserSetting userInfo){
        for(int i=0;i<stateGroup.size();i++){
            int state=getState(userInfo, i);
            ToggleButton checkBox;
            //The name of resource
            String rsName="";
            //The Id of resource
            int btId=0;
            switch (state){
                case 0:
                    rsName="setting_communicate_check_item"+(i+1);
                    btId=getResources().getIdentifier(rsName,"id",getPackageName());
                    checkBox=(ToggleButton)findViewById(btId);
                    checkBox.setChecked(false);
                    break;
                case 1:
                    rsName="setting_communicate_check_item"+(i+1);
                    btId=getResources().getIdentifier(rsName,"id",getPackageName());
                    checkBox=(ToggleButton)findViewById(btId);
                    checkBox.setChecked(true);
                    break;
            }
        }
        complete.setVisibility(View.INVISIBLE);
    }

    /**
     * 改变用户的状态信息
     * Change the states of UserInfo
     */
    private void changeInfo(UserSetting userInfo){

            for(int i=0;i<stateGroup.size();i++){
                ToggleButton cb=stateGroup.get(i);
                if(!cb.isChecked())
                    setState(userInfo,i,0);
                else
                    setState(userInfo,i,1);
            }
            complete.setVisibility(View.INVISIBLE);
    }

    /**
     * 监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_communicate_back_img:
                finish();
                break;
            case R.id.setting_communicate_complete:
                changeInfo(dao.find(telephone));
                setInfo(dao.find(telephone));
                //判断用户设置中是否为会话模式，如果是，则将SpeakToIt中的标志isHuihua改为true
                ToggleButton cb0=stateGroup.get(0);
                if(cb0.isChecked()){
                    SpeakToitActivity.isHuihua=true;
                }else{
                    SpeakToitActivity.isHuihua=false;
                }

//                UserSetting s=dao.find(dao2.findTelephone());
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_communicate_check_item1:
                changeToggleButton(stateGroup.get(0));
                break;
            case R.id.setting_communicate_check_item2:
                changeToggleButton(stateGroup.get(1));
                break;
            case R.id.setting_communicate_check_item3:
                changeToggleButton(stateGroup.get(2));
                break;
            case R.id.setting_communicate_check_item4:
                changeToggleButton(stateGroup.get(3));
                break;
            case R.id.setting_communicate_check_item5:
                changeToggleButton(stateGroup.get(4));
                break;
            case R.id.setting_communicate_check_item6:
                changeToggleButton(stateGroup.get(5));
                break;
        }
    }
    //改变ToggleButton状态
    private void changeToggleButton(ToggleButton checkBox){
//        checkBox.setChecked(!checkBox.isChecked());
        complete.setVisibility(View.VISIBLE);
    }
    //和在通知栏显示有关的函数
//    class NotificationBroadcastReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ACTION_BTN)) {
//                int btn_id = intent.getIntExtra(INTENT_NAME, 0);
//                switch (btn_id) {
//                    case INTENT_BTN_LOGIN:
//                        //Toast.makeText(MainActivity.this, "从通知栏点登录", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(SettingCommunicateActivity.this,SpeakToitActivity.class));
//                        unregeisterReceiver();
//                        NotificationManager notificationManager = (NotificationManager) SettingCommunicateActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.cancel(0);
//                        break;
//                }
//            }
//        }
//    }

    private void notification() {
        unregeisterReceiver();
        intiReceiver();

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_fast_open);
       // remoteViews.setTextViewText(R.id.tv_up, "首都机场精品无线");
       // remoteViews.setTextViewText(R.id.tv_down, "已免费接入");

        Intent intent = new Intent(ACTION_BTN);
        intent.putExtra(INTENT_NAME, INTENT_BTN_LOGIN);
       // PendingIntent intentpi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intentpi = PendingIntent.getBroadcast(this, 0, intent, 0);
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
        intentFilter.addAction(ACTION_BTN);
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
    }

    private void unregeisterReceiver() {
        if (mReceiver != null) {
            getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
