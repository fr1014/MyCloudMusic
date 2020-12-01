package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.fr1014.mycoludmusic.app.MyApplication;

public class DisplayUtil {
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }
}