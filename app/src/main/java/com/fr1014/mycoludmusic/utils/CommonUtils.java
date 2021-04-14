package com.fr1014.mycoludmusic.utils;

import android.text.format.DateUtils;
import android.widget.Toast;

import com.fr1014.mycoludmusic.app.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 创建时间:2020/9/10
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class CommonUtils {
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static void rd_ing(){
        toastShort("功能开发中");
    }

    public static Toast toastShort(String message) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
        return toast;
    }

    public static Toast toastLong(String message) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_LONG);
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

    public static String formatTimeYear(long timeStamp){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));      // 时间戳转换成时间
    }

    /**
     * @param str 时长
     * @return 时长为str，需要转化为毫秒的数字时长duration
     * @throws ParseException
     */
    public static long stringToDuration(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date date = formatter.parse(str);
        long duration = date.getMinutes() * 60000 + date.getSeconds() * 1000;
        return duration;
    }

    public static String strFormatTime(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("ss.SSS");
        SimpleDateFormat f = new SimpleDateFormat("mm:ss.SSS");
        Date date = formatter.parse(str);
        return f.format(date);
    }

    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static String formatNumber(long number) {
        String str;
        if (number > 10000) {
            int big = (int) (number / 10000);
            String small = (number % 10000) + "";
            str = big + "." + small.substring(0, 1) + "万";
        } else {
            str = number + "";
        }
        return str;
    }
}
