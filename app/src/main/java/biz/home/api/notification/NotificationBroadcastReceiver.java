package biz.home.api.notification;

import android.app.Instrumentation;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

import biz.home.SettingCommunicateActivity;
import biz.home.SpeakToitActivity;

/**
 * Created by admin on 2016/1/21.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {
    /*
    是否打开话筒的标志位，如果点击了在通知栏点击了话筒，则将flagIsOpenMicrophone置为true，否则为false
     */
    public static Boolean flagIsOpenMicrophone=false;
    public final static String ACTION_BTN = "com.example.notification.btn.login";

    public final static String INTENT_NAME = "btnid";

    public final static String OPENMICROPHONEFLAG="isOpenMicrophoneFlag";

    public final static int INTENT_BTN_LOGIN = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_BTN)) {
            int btn_id = intent.getIntExtra(INTENT_NAME, 0);
            switch (btn_id) {
                case INTENT_BTN_LOGIN:
                    System.out.println("NotificationBroadcastReceiver ：" + "接收到了点击");
                    //Toast.makeText(MainActivity.this, "从通知栏点登录", Toast.LENGTH_SHORT).show();
                    intent=new Intent(context,SpeakToitActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    // intent.putExtra(OPENMICROPHONEFLAG,true);
                    flagIsOpenMicrophone=true;
                    context.startActivity(intent);
//                    context.startService(new Intent(context,NotificationService.class));

//                    if(SpeakToitActivity.isForeground){
//                        Runtime runtime = Runtime.getRuntime();
//                        try {
//                            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                super.run();
//                                Instrumentation inst = new Instrumentation();
//                                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                            }
//                        }.start();
//                    }

                    //unregeisterReceiver();
                   // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                   // notificationManager.cancel(0);

                    break;
            }
        }
    }
}
