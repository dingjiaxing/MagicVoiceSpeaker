package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import biz.home.bean.ArtificialAnswer;

/**
 * Created by admin on 2015/11/3.
 */
public class ArtificialAnswerDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public ArtificialAnswerDao(Context context) {
        helper=new DBOpenHelper(context);
        db=helper.getWritableDatabase();
        String sql2="create table if not exists tb_artificialAnswer(_id integer primary key autoincrement,type text,question text,answer text,url text)";
        db.execSQL(sql2);
    }
    public void add(ArrayList<ArtificialAnswer> list){
        for(int i=0;i<list.size();i++){
            ArtificialAnswer aa=list.get(i);
            add(aa);
        }
    }
    public void add(ArtificialAnswer aa){
        if(aa.getType()==null){
            aa.setType("");
        }
        if(aa.getQuestion()==null){
            aa.setQuestion("");
        }
        if(aa.getAnswer()==null){
            aa.setAnswer("");
        }
        if(aa.getUrl()==null){
            aa.setUrl("");
        }
        db=helper.getWritableDatabase();
        db.execSQL("insert into tb_artificialAnswer(type,question,answer,url) values(?,?,?,?)",new String[]{aa.getType(),aa.getQuestion(),aa.getAnswer(),aa.getUrl()});
        db.close();
    }
    public void deleteAll(){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_artificialAnswer");
        db.close();
    }
    public void delete(String answer){
        db=helper.getWritableDatabase();
        db.execSQL("delete from tb_artificialAnswer where answer=?",new String[]{answer});
        db.close();
    }
    public   ArrayList<ArtificialAnswer> findAll(){
        ArrayList<ArtificialAnswer> list=new ArrayList<>();

        db=helper.getWritableDatabase();
        String sql="select type,question,answer,url from tb_artificialAnswer";
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            ArtificialAnswer aa=new ArtificialAnswer();
            aa.setType(cursor.getString(0));
            aa.setQuestion(cursor.getString(1));
            aa.setAnswer(cursor.getString(2));
            aa.setUrl(cursor.getString(3));
            list.add(aa);
        }
        cursor.close();
        db.close();
        return list;
    }
    private int getCount(){
        int count=0;
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(_id) from tb_artificialAnswer", null);
        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

}
