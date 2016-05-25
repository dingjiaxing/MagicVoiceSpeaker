package biz.home.yibuTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.alarm.OneShotAlarm;
import biz.home.alarm.RepeatingAlarm;
import biz.home.bean.AlarmInfo;
import biz.home.bean.MagicResult;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.model.ContactInfo;
import biz.home.model.MagicResultFunctionalEnum;
import biz.home.util.AlarmInfoDao;
import biz.home.util.ContactInfoDao;

/**
 * Created by admin on 2015/8/22.
 */
public class ScheduleTask extends AsyncTask {
    private AlarmInfoDao alarmInfoDao;
    private AlarmInfo alarmInfo;
    private  String endResult;
    private  FragmentAdapter mFragmentAdapter;
    private  List<Fragment> mFragmentList;
    SpeechSynthesizer speechSynthesizer;
    ViewPager mViewPager;
    private final int MaxFragmentSize = 10;
    Context context;
    MagicResult magicResult;
    MainEditFragment mainEditFragment;
    MainWebFragment mainWebFragment;
    ContactInfoDao dao;
    String TAG="MessageTask-Class";
    public static int size;                   //一个姓名对应了size个电话号码
    ContactInfo[] cin;
    //提醒的频率
    public static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    public static final int WEEK = 1000 * 60 * 60 * 24*7;// 一周

    public ScheduleTask(String endResult, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, SpeechSynthesizer speechSynthesizer, ViewPager mViewPager, Context context, MagicResult magicResult, MainEditFragment mainEditFragment, MainWebFragment mainWebFragment, ContactInfoDao dao, String TAG, ContactInfo[] cin) {
        super();
        this.endResult = endResult;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.speechSynthesizer = speechSynthesizer;
        this.mViewPager = mViewPager;
        this.context = context;
        this.magicResult = magicResult;
        this.mainEditFragment = mainEditFragment;
        this.mainWebFragment = mainWebFragment;
        this.dao = dao;
        this.TAG = TAG;
        this.cin = cin;
    }

    public ScheduleTask(Context context, MagicResult magicResult, MainWebFragment mainWebFragment, SpeechSynthesizer speechSynthesizer, String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {

        super();
        this.endResult = endResult;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.speechSynthesizer = speechSynthesizer;
        this.mViewPager = mViewPager;
        this.context = context;
        this.magicResult = magicResult;
        this.mainEditFragment = mainEditFragment;
        this.mainWebFragment = mainWebFragment;
        this.dao = dao;
        this.TAG = TAG;
        this.cin = cin;
        alarmInfoDao=new AlarmInfoDao(context);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try{
            String question=magicResult.getQuestion().trim();
            String content="提醒创建成功";
            if(question.equals(endResult.trim())){
                System.out.println("根据回答改变UI");
                mainEditFragment.topShow.setText(content);
                Bundle bundle = new Bundle();
                bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
                bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
                bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
                bundle.putString(MainEditFragment.TITLE, "");
                bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                mFragmentList.get(mFragmentList.size()-1).getArguments().putAll(bundle);
            }else{
                mainEditFragment = MainEditFragment.newInstance(null,content,question,MainEditFragment.BUTTON_DEFAULT);
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
            }
            SpeakToitActivity.MIROPHONE_STATE=3;
        }catch (Exception e){
            e.printStackTrace();
            String error="服务器错误，请稍后重试！";
            mainEditFragment = MainEditFragment.newInstance(null,error,"",MainEditFragment.BUTTON_DEFAULT);
            addFragment2List(mainEditFragment);
            mFragmentAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mFragmentList.size() - 1);
            SpeakToitActivity.MIROPHONE_STATE=3;
            speechSynthesizer.speak(error);
        }

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String type=magicResult.getResultFunctional().getSchedule().getName();
            String date=magicResult.getResultFunctional().getSchedule().getDatetime().getDate();
            String time=magicResult.getResultFunctional().getSchedule().getDatetime().getTime();
            Date date2=new Date();
            Random ran=new Random();
            int id=ran.nextInt(100000);
        /*
        //提醒
        if(type.equals("reminder")){
            String content=magicResult.getResultFunctional().getSchedule().getContent();
            Intent intent = new Intent(context, OneShotAlarm.class);
            intent.putExtra("content",content);
            PendingIntent sender = PendingIntent.getBroadcast(
                    context, Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            String s="2015-08-22 14:08:00";
            s=date+" "+time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                date2=sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date2);
//            calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.add(Calendar.SECOND, 30);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            String timeMillis = String.valueOf(calendar.getTimeInMillis());
            alarmInfo=new AlarmInfo(id,timeMillis,s,content,0);
        }
        */
            //闹钟
            String content=magicResult.getResultFunctional().getSchedule().getContent();
            if(type.equals("clock")||type.equals("reminder")){
                String repeat=magicResult.getResultFunctional().getSchedule().getRepeat();
                //提醒一次
                if(repeat==null){
                    Intent intent = new Intent(context, OneShotAlarm.class);
                    intent.putExtra("content",content);
                    PendingIntent sender = PendingIntent.getBroadcast(
                            context,Integer.valueOf(id) , intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    String s="2015-08-21 19:56:00";
                    s=date+" "+time;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    try {
                        date2=sdf.parse(s);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date2);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                    String timeMillis = String.valueOf(calendar.getTimeInMillis());
                    alarmInfo=new AlarmInfo(id,timeMillis,s,content,0);
                }
                //重复提醒
                else{
                    //每天都提醒，提醒频率为每天
                    if(repeat.equals("EVERYDAY")){
                        Intent intent = new Intent(context, RepeatingAlarm.class);
                        intent.putExtra("content",content);
                        PendingIntent sender = PendingIntent.getBroadcast(context,
                                Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        // Schedule the alarm!
                        AlarmManager am = (AlarmManager) context
                                .getSystemService(Context.ALARM_SERVICE);

                        String s="2015-08-21 19:56:00";
                        s=date+" "+time;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        try {
                            date2=sdf.parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date2);

                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                INTERVAL, sender);

                        String timeMillis = String.valueOf(calendar.getTimeInMillis());
                        alarmInfo=new AlarmInfo(id,timeMillis,s,content,INTERVAL);
                    }
                    //每周提醒一次
                    else{
                        Intent intent = new Intent(context, RepeatingAlarm.class);
                        intent.putExtra("content",content);
                        PendingIntent sender = PendingIntent.getBroadcast(context,
                                Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        // Schedule the alarm!
                        AlarmManager am = (AlarmManager) context
                                .getSystemService(Context.ALARM_SERVICE);
                        String s="2015-08-21 19:56:00";
                        s=date+" "+time;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        try {
                            date2=sdf.parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date2);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                WEEK, sender);
                        String timeMillis = String.valueOf(calendar.getTimeInMillis());
                        alarmInfo=new AlarmInfo(id,timeMillis,s,content,WEEK);
                    }

                }

            }
            speechSynthesizer.speak("提醒创建成功，说“查看日程即可查看您的日程信息”");
            alarmInfoDao.add(alarmInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private List<Fragment> addFragment2List(Fragment fragment) {
        int mSize = mFragmentList.size();
        if (mSize >= MaxFragmentSize) {
            mFragmentList.remove(0);
            return addFragment2List(fragment);
        }
        mFragmentList.add(fragment);
        return mFragmentList;
    }

}
