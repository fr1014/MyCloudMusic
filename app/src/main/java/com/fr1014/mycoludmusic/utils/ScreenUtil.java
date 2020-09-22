package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 创建时间:2020/9/14
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class ScreenUtil {
    public static int height;
    public static int width;
    private static ScreenUtil instance;
    private Context context;

    private ScreenUtil(Context context) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    public static ScreenUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ScreenUtil(context);
        }
        return instance;
    }


    /**
     * 得到手机屏幕的宽度, pix单位
     */

    /**
     * 获得通知栏的高度
     *
     * @return
     */
    public static int getStatusHeight(Context context) {
        int resid = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resid > 0) {
            return context.getResources().getDimensionPixelSize(resid);
        }
        return -1;
    }

    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenWidth() {
        return width;
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    //px转dp
    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}