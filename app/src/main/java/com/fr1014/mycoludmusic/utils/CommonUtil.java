package com.fr1014.mycoludmusic.utils;

import android.widget.Toast;

import com.fr1014.mycoludmusic.app.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 创建时间:2020/9/10
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class CommonUtil {

    public static Toast toastShort(String message) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), null, Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
        return toast;
    }

    public static Toast toastLong(String message) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), null, Toast.LENGTH_LONG);
        toast.setText(message);
        toast.show();
        return toast;
    }

    //格式化歌曲时间
    public static String formatTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date data = new Date(time);
        return dateFormat.format(data);
    }

    public static long stringToDuration(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date date = (Date) formatter.parse(str);
        return date.getTime();
    }

    public static String strFormatTime(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("ss.SSS");
        SimpleDateFormat f = new SimpleDateFormat("mm:ss.SSS");
        Date date = (Date) formatter.parse(str);
        return f.format(date);
    }
}
