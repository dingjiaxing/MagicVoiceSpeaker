package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceFavorites;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourcePushDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourcePushDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(MagicResultResourcePush mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourcePush values(?,?,?,?,?,?)",
               new Object[]{mr.getPushId(),mr.getPostId(),mr.getUserId(),mr.getPushDate(),mr.getTitle(),mr.getPublisher()} );
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourcePush");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from tb_resourcePush",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public void deleteByPushId(String id){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourcePush where pushId="+"'"+id+"'");
        db.close();
    }
    public List<MagicResultResourcePush> find(){
        List<MagicResultResourcePush> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_resourcePush order by pushDate desc",null);

        while (cursor.moveToNext()){
            MagicResultResourcePush mr=new MagicResultResourcePush();
            mr.setPushId(cursor.getString(0));
            mr.setPostId(cursor.getString(1));
            mr.setUserId(cursor.getString(2));
            mr.setPushDate(cursor.getString(3));
            mr.setTitle(cursor.getString(4));
            mr.setPublisher(cursor.getString(5));
            list.add(mr);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return list;
    }



}
