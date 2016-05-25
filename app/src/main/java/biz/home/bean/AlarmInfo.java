package biz.home.bean;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2015/10/8.
 */
public class AlarmInfo {
    private int _id;        //自增长的id
    private int alarmId;    //标识不同闹钟的id
    private String time;    //提醒的时间,时间的毫秒值
    private int dayOfWeek; //   提醒 是 在 周几  数字 1  2  3  4  5  6  7
    private int hour;       //提醒时间 的  小时 0-23
    private int minute;     //提醒时间 的 分钟 0-59
    private String dateTime;    //提醒时间的字符串，格式为 2015-08-21 19:56:00
    private String content;     //提醒的内容
    private int isRepeat;      //是否重复提醒，0时为不重复，非0时数值即为提醒的周期的毫秒值

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    public AlarmInfo() {
    }

    public AlarmInfo( int alarmId, String time,String dateTime, String content, int isRepeat) {
        this.alarmId = alarmId;
        this.time = time;
        this.dateTime=dateTime;
        this.content = content;
        this.isRepeat = isRepeat;

        Date date=new Date(Long.parseLong(time));
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        if(dayOfWeek==0){
            dayOfWeek=7;
        }
        this.dayOfWeek=dayOfWeek;
        this.hour=hour;
        this.minute=minute;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(int isRepeat) {
        this.isRepeat = isRepeat;
    }

    @Override
    public String toString() {
        return _id+","+alarmId+","+time+","+content+","+isRepeat;
    }
}
