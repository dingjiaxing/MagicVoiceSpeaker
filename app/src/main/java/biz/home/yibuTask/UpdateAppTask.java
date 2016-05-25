package biz.home.yibuTask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import biz.home.SpeakToitActivity;
import biz.home.SplashActivity;
import biz.home.application.myUtils.UpdateService;
import biz.home.bean.MagicInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2015/12/2.
 */
public class UpdateAppTask extends AsyncTask {
    private String TAG="UpdateAppTask";
    private MagicUserInfoDao dao;
    private Activity context;
    private String version="";
    private String updateAppUrl;
    private int resourceFlag;
    public static int SETTINGREOURCE=1101;
    public static int SPEAKEMAIN=1102;
    @Override
    protected Object doInBackground(Object[] params) {
        dao=new MagicUserInfoDao(context);
        String uid=dao.findUid();
        MagicInfo info=new MagicInfo();
        info.setUid(uid);
        info.setApi(MagicInfoApiEnum.ANDROIDVERSION);
        String str= HttpHelp.send(info);
        version=HttpHelp.getJsonString(str).trim();
        if(!version.equals(SplashActivity.appVersion)){
            info.setApi(MagicInfoApiEnum.ANDROIDAPP);
            str=HttpHelp.send(info);
            updateAppUrl=HttpHelp.getJsonString(str);
        }
        return null;
    }

    public UpdateAppTask(Activity context,int resourceFlag) {
        this.context=context;
        this.resourceFlag=resourceFlag;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        //如果是在设置页面点击了检查更新
        if(resourceFlag==SETTINGREOURCE){
            if(version.equals(SplashActivity.appVersion)){
                Toast.makeText(context,"已是最新版本",Toast.LENGTH_LONG).show();
            }else {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                alert.setCancelable(false);
                alert.setTitle("温馨提示");
                alert.setMessage("是否确定更新？");
                alert.setPositiveButton("            更新              ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,UpdateService.class);
                        intent.putExtra("Key_App_Name","Magic神奇小秘书");
                        intent.putExtra("Key_Down_Url",updateAppUrl);
                        context.startService(intent);
//                        DownloaderTask task=new DownloaderTask();
//                        task.execute(updateAppUrl);
                        /*
                        //用系统自带的浏览器来下载
                        Uri uri = Uri.parse(updateAppUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                        */
                    }
                }).setNegativeButton("             取消            ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
//            在程序刚刚启动时检测一下是否有新版本
        }else if(resourceFlag==SPEAKEMAIN){
            if(version.equals(SplashActivity.appVersion)){
//                Toast.makeText(context,"已是最新版本",Toast.LENGTH_LONG).show();
            }else {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                alert.setTitle("温馨提示");
                alert.setCancelable(false);
                alert.setMessage("检测到有新版本，是否更新？");
                alert.setPositiveButton("            更新              ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,UpdateService.class);
                        intent.putExtra("Key_App_Name","Magic神奇小秘书");
                        intent.putExtra("Key_Down_Url",updateAppUrl);
                        context.startService(intent);
//                        DownloaderTask task=new DownloaderTask();
//                        task.execute(updateAppUrl);
                        /*
                        Uri uri = Uri.parse(updateAppUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                        */
                    }
                }).setNegativeButton("             取消            ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        }
    }
    //内部类
    private class DownloaderTask extends AsyncTask<String, Void, String> {

        public DownloaderTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = params[0];
//          Log.i("tag", "url="+url);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            fileName = URLDecoder.decode(fileName);
            Log.i(TAG, "fileName=" + fileName);

//            File directory = Environment.getExternalStorageDirectory();
            File directory = context.getCacheDir();
            File file = new File(directory, fileName);
            if (file.exists()) {
                Log.i(TAG, "The file has already exists.");
                return fileName;
            }
            try {
                HttpClient client = new DefaultHttpClient();
//                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();
                    writeToSDCard(fileName, input);
                    input.close();
//                  entity.consumeContent();
                    return fileName;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            closeProgressDialog();
            if (result == null) {
                Toast t = Toast.makeText(context, "连接错误！请稍后再试！", Toast.LENGTH_SHORT);
//                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }

            Toast t = Toast.makeText(context, "已保存到SD卡。", Toast.LENGTH_SHORT);
//            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
//            File directory = Environment.getExternalStorageDirectory();
            File directory=context.getCacheDir();
            File file = new File(directory, result);
            Log.i(TAG, "Path=" + file.getAbsolutePath());

            Intent intent = getFileIntent(file);

            context.startActivity(intent);

        }
    }
    public Intent getFileIntent(File file){
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type="+type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }
    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
      /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if(end.equals("pdf")){
            type = "application/pdf";//
        }
        else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            type = "audio/*";
        }
        else if(end.equals("3gp")||end.equals("mp4")){
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            type = "image/*";
        }
        else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
//      else if(end.equals("pptx")||end.equals("ppt")){
//        type = "application/vnd.ms-powerpoint";
//      }else if(end.equals("docx")||end.equals("doc")){
//        type = "application/vnd.ms-word";
//      }else if(end.equals("xlsx")||end.equals("xls")){
//        type = "application/vnd.ms-excel";
//      }
        else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type="*/*";
        }
        return type;
    }
    public void writeToSDCard(String fileName,InputStream input){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
//          if(file.exists()){
//              Log.i("tag", "The file has already exists.");
//              return;
//          }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                while ((j = input.read(b)) != -1) {
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Log.i("tag", "NO SDCard.");
        }
    }
}
