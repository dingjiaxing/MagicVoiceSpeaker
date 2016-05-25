package biz.home.application.myUtils;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.UserSettingDao;

public class ShakeListenerUtils implements SensorEventListener
{
    private Activity context;
    MagicUserInfoDao userInfoDao;
    UserSettingDao userSettingDao;
    private long lastClickTime;

    public ShakeListenerUtils(Activity context)
    {
        super();
        this.context = context;
        userInfoDao=new MagicUserInfoDao(context);
        userSettingDao=new UserSettingDao(context);
        lastClickTime=System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //如果在设置中设置了“摇一摇”启动应用时，便会执行以下启动应用的代码
        try{
            int sensorType = event.sensor.getType();
            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;

            if (sensorType == Sensor.TYPE_ACCELEROMETER)
            {

			/*正常情况下，任意轴数值最大就在9.8~10之间，只有在突然摇动手机
			  的时候，瞬时加速度才会突然增大或减少。   监听任一轴的加速度大于17即可
			*/
                if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math
                        .abs(values[2]) > 17))
                {
//                context.overridePendingTransition(R.anim.zoom_out_enter,
//                        R.anim.zoom_out_exit);
                    //检测到晃动后启动OpenDoor效果
                    if(userSettingDao.find(userInfoDao.findTelephone()).getStateItem4()==1){
                        if(System.currentTimeMillis()-lastClickTime>1000&&SpeakToitActivity.isForeground==false) {
                            lastClickTime = System.currentTimeMillis();
                            Intent intent = new Intent(context, SpeakToitActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            context.finish();
                        }
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //当传感器精度改变时回调该方法，Do nothing.
    }

}