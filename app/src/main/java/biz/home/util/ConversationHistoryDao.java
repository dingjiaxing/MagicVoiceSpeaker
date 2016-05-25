package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;

/**
 * Created by admin on 2015/8/30.
 */
public class ConversationHistoryDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    String sql="";
    MagicUserInfoDao dao;
    private String TAG="ConversationHistoryDao";

    public ConversationHistoryDao(Context context){
        helper=new DBOpenHelper(context);
        dao=new MagicUserInfoDao(context);
    }
    public void createConversationHistory(){
        sql="create table tb_conversationHistory(_id integer primary key autoincrement,type varchar(35),url varchar(100),title varchar(100),topText varchar(300),centerText varchar(200),telephone varchar(22))";
        db=helper.getWritableDatabase();
        db.execSQL(sql);
    }
    public void deleteAll(){
        sql="delete from tb_conversationHistory";
        db=helper.getWritableDatabase();
        db.execSQL(sql);
    }
    public void addList(List<Fragment> mFragmentList){
        try{
            int size=mFragmentList.size();
            Log.i(TAG, "addList mFragmentList.size():" + size);
            for(int i=0;i<size;i++){
                Fragment f=mFragmentList.get(i);
                addFragment(f);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Fragment> findAll(){
        ArrayList<Fragment> mFragmentList=new ArrayList<Fragment>();
        db=helper.getWritableDatabase();
        String telephone=dao.findTelephone();
        sql="select * from tb_conversationHistory where telephone='"+telephone+"'";
        Cursor cursor=db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Fragment f;
            String type=cursor.getString(1);
            if(type.equals("MainWebFragment")){     //存储的是 web
                String url=cursor.getString(2);
                String textShow=cursor.getString(3);
                String question=cursor.getString(5);
                f=MainWebFragment.newInstance(url,textShow,question);
                mFragmentList.add(f);
            }else{      //存储的是普通文本交互
                String topText=cursor.getString(4);
                String centerText=cursor.getString(5);
                f=MainEditFragment.newInstance("",topText,centerText,MainEditFragment.BUTTON_DEFAULT);
                mFragmentList.add(f);
            }
        }
        return mFragmentList;
    }
    public void addFragment(Fragment f){
        try{
            db=helper.getWritableDatabase();
            String telephone=dao.findTelephone();
            if(f instanceof MainWebFragment){
                String url=((MainWebFragment) f).getCurrentUrl();
                String text=((MainWebFragment) f).webText.getText().toString();
                String question=((MainWebFragment) f).webQuestion.getText().toString();
                sql="insert into tb_conversationHistory(type,url,telephone,title,centerText) values('MainWebFragment','"+url+"','"+telephone+"','"+text+"','"+question+"')";
            }
            if(f instanceof MainEditFragment){
                try{
                    if(((MainEditFragment) f).getButtonState()){    //如果是普通的文本交互
                        String topText,centerText;
                        try{
                            topText=((MainEditFragment) f).getTopShow().getText().toString();
                        }catch (Exception e){
                            e.printStackTrace();
                            topText="";
                        }
                        centerText=((MainEditFragment) f).getCenterShow().getText().toString();
                        sql="insert into tb_conversationHistory(type,topText,centerText,telephone) values('MainEditFragment','"+topText+"','"+centerText+"','"+telephone+"')";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public int  getCount(){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(_id) from tb_conversationHistory",null);
        if(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        return 0;
    }
}
