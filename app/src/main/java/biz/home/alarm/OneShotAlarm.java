package biz.home.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by admin on 2015/8/22.
 */
public class OneShotAlarm extends BroadcastReceiver {
    private Button button;
    MediaPlayer mMediaPlayer;
    Vibrate vi;
    String TAG="OneShotAlarm";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive ");
//        Toast.makeText(context, TAG + " onReceive", Toast.LENGTH_SHORT).show();
//        intent.getStringExtra("")
        String content=intent.getStringExtra("content");
        Log.d(TAG, "onReceive content:"+content);
        Intent it=new Intent(context,AlarmAlertDialog.class);
        it.putExtra("content",content);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
