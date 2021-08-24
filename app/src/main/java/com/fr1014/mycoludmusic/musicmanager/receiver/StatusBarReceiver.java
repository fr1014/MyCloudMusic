package com.fr1014.mycoludmusic.musicmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay;

public class StatusBarReceiver extends BroadcastReceiver {

    public static final String ACTION_STATUS_BAR = "com.fr1014.music.STATUS_BAR_ACTIONS";
    public static final String EXTRA = "extra";
    public static final String EXTRA_BACK = "back";
    public static final String EXTRA_NEXT = "next";
    public static final String EXTRA_QUIT = "quit";
    public static final String EXTRA_PLAY_PAUSE = "play_pause";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra,EXTRA_BACK)){
            MyAudioPlay.get().playPre();
        } else if (TextUtils.equals(extra, EXTRA_NEXT)) {
            MyAudioPlay.get().playNext();
        } else if (TextUtils.equals(extra, EXTRA_PLAY_PAUSE)) {
            MyAudioPlay.get().playOrPause();
        }else if (TextUtils.equals(extra,EXTRA_QUIT)){
            MyAudioPlay.get().quit();
        }
    }
}
