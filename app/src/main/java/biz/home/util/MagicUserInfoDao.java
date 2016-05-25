package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import biz.home.model.MagicUserInfo;

/**
 * Created by admin on 2015/8/22.
 */
public class MagicUserInfoDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;


    public MagicUserInfoDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void createUser(){
        String sql2="create table tb_magicUserInfo(uid varchar(64) primary key,telephone varchar(11),userName varchar(45),nickName varchar(45),realName varchar(45),company varchar(80),position varchar(80),email varchar(40))";
        db=helper.getWritableDatabase();
        db.execSQL(sql2);
    }

    public void deleteUser(){
        String sql2="drop table tb_magicUserInfo";
        db=helper.getWritableDatabase();
        db.execSQL(sql2);
    }
    public void add(MagicUserInfo magicUserInfo){
        db=helper.getWritableDatabase();
        if(magicUserInfo.getNickName()==null){
            magicUserInfo.setNickName("");
        }
        if(magicUserInfo.getCompanyName()==null){
            magicUserInfo.setCompanyName("");
        }
        if(magicUserInfo.getPosition()==null){
            magicUserInfo.setPosition("");
        }
        if(magicUserInfo.getEmail()==null){
            magicUserInfo.setEmail("");
        }
        db.execSQL("insert into tb_magicUserInfo(uid,telephone,userName,nickName,realName,company,position,email) values(?,?,?,?,?,?,?,?)",
                new Object[]{magicUserInfo.getUid(),magicUserInfo.getTelephone(),magicUserInfo.getUserName(),magicUserInfo.getNickName(),magicUserInfo.getRealName(),magicUserInfo.getCompanyName(),magicUserInfo.getPosition(),magicUserInfo.getEmail()});
    }
    public void update(MagicUserInfo magicUserInfo){
        db=helper.getWritableDatabase();
        db.execSQL("update tb_magicUserInfo set telephone=?,userName=?,nickName=?,realName=?,company=?,position=?,email=? where uid=?",
                new Object[]{magicUserInfo.getTelephone(),magicUserInfo.getUserName(),magicUserInfo.getNickName(),magicUserInfo.getRealName(),magicUserInfo.getCompanyName(),magicUserInfo.getPosition(),magicUserInfo.getEmail(),magicUserInfo.getUid()});
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_magicUserInfo");
    }
    public int getCount(){
        db=helper.getWritableDatabase();
        int result=0;
        Cursor cursor=db.rawQuery("select count(uid) from tb_magicUserInfo",null);
        if(cursor.moveToNext()){
            result=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        return result;
    }
    public String findUid(){
        db=helper.getWritableDatabase();
        String result=null;
        Cursor cursor=db.rawQuery("select uid from tb_magicUserInfo",null);
        if(cursor.moveToNext()){
            result= cursor.getString(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        return result;
    }
    public String findTelephone(){
        db=helper.getWritableDatabase();
        String result=null;
        Cursor cursor=null;
        cursor=db.rawQuery("select telephone from tb_magicUserInfo",null);
        if(cursor.moveToNext()){
            result=cursor.getString(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        return result;
    }
    public MagicUserInfo findUser(){
        db=helper.getWritableDatabase();
        MagicUserInfo user=null;
        Cursor cursor=db.rawQuery("select * from tb_magicUserInfo",null);
        if(cursor.moveToNext()) {
            user=new MagicUserInfo();
            user.setUid(cursor.getString(0));
            user.setTelephone(cursor.getString(1));
            user.setUserName(cursor.getString(2));
            user.setNickName(cursor.getString(3));
            user.setRealName(cursor.getString(4));
            user.setCompanyName(cursor.getString(5));
            user.setPosition(cursor.getString(6));
            user.setEmail(cursor.getString(7));
//            return user;
        }
        if(cursor!=null){
            cursor.close();
        }
        return user;
    }
}
