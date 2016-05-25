package biz.home.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.bean.MagicResultResourceFavorites;
import biz.home.util.DBOpenHelper;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceCollectDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ResourceCollectDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(MagicResultResourceFavorites mr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_resourceCollect(favoritesId,postId,userId,favoritesDate,title,publisher) values(?,?,?,?,?,?)",
                new Object[]{mr.getFavoritesId(),mr.getPostId(),mr.getUserId(),mr.getFavoritesDate(),mr.getTitle(),mr.getPublisher()});
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceCollect");
        db.close();
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(favoritesId) from tb_resourceCollect",null);

        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return count;
    }
    public void deleteByFavoritesId(String id){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_resourceCollect where favoritesId="+"'"+id+"'");
        db.close();
    }
    public List<MagicResultResourceFavorites> find(){
        List<MagicResultResourceFavorites> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select distinct favoritesId ,postId,userId,favoritesDate,title,publisher from tb_resourceCollect order by favoritesDate desc",null);

        while (cursor.moveToNext()){
            MagicResultResourceFavorites mr=new MagicResultResourceFavorites();
            mr.setFavoritesId(cursor.getString(0));
            mr.setPostId(cursor.getString(1));
            mr.setUserId(cursor.getString(2));
            mr.setFavoritesDate(cursor.getString(3));
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
