package com.fr1014.mycoludmusic.musicmanager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.fr1014.mycoludmusic.constants.Actions;

public class PlayService extends Service {
    public PlayService() {
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AudioPlayer.get().init(this);
        // TODO: 2020/9/29 MediaSession
//        Notifier.getInstance().init(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Actions.ACTION_STOP:
                    stop();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void stop() {
        AudioPlayer.get().stopPlayer();
//        QuitTimer.get().stop();
//        Notifier.getInstance().cancelAll();
    }
}
