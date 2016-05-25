package biz.home.yibuTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import biz.home.SettingInformationsActivity;
import biz.home.SpeakToitActivity;
import biz.home.api.MapLocation;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.fragment.MainEditFragment;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicResultStatusEnum;
import biz.home.model.MagicUserInfo;
import biz.home.util.DeviceIdUtil;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2015/9/9.
 */
public class UpdateUserInfoTask extends AsyncTask {
    Context context;
    MagicUserInfo user;
    MagicUserInfoDao dao;
    MagicResult magicResult;

    public UpdateUserInfoTask() {
    }

    public UpdateUserInfoTask(Context context, MagicUserInfo user, MagicUserInfoDao dao) {
        this.context=context;
        this.user=user;
        this.dao=dao;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try{
            String uid="";
            if(dao.getCount()!=0){
                uid=dao.findUid();
            }
            String token="SQe6100cdcee892111";
            String api= MagicInfoApiEnum.UPDATEUSER.getCode();
            user.setUid(uid);
            MagicInfo magicInfo=new MagicInfo(uid,token,MagicInfoApiEnum.UPDATEUSER,user);
            String str = HttpHelp.send(magicInfo);      //向服务器发送请求，获得服务器回复的字符串
            magicResult=HttpHelp.transfer(str);         //将服务器回复的字符串转化为对象
            if(magicResult.getStatus().equals(MagicResultStatusEnum.SUCCESS)){
                dao.update(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try{
            if(magicResult.getStatus().equals(MagicResultStatusEnum.SUCCESS)){
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("系统提示：");
                alert.setMessage("恭喜您，信息更新成功！");
                alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }else{
                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
            }
            if(magicResult.getStatus().equals(MagicResultStatusEnum.ERROR)){
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("系统提示：");
                alert.setMessage("服务器错误，请稍后重试！");
                alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
            super.onPostExecute(o);
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
