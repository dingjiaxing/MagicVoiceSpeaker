package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import biz.home.bean.AlarmInfo;
import biz.home.yibuTask.ScheduleTask;

/**
 * Created by admin on 2015/10/8.
 */
public class AlarmInfoDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public AlarmInfoDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public int getCount(){
        int count=0;
        try{
            find();
            db=helper.getWritableDatabase();
            Cursor cursor=db.rawQuery("select count(_id) from tb_alarmInfo",null);
            if (cursor.moveToNext()){
                count= cursor.getInt(0);
            }
            cursor.close();
            return  count;
        }catch (Exception e){
            e.printStackTrace();
            return  0;
        }
    }
    public void add(AlarmInfo alarmInfo){
        db=helper.getWritableDatabase();
//        int _id=alarmInfo.get_id();
        int alarmId=alarmInfo.getAlarmId();
        String time=alarmInfo.getTime();
        int dayOfWeek=alarmInfo.getDayOfWeek();
        int hour=alarmInfo.getHour();
        int minute=alarmInfo.getMinute();
        String dateTime=alarmInfo.getDateTime();
        String content=alarmInfo.getContent();
        int isRepeat=alarmInfo.getIsRepeat();
        db.execSQL("insert into tb_alarmInfo(alarmId,time,dayOfWeek,hour,minute,dateTime,content,isRepeat) values(?,?,?,?,?,?,?,?)",
                new Object[]{alarmId, time, dayOfWeek, hour, minute, dateTime, content, isRepeat});
    }
    public void delete(int alarmId){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_alarmInfo where alarmId='" + alarmId + "'");
    }
    public AlarmInfo[] find(){
        AlarmInfo[] alarmInfos=new AlarmInfo[20];
        db=helper.getWritableDatabase();
        int i=0;
        Cursor cursor=db.rawQuery("select _id,alarmId,time ,dayOfWeek,hour,minute,dateTime,content,isRepeat  from tb_alarmInfo where isRepeat='0' order by isRepeat asc,time asc ",null);
        while(cursor.moveToNext()){
            String time=cursor.getString(2);
            int isRepeat=cursor.getInt(8);
            int alarmId=cursor.getInt(1);
            if(isRepeat==0 && System.currentTimeMillis()>Long.parseLong(time)){
                delete(alarmId);        //当该日程仅提醒一次，并且已过期时，在数据库中删除该闹钟
            }else{
                alarmInfos[i]=new AlarmInfo();
                alarmInfos[i].set_id(cursor.getInt(0));
                alarmInfos[i].setAlarmId(cursor.getInt(1));
                alarmInfos[i].setTime(cursor.getString(2));
                alarmInfos[i].setDayOfWeek(cursor.getInt(3));
                alarmInfos[i].setHour(cursor.getInt(4));
                alarmInfos[i].setMinute(cursor.getInt(5));
                alarmInfos[i].setDateTime(cursor.getString(6));
                alarmInfos[i].setContent(cursor.getString(7));
                alarmInfos[i].setIsRepeat(cursor.getInt(8));
                i++;
            }
        }
        //每天的提醒
        cursor=db.rawQuery("select _id,alarmId,time ,dayOfWeek,hour,minute,dateTime,content,isRepeat  from tb_alarmInfo  where isRepeat='"+ ScheduleTask.INTERVAL+"' order by hour asc,minute asc",null);
        while(cursor.moveToNext()){
            alarmInfos[i]=new AlarmInfo();
            alarmInfos[i].set_id(cursor.getInt(0));
            alarmInfos[i].setAlarmId(cursor.getInt(1));
            alarmInfos[i].setTime(cursor.getString(2));
            alarmInfos[i].setDayOfWeek(cursor.getInt(3));
            alarmInfos[i].setHour(cursor.getInt(4));
            alarmInfos[i].setMinute(cursor.getInt(5));
            alarmInfos[i].setDateTime(cursor.getString(6));
            alarmInfos[i].setContent(cursor.getString(7));
            alarmInfos[i].setIsRepeat(cursor.getInt(8));
            i++;
        }
        //每周的提醒
        cursor=db.rawQuery("select _id,alarmId,time ,dayOfWeek,hour,minute,dateTime,content,isRepeat  from tb_alarmInfo  where isRepeat='"+ScheduleTask.WEEK+"' order by dayOfWeek asc,hour asc,minute asc",null);
        while(cursor.moveToNext()){
            alarmInfos[i]=new AlarmInfo();
            alarmInfos[i].set_id(cursor.getInt(0));
            alarmInfos[i].setAlarmId(cursor.getInt(1));
            alarmInfos[i].setTime(cursor.getString(2));
            alarmInfos[i].setDayOfWeek(cursor.getInt(3));
            alarmInfos[i].setHour(cursor.getInt(4));
            alarmInfos[i].setMinute(cursor.getInt(5));
            alarmInfos[i].setDateTime(cursor.getString(6));
            alarmInfos[i].setContent(cursor.getString(7));
            alarmInfos[i].setIsRepeat(cursor.getInt(8));
            i++;
        }
        cursor.close();
        return alarmInfos;
    }
    public AlarmInfo find(int alarmId0){
        AlarmInfo alarmInfo=new AlarmInfo();
        db=helper.getWritableDatabase();
        int i=0;
        Cursor cursor=db.rawQuery("select _id,alarmId,time,dayOfWeek,hour,minute,dateTime,content,isRepeat from tb_alarmInfo where alarmId='"+alarmId0+"'",null);
        while(cursor.moveToNext()){
            String time=cursor.getString(2);
            int isRepeat=cursor.getInt(8);
            int alarmId=cursor.getInt(1);
            if(isRepeat==0 && System.currentTimeMillis()>Long.parseLong(time)){
                delete(alarmId);        //当该日程仅提醒一次，并且已过期时，在数据库中删除该闹钟
            }else{
                alarmInfo=new AlarmInfo();
                alarmInfo.set_id(cursor.getInt(0));
                alarmInfo.setAlarmId(cursor.getInt(1));
                alarmInfo.setTime(cursor.getString(2));
                alarmInfo.setDayOfWeek(cursor.getInt(3));
                alarmInfo.setHour(cursor.getInt(4));
                alarmInfo.setMinute(cursor.getInt(5));
                alarmInfo.setDateTime(cursor.getString(6));
                alarmInfo.setContent(cursor.getString(7));
                alarmInfo.setIsRepeat(cursor.getInt(8));
//                i++;
            }
        }
        cursor.close();
        return alarmInfo;
    }
}
