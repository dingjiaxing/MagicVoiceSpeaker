package biz.home.application;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by adg on 2015/7/27.
 */
public class App extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            SQLiteDatabase db= Connector.getDatabase();
            JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
            JPushInterface.init(this);     		// 初始化 JPush
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
