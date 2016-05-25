package biz.home.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by lmw on 2015/7/16.
 */
public class SelectorUtil {

    public static StateListDrawable getBottomMenuBackground(Drawable[] drawables){
        StateListDrawable bg=new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed},drawables[1]);
        bg.addState(new int[]{},drawables[0]);
        return bg;
    }
}
