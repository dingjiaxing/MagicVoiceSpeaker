package biz.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.adapter.DeletableAdapter;
import biz.home.bean.AlarmInfo;
import biz.home.yibuTask.ScheduleTask;

/**
 * Created by admin on 2015/10/8.
 */
public class MainAlarmsFragment extends Fragment {
    private int size;
    private DeletableAdapter adapter;
    private ListView listView;
    AlarmInfo[] alarminfos;
    public static AlarmInfo[] alarminfos2;
    private ArrayList<String> text,text2;
    @SuppressLint("ValidFragment")
    public MainAlarmsFragment(AlarmInfo[] alarminfos,int size) {
        super();
        this.alarminfos = alarminfos;
        this.size=size;
    }
    public MainAlarmsFragment() {
//        super();
    }



//    public static MainAlarmsFragment newInstance(AlarmInfo[] alarminfoss,int size) {
//        alarminfos2 = alarminfoss;
//
//        Bundle args = new Bundle();
//        MainAlarmsFragment fragment = new MainAla
// rmsFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        alarminfos=alarminfos2;
        View view = inflater.inflate(R.layout.alarms_list, container, false);
        listView= (ListView) view.findViewById(R.id.alarms_list);
        text=new ArrayList<String>();
        text2=new ArrayList<String>();
//        int size=alarminfos.length;
        System.out.println("size:" + size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        int dayForWeek = 0;
        Date date;
        String dateText="";
        String timeText="";
        String xWeek="";
        int circle=0;
        for(int i=0;i<size;i++){
            System.out.println("日程管理-MainAlarmsFragment i:" + i);
            String frequency="";        //描述响铃平率的字符串（一次、每天、每周）

            String dateTime=alarminfos[i].getDateTime();
            try {
                date = sdf.parse(dateTime);
                Calendar cal1= Calendar.getInstance();
                Calendar cal2= Calendar.getInstance();
                cal2.setTime(date);
                xWeek=dayOfWeek(cal2);
                circle=alarminfos[i].getIsRepeat();     //响铃周期
                System.out.println("circle:" + circle);
                if(circle==0){
                    frequency="";
                }else if(circle== ScheduleTask.INTERVAL){
                    frequency="(每天)";
                }else if(circle==ScheduleTask.WEEK){
                    frequency="(每周"+xWeek+")";
                }

                if(isSameWeek(cal1,cal2)){
                    if(isSameDay(cal1,cal2)){
                        dateText="今天";
                    }else if(isNextDay(cal1,cal2)){
                        dateText="明 天";
                    }else if(isNextNextDay(cal1,cal2)){
                        dateText="后天";
                    }else{
                        dateText="本周"+xWeek;
                    }
                }else{
                    if (isNextWeek(cal1,cal2)){
                        dateText="下周"+xWeek;
                    }else{
                        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                        dateText=df.format(date);
                    }
                }
                SimpleDateFormat df= new SimpleDateFormat("HH:mm", Locale.CHINA);
                timeText=df.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateText=circle==0?dateText:"";
            text.add(dateText+" "+timeText +frequency+ " | " + alarminfos[i].getContent());
            text2.add(String.valueOf(alarminfos[i].getAlarmId()));
        }
        adapter=new DeletableAdapter(getActivity(),text,text2);
        listView.setAdapter(adapter);

        return view;
    }
    public static boolean isSameDay(Calendar cal1,Calendar cal2){
//        int subYear = cal1.get(Calendar.YEAR)-cal2.get(Calendar.YEAR);
        int year1=cal1.get(Calendar.YEAR);//得到年
        int month1=cal1.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
        int day1=cal1.get(Calendar.DAY_OF_MONTH);//得到天

        int year2=cal2.get(Calendar.YEAR);//得到年
        int month2=cal2.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
        int day2=cal2.get(Calendar.DAY_OF_MONTH);//得到天
        if(year1==year2 && month1==month2 && day1==day2){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isNextDay(Calendar cal1,Calendar cal2){
        cal1.add(Calendar.DATE,1);
        if(isSameDay(cal1,cal2)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isNextNextDay(Calendar cal1,Calendar cal2){
        cal1.add(Calendar.DATE,2);
        if(isSameDay(cal1,cal2)){
            return true;
        }else{
            return false;
        }
    }

    public static String dayOfWeek(Calendar calendar){
        String s="";
        int n=0;
        if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            n = 7;
        }else{
            n = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        switch (n){
            case 1:
                s="一";
                break;
            case 2:
                s="二";
                break;
            case 3:
                s="三";
                break;
            case 4:
                s="四";
                break;
            case 5:
                s="五";
                break;
            case 6:
                s="六";
                break;
            case 7:
                s="日";
                break;
        }
        return s;
    }
    public static boolean isSameWeek(Calendar cal1,Calendar cal2) {
        int subYear = cal1.get(Calendar.YEAR)-cal2.get(Calendar.YEAR);
        //subYear==0,说明是同一年
        if(subYear == 0)
        {
            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        //例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        //java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        //大家可以查一下自己的日历
        //处理的比较好
        //说明:java的一月用"0"标识，那么12月用"11"
        else if(subYear==1 && cal2.get(Calendar.MONTH)==11)
        {
            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        //例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if(subYear==-1 && cal1.get(Calendar.MONTH)==11)
        {
            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }
    public static boolean isNextWeek(Calendar cal1,Calendar cal2) {
        int subYear = cal2.get(Calendar.YEAR)-cal1.get(Calendar.YEAR);
        //subYear==0,说明是同一年
        if(subYear == 0)
        {
            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)-1)
                return true;
        }
        //例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        //java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        //大家可以查一下自己的日历
        //处理的比较好
        //说明:java的一月用"0"标识，那么12月用"11"
        else if(subYear==1 && cal1.get(Calendar.MONTH)==11)
        {
            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)-1)
                return true;
        }
        //例子:cal1是"2004-12-31"，cal2是"2005-1-1"
//        else if(subYear==-1 && cal1.get(Calendar.MONTH)==11)
//        {
//            if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        }
        return false;
    }

}
