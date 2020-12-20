package com.fr1014.mycoludmusic.musicmanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fr1014.mycoludmusic.musicmanager.constants.Actions;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;

import io.reactivex.disposables.CompositeDisposable;

public class PlayService extends Service {
    private static final String TAG = "Service";
    private CompositeDisposable mCompositeDisposable;

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        CoverLoadUtils.get().init();
        AudioPlayer.get().init(this);
        MediaSessionManager.get().init(this);
        Notifier.get().init(this);
        QuitTimer.get().init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (Actions.ACTION_STOP.equals(intent.getAction())) {
                stop();
            }
        }
        return START_NOT_STICKY;
    }

    private void stop() {
        AudioPlayer.get().stopPlayer();
        QuitTimer.get().stop();
        Notifier.get().cancelAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MediaPlayer mediaPlayer = AudioPlayer.get().getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (AudioPlayer.get().mCompositeDisposable != null){
            AudioPlayer.get().mCompositeDisposable.clear();
        }
    }
}
