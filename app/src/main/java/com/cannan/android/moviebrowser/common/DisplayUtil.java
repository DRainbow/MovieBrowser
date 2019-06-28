package com.cannan.android.moviebrowser.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * @ClassName: DisplayUtil
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 01:06
 */
public class DisplayUtil {

    public static int dp2px(int dp) {
        return (int) ((dp * Resources.getSystem().getDisplayMetrics().density) + 0.5);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return dm.widthPixels;
    }
}
