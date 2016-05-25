package biz.home.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import biz.home.model.MagicUserInfo;

/**
 * Created by admin on 2015/8/19.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION=4;
    private static final String DBNAME="magic.db";
    private Context context;
    private  String TAG="DBOpenHelper";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBOpenHelper(Context context){
        super(context,DBNAME,null,VERSION);
        this.context=context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1="create table tb_contactInfo(tel varchar(30) primary key,name  varchar(30),namePinyin varchar(40))";
        db.execSQL(sql1);
        String sql2="create table tb_magicUserInfo (uid varchar(30) primary key,telephone varchar(30),userName varchar(30),nickName varchar(30), realName varchar(30),email varchar(40)) ";
        sql2="create table tb_magicUserInfo(uid varchar(64) primary key,telephone varchar(11),userName varchar(45),nickName varchar(45),realName varchar(45),company varchar(80),position varchar(80),email varchar(40))";
        db.execSQL(sql2);
        sql2="create table tb_magicUserSetting(telephone varchar(11) primary key,stateItem1 integer,stateItem2 integer,stateItem3 integer,stateItem4 integer,stateItem5 integer,stateItem6 integer)";
        db.execSQL(sql2);
        sql2="create table tb_conversationHistory(_id integer primary key autoincrement,type varchar(35),url varchar(100),title varchar(100),topText varchar(300),centerText varchar(200),telephone varchar(22))";
        db.execSQL(sql2);
        sql2="create table tb_alarmInfo(_id integer primary key autoincrement,alarmId integer,time varchar(100),dayOfWeek integer,hour integer,minute integer,dateTime varchar(100),content varchar(100),isRepeat integer)";
        db.execSQL(sql2);
        sql2="create table tb_artificialAnswer(_id integer primary key autoincrement,type text,question text,answer text,url text)";
        db.execSQL(sql2);
        sql2="create table tb_voiceFileRecord(messageId text primary key ,path text,isUpload boolean)";
        db.execSQL(sql2);
        //和资源相关的表
        sql2="create table tb_resourceCollect(favoritesId text primary key ,postId text,userId text,favoritesDate text,title text,publisher text)";
        db.execSQL(sql2);
        sql2="create table tb_resourcePush(pushId text primary key ,postId text,userId text,pushDate text,title text,publisher text)";
        db.execSQL(sql2);
        sql2="create table tb_resourceContact(contactId text primary key ,postId text,userId text,realName text,contactDate text,title text,publisher text)";
        db.execSQL(sql2);
        sql2="create table tb_resourceRelease(releaseId text primary key,postId text,userId text,telephone text,releaseDate text,title,browseCount text)";
        db.execSQL(sql2);
        sql2="create table tb_resourceReleaseContact(_id integer  primary key autoincrement ,releaseId text,userId text,realName text,contactTime text)";
        db.execSQL(sql2);
        sql2="create table tb_resourceInfo(postId text primary key,title text,content text,resourceTypeName text,dateTime text,userId text,realName text," +
                " companyName text,position text,telephone text,email text,favoritesCount int,browseCount int,contactCount int,voicePath text,confirm bool,favoritesStatus bool)";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i=oldVersion;i<=newVersion;i++){
            switch (i){
                case 2:
                    //创建表 tb_

                    break;
                case 3:
                    String sql2= null;
                    try {
                        Log.i(TAG, "onUpgrade: 创建版本3的表 tb_voiceFileRecord");
                        sql2 = "create table tb_voiceFileRecord(messageId text primary key ,path text,isUpload boolean)";
                        db.execSQL(sql2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    //和资源相关的表
                    sql2="create table tb_resourceCollect(favoritesId text primary key ,postId text,userId text,favoritesDate text,title text,publisher text)";
                    db.execSQL(sql2);
                    sql2="create table tb_resourcePush(pushId text primary key ,postId text,userId text,pushDate text,title text,publisher text)";
                    db.execSQL(sql2);
                    sql2="create table tb_resourceContact(contactId text primary key ,postId text,userId text,realName text,contactDate text,title text,publisher text)";
                    db.execSQL(sql2);
                    sql2="create table tb_resourceRelease(releaseId text primary key,postId text,userId text,telephone text,releaseDate text,title,browseCount text)";
                    db.execSQL(sql2);
                    sql2="create table tb_resourceReleaseContact(_id integer private key autoincrement,releaseId text,userId text,realName text,contactTime text)";
                    db.execSQL(sql2);
                    sql2="create table tb_resourceInfo(postId text primary key,title text,content text,resourceTypeName text,dateTime text,userId text,realName text," +
                            " companyName text,position text,telephone text,email text,favoritesCount int,browseCount int,contactCount int,voicePath text,confirm bool,favoritesStatus bool)";
                    db.execSQL(sql2);
                    break;
                default:
                    break;
            }
        }

    }
}
