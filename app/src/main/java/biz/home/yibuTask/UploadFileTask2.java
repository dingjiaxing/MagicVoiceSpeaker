package biz.home.yibuTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

import biz.home.SpeakToitActivity;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2015/11/18.
 */
public class UploadFileTask2 extends AsyncTask<String, Integer, String> {
    public static Map<String,String > pics;
    Map<String,String> map;
    MagicUserInfoDao dao;
    Context context;
    private MagicResultResourceInfo resourceInfo;
    Handler handler;
    ProgressBar progressBar;

    public UploadFileTask2(ProgressBar progressBar,MagicResultResourceInfo resourceInfo,Context context,Handler handler) {
        this.progressBar=progressBar;
        this.resourceInfo=resourceInfo;
        this.pics= SpeakToitActivity.pics;
        this.context=context;
        this.handler=handler;
    }

    @Override
    protected void onPostExecute(String o) {
        Message msg=new Message();
        msg.what=1;
        handler.sendMessage(msg);

        super.onPostExecute(o);
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            publishProgress(10);
            //上传文件
            dao=new MagicUserInfoDao(context);
            String uid=dao.findUid();
            publishProgress(15);
            String telephone=dao.findTelephone();
            String text=resourceInfo.getContent();
            Map<String,String> map2=new HashMap<>();
            publishProgress(20);
            int i=0;
            for(String key:pics.keySet()){
                if(text.contains(key)){
                    String value=pics.get(key);
                    map2.put(key,value);
                }
            }
            publishProgress(30);
            pics=map2;
            String[] picPaths=new String[map2.size()];
            publishProgress(35);
            for(String key:map2.keySet()){
                String value=pics.get(key);
                picPaths[i]=value;
                String newValue=value.substring(value.lastIndexOf("/")+1);
                pics.put(key,newValue);
                i++;
            }
            publishProgress(50);
            System.out.println("picPaths.length:" + picPaths.length);
            System.out.println(" HttpHelp.uploadFile(uid, telephone, picPaths, pics):" + uid + telephone + picPaths + pics);
            publishProgress(70);
            String result= HttpHelp.uploadFile(uid, telephone, picPaths, pics);
            publishProgress(80);
            result=HttpHelp.getJsonString(result);
            map=HttpHelp.transferPicResultToMap(result);
            for(String key:map.keySet()){
                System.out.println("map key  value" +key+map.get(key) );
            }
            publishProgress(90);
            resourceInfo.setImage(map);
            SpeakToitActivity.projectReleaseResourceInfo=resourceInfo;
            publishProgress(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }
}
