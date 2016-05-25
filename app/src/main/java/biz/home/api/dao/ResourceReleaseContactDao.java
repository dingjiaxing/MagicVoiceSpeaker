package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceContactToRelease;
import biz.home.bean.ResourceReleaseContact;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceReleaseContactDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourceReleaseContactDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(ResourceReleaseContact mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourceReleaseContact(releaseId,userId,realName,contactTime) values(?,?,?,?)",
                new Object[]{mr.getReleaseId(),mr.getUserId(),mr.getRealName(),mr.getContactTime()} );
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceReleaseContact");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from tb_resourceReleaseContact",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public List<ResourceReleaseContact> findByReleaseId(String releaseId){
        List<ResourceReleaseContact> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select releaseId,userId,realName,contactTime from tb_resourceReleaseContact order by contactTime desc where releaseId="+"'"+releaseId+"'",null);

        while (cursor.moveToNext()){
            ResourceReleaseContact mr=new ResourceReleaseContact();
            mr.setReleaseId(cursor.getString(0));
            mr.setUserId(cursor.getString(1));
            mr.setRealName(cursor.getString(2));
            mr.setContactTime(cursor.getString(3));
            list.add(mr);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return list;
    }
}
