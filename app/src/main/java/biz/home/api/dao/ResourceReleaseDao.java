package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceRelease;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceReleaseDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourceReleaseDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(MagicResultResourceRelease mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourceRelease values(?,?,?,?,?,?,?)",
                new Object[]{mr.getReleaseId(),mr.getPostId(),mr.getUserId(),mr.getTelephone(),mr.getReleaseDate(),mr.getTitle(),mr.getBrowseCount()} );
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceRelease");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from tb_resourceRelease",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public void deleteByReleaseId(String id){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceRelease where releaseId="+"'"+id+"'");
        db.close();
    }
    public List<MagicResultResourceRelease> find(){
        List<MagicResultResourceRelease> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_resourceRelease order by releaseDate desc",null);

        while (cursor.moveToNext()){
            MagicResultResourceRelease mr=new MagicResultResourceRelease();
            mr.setReleaseId(cursor.getString(0));
            mr.setPostId(cursor.getString(1));
            mr.setUserId(cursor.getString(2));
            mr.setTelephone(cursor.getString(3));
            mr.setReleaseDate(cursor.getString(4));
            mr.setTitle(cursor.getString(5));
            mr.setBrowseCount(cursor.getString(6));
            list.add(mr);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return list;
    }
}
