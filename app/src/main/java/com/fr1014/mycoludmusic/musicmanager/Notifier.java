package com.fr1014.mycoludmusic.musicmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fr1014.mycoludmusic.MainActivity;
import com.fr1014.mycoludmusic.R;

/**
 * 创建时间:2020/9/22
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class Notifier {
    //NOTIFICATION_ID指的是创建的通知的ID
    private static final int NOTIFICATION_ID = 0x111;
    //Channel ID 必须保证唯一
    private static final String CHANNEL_ID = "com.fr1014.cloudmusic.notification.channel";
    private MusicService musicService;
    private NotificationManager notificationManager;

    private Notifier() {

    }

    public static Notifier getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static Notifier instance = new Notifier();
    }

    public void init(MusicService musicService) {
        this.musicService = musicService;
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        this.notificationManager = (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showPlay(Music music) {
        if (music == null) return;
        //将服务置于启动状态
        musicService.startForeground(NOTIFICATION_ID, buildNotification(musicService, music, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification buildNotification(Context context, Music music, boolean isPlaying) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        //设定的通知渠道名称
        String channelName = context.getString(R.string.channel_name);
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_HIGH;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        notificationManager.createNotificationChannel(channel);
        return new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.film) //设置通知图标
                .setContentTitle(music.getTitle())//设置通知标题
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();//设置处于运行状态
    }
}
