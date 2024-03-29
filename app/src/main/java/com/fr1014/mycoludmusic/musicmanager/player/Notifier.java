package com.fr1014.mycoludmusic.musicmanager.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fr1014.mycoludmusic.MainActivity;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.musicmanager.receiver.StatusBarReceiver;
import com.fr1014.mycoludmusic.utils.FileUtils;

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
    private PlayService playService;
    private NotificationManager notificationManager;

    private Notifier() {

    }

    public static Notifier get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final Notifier instance = new Notifier();
    }

    public void init(PlayService musicService) {
        this.playService = musicService;
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        this.notificationManager = (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground(playService);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(Context context) {
        String NOTIFICATION_CHANNEL_ID = "com.fr1014.cloudmusic";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notifier)
                .setContentIntent(pendingIntent)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        playService.startForeground(2, notification);
    }

    public void showPlay(Music music) {
        if (music == null) return;
        //将服务置于启动状态
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music, true));
    }

    public void showPause(Music music) {
        if (music == null) {
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music, false));
    }

    public void cancelAll() {
        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, Music music, boolean isPlaying) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 适配 >= 8.0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //设定的通知渠道名称
            String channelName = context.getString(R.string.channel_name);
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            //是否允许震动
            channel.enableVibration(false);
            //设置通知无声音
            channel.setSound(null, null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifier) //设置通知图标
                .setContentTitle(music.getTitle())//设置通知标题
                .setContentIntent(pendingIntent)
                .setContent(getRemoteViews(context, music, isPlaying)) //设置普通notification视图
//                .setCustomBigContentView(getRemoteViews(context, music, isPlaying)) //设置显示bigView的notification视图
                .setOngoing(true) //true使notification变为ongoing,用户不能手动清除
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                .build();//设置处于运行状态
    }

    private RemoteViews getRemoteViews(Context context, Music music, boolean isPlaying) {
        String title = music.getTitle();
        String artist = music.getArtist();
        Bitmap cover;
        Bitmap coverLocal = FileUtils.getCoverLocal("Notifier", music);
        if (coverLocal == null) {
            cover = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        } else {
            cover = coverLocal;
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        remoteViews.setImageViewBitmap(R.id.iv_icon, cover);
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_author, artist);

        Intent backIntent = new Intent(context, StatusBarReceiver.class);
        backIntent.setAction(StatusBarReceiver.ACTION_STATUS_BAR);
        backIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_BACK);
        PendingIntent backPendingIntent = PendingIntent.getBroadcast(context, 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_back, R.drawable.ic_song_back_black);
        remoteViews.setOnClickPendingIntent(R.id.iv_back, backPendingIntent);

        Intent playIntent = new Intent(context, StatusBarReceiver.class);
        playIntent.setAction(StatusBarReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_PLAY_PAUSE);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_play_pause, isPlaying ? R.drawable.ic_stop_black : R.drawable.ic_play_black);
        remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, playPendingIntent);

        Intent nextIntent = new Intent(context, StatusBarReceiver.class);
        nextIntent.setAction(StatusBarReceiver.ACTION_STATUS_BAR);
        nextIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_next, R.drawable.ic_song_next_black);
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);

        Intent quitIntent = new Intent(context, StatusBarReceiver.class);
        quitIntent.setAction(StatusBarReceiver.ACTION_STATUS_BAR);
        quitIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_QUIT);
        PendingIntent quitPendingIntent = PendingIntent.getBroadcast(context, 3, quitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_quit, R.drawable.ic_del);
        remoteViews.setOnClickPendingIntent(R.id.iv_quit, quitPendingIntent);

        return remoteViews;
    }
}
