package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import biz.home.model.UserSetting;

/**
 * Created by admin on 2015/9/18.
 */
public class UserSettingDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public UserSettingDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void createUserSetting(){

        String sql2="create table tb_magicUserSetting(telephone varchar(11) primary key,stateItem1 integer,stateItem2 integer,stateItem3 integer,stateItem4 integer,stateItem5 integer,stateItem6 integer)";
        db=helper.getWritableDatabase();
        db.execSQL(sql2);
    }
    public void add(UserSetting setting){
        db=helper.getWritableDatabase();
        if(setting.getTelephone()!=null){
            db.execSQL("insert into tb_magicUserSetting(telephone,stateItem1,stateItem2,stateItem3,stateItem4,stateItem5,stateItem6) values(?,?,?,?,?,?,?) ",
                    new Object[]{setting.getTelephone(),setting.getStateItem1(),setting.getStateItem2(),setting.getStateItem3(),setting.getStateItem4(),setting.getStateItem5(),setting.getStateItem6()});
        }
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete  from tb_magicUserSetting");
    }
    public void update(UserSetting setting){
        db=helper.getWritableDatabase();
        if(setting.getTelephone()!=null){
            db.execSQL("update tb_magicUserSetting set stateItem1=?,stateItem2=?,stateItem3=?,stateItem4=?,stateItem5=?,stateItem6=? where telephone=?",
                    new Object[]{setting.getStateItem1(),setting.getStateItem2(),setting.getStateItem3(),setting.getStateItem4(),setting.getStateItem5(),setting.getStateItem6(),setting.getTelephone()});
        }
    }
    public UserSetting find(String telephone){
        UserSetting setting=null;
        db=helper.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.rawQuery("select * from tb_magicUserSetting where telephone='"+telephone+"'",null);
        if(cursor.moveToNext()){
            setting=new UserSetting(cursor.getString(0),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)));
        }
        return setting;
    }
    public int getCount(){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(telephone) from tb_magicUserSetting",null);
        if(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        return 0;
    }
}
