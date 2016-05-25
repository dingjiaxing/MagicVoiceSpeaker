package biz.home.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 2015/11/27.
 */
public class BootStartReceiver extends BroadcastReceiver {
    private static final String TAG = "BootStartReceiver";



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive 接收到开机");
//        Toast.makeText(context, "BootStartReceiver 接收到开机事件", Toast.LENGTH_SHORT).show();
        Intent mintent=new Intent(context,BootStartService.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(mintent);
    }
}
