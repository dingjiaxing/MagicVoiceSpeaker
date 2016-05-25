package biz.home.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by lmw on 2015/7/16.
 */
public class BackgroudChangeUtil {
    public static Drawable getDrawable(Context context,Bitmap bitmap,String notPressColor,String pressColor){
        Drawable sourceDrawable=new BitmapDrawable(null,bitmap);
        //Drawable sourceDrawable = context.getResources().getDrawable(R.drawable.keyboard_control);
        Bitmap sourceBitmap = ColorChangeUtil.convertDrawableToBitmap(sourceDrawable);
        //Pass the bitmap and color code to change the icon color dynamically.
        Bitmap mNoPressBitmap = ColorChangeUtil.changeImageColor(sourceBitmap, Color.parseColor(notPressColor));
        Bitmap mPressBitmap = ColorChangeUtil.changeImageColor(sourceBitmap, Color.parseColor(pressColor));
        Drawable[] drawables=new Drawable[]{new BitmapDrawable(null,mNoPressBitmap),
                new BitmapDrawable(null,mPressBitmap)};
        return SelectorUtil.getBottomMenuBackground(drawables);
    }
}
