package biz.home.api.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 2015/12/10.
 */
public class MessageClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "通知被点击了", Toast.LENGTH_SHORT).show();
        Log.i("JPush", "onReceive 通知被点击了");
        MyJPushReceiver.count=0;
        Intent realIntent = intent.getParcelableExtra("realIntent");
        realIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(realIntent);
    }
}
