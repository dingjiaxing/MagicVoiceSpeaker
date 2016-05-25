package biz.home.yibuTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import biz.home.SpeakToitActivity;
import biz.home.application.myUtils.ConnectionDetector;
import biz.home.assistActivity.resource.MagicRecommendResourceList;
import biz.home.assistActivity.resource.ResourceInfo;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2016/2/1.
 */
public class OpenResourceInfoTask extends AsyncTask {
    private static final String TAG ="OpenResourceInfoTask";
    private  Context context;
    private String url;
    private MagicUserInfoDao dao;
    MagicResultResourceInfo mri;
    private String userId;
    private String postId;

    public OpenResourceInfoTask(Context context,String postId) {
        this.context=context;
        this.postId=postId;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            dao=new MagicUserInfoDao(context);
            userId=dao.findUid();
            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCEINFO);
            System.out.println("postId:" +postId );
            info.setPostId(postId);
            String s= HttpHelp.send(info);
            MagicResult mr=HttpHelp.transfer(s);
            mri=mr.getResourceInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(mri!=null){
            Intent intent=new Intent(context,ResourceInfo.class);
            intent.putExtra("resourceInfo",mri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else{
            ConnectionDetector cd=new ConnectionDetector(context);
            Log.i(TAG, "onPostExecute: 是否联网："+cd.isConnectingToInternet());
            if(!cd.isConnectingToInternet()){
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                alert.setTitle("温馨提示");
                alert.setMessage("您的网络未连接，请连接网络后再打开");
                alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }else{
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                alert.setTitle("温馨提示");
                alert.setMessage("资源服务器正在维护中，请稍后重试！");
                alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }

        }

    }
}
