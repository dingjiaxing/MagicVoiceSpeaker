package biz.home.alarm;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by admin on 2015/8/21.
 */
public class Vibrate {
    final static String TAG = "GameEngine";
    Vibrator vibrator;
    long[] pattern = { 100, 50, 800, 600 }; // 震动周期   可以自己设置
    public Vibrate(Context context) {
        vibrator = (Vibrator) context
                .getSystemService(context.VIBRATOR_SERVICE);
    }
    public void playVibrate(int type) {
        vibrator.vibrate(pattern, type);
        // -1不重复，非-1为从pattern的指定下标开始重复
    }
    public void Stop() {
        vibrator.cancel();
    }
}
