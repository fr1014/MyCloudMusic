package com.fr1014.mycoludmusic.utils;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Create by fanrui on 2020/12/16
 * Describe:
 */
public class StatusBarUtils {
    private final static int MIUI = 0;
    private final static int FLYME = 1;
    private final static int COMMON = 2;

    public static void setStatusBarTextColor(Window window, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//黑色
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//白色
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    public static void setImmersiveStatusBar(Window window, boolean fontIconDark) {
        setTranslucentStatus(window);
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarFontIconDark(window, COMMON);
            } else if (OSUtils.isMiui()) {
                setStatusBarFontIconDark(window, MIUI);
            } else if (OSUtils.isFlyme()) {
                setStatusBarFontIconDark(window, FLYME);
            }
        }
    }

    /**
     * 设置文字颜色
     */
    public static void setStatusBarFontIconDark(Window window, int type) {
        switch (type) {
            case MIUI:
                setMiuiUI(window, true);
                break;
            case COMMON:
                setCommonUI(window);
                break;
            case FLYME:
                setFlymeUI(window, true);
                break;
        }
    }

    public static void setCommonUI(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    public static void setFlymeUI(Window window, boolean dark) {
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMiuiUI(Window window, boolean dark) {
        try {
            Class clazz = window.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏透明
     */
    private static void setTranslucentStatus(Window window) {

        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //清除透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏颜色必须添加
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//设置透明
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //19
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
