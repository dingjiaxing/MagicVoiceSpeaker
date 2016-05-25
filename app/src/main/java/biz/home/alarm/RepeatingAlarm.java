package biz.home.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 2015/8/22.
 */
public class RepeatingAlarm extends BroadcastReceiver {
    String TAG="RepeatingAlarm";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive ");
        String content=intent.getStringExtra("content");
        Toast.makeText(context,TAG+" onReceive",Toast.LENGTH_SHORT).show();
        Intent it=new Intent(context,AlarmAlertDialog.class);
        it.putExtra("content",content);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
