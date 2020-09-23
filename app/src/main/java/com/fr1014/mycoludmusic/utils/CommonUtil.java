package com.fr1014.mycoludmusic.utils;

import android.widget.Toast;

import com.fr1014.mycoludmusic.app.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建时间:2020/9/10
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class CommonUtil {

    public static void toastShort(String message) {
        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String message) {
        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_LONG).show();
    }

    //格式化歌曲时间
    public static String formatTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date data = new Date(time);
        return dateFormat.format(data);
    }
}
