package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceContact;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceContactDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourceContactDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(MagicResultResourceContact mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourceContact values(?,?,?,?,?,?,?)",
                new Object[]{mr.getContactId(),mr.getPostId(),mr.getUserId(),mr.getRealName(),mr.getContactDate(),mr.getTitle(),mr.getPublisher()} );
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceContact");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from tb_resourceContact",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public void deleteByContactId(String id){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceContact where contactId="+"'"+id+"'");
        db.close();
    }
    public List<MagicResultResourceContact> find(){
        List<MagicResultResourceContact> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_resourceContact order by contactDate desc",null);

        while (cursor.moveToNext()){
            MagicResultResourceContact mr=new MagicResultResourceContact();
            mr.setContactId(cursor.getString(0));
            mr.setPostId(cursor.getString(1));
            mr.setUserId(cursor.getString(2));
            mr.setRealName(cursor.getString(3));
            mr.setContactDate(cursor.getString(4));
            mr.setTitle(cursor.getString(5));
            mr.setPublisher(cursor.getString(6));
            list.add(mr);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return list;
    }
}
