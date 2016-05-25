package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import biz.home.model.VoiceFileRecord;

/**
 * Created by admin on 2015/12/24.
 */
public class VoiceFileDao  {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public VoiceFileDao(Context context) {
        helper=new DBOpenHelper(context);
    }
    public void add(VoiceFileRecord vfr){
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_voiceFileRecord(messageId,path,isUpload) values(?,?,?)",new Object[]{vfr.getMessageId(),vfr.getPath(),vfr.getUpload()});
        db.close();
    }
    public void delete(String messageId){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_voiceFileRecord where messageId='"+messageId+"'");
        db.close();
    }
    public List<VoiceFileRecord> find(){
        List<VoiceFileRecord> list=new ArrayList<>();
        db=helper.getReadableDatabase();
        Cursor c=db.rawQuery("select messageId,path,isUpload from tb_voiceFileRecord",null);
        VoiceFileRecord vrd=new VoiceFileRecord();
        while (c.moveToNext()){
            vrd.setMessageId(c.getString(0));
            vrd.setPath(c.getString(1));
            vrd.setUpload(Boolean.valueOf(c.getString(2)));
            list.add(vrd);
        }
        return list;
    }
    public int getCount(){
        int count=0;
        db=helper.getReadableDatabase();
        Cursor c=db.rawQuery("select count(*) from tb_voiceFileRecord",null);
        if(c.moveToNext()){
            count=c.getInt(0);
        }
        db.close();
        return count;
    }

}
