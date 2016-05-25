package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceInfo;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceInfoDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourceInfoDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(MagicResultResourceInfo mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourceInfo values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{mr.getPostId(),mr.getTitle(),mr.getContent(),mr.getResourceTypeName(),mr.getDateTime(),mr.getUserId(),
                mr.getCompanyName(),mr.getPosition(),mr.getTelephone(),mr.getEmail(),mr.getFavoritesCount(),mr.getBrowseCount(),
                mr.getContactCount(),mr.getVoicePath(),mr.isConfirm()?"true":"false",mr.isFavoritesStatus()?"true":"false"} );
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceInfo");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from tb_resourceInfo",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public void deleteByPostId(String id){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceInfo where postId="+"'"+id+"'");
        db.close();
    }
    public List<MagicResultResourceInfo> find(String postId){
        List<MagicResultResourceInfo> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_resourceInfo order by dateTime desc where postId="+"'"+postId+"'",null);

        while (cursor.moveToNext()){
            MagicResultResourceInfo mr=new MagicResultResourceInfo();
            mr.setPostId(cursor.getString(0));
            mr.setTitle(cursor.getString(1));
            mr.setContent(cursor.getString(2));
            mr.setResourceTypeName(cursor.getString(3));
            mr.setDateTime(cursor.getString(4));
            mr.setUserId(cursor.getString(5));
            mr.setRealName(cursor.getString(6));
            mr.setCompanyName(cursor.getString(7));
            mr.setPosition(cursor.getString(8));
            mr.setTelephone(cursor.getString(9));
            mr.setEmail(cursor.getString(10));
            mr.setFavoritesCount(cursor.getInt(11));
            mr.setBrowseCount(cursor.getInt(12));
            mr.setContactCount(cursor.getInt(13));
            mr.setVoicePath(cursor.getString(14));
            mr.setConfirm(Boolean.parseBoolean(cursor.getString(15)));
            mr.setFavoritesStatus(Boolean.parseBoolean(cursor.getString(16)));
            list.add(mr);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return list;
    }

}
