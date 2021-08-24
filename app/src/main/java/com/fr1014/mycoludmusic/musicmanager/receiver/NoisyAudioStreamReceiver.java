package com.fr1014.mycoludmusic.musicmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay;

/**
 * 来电/耳机拔出时暂停播放
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyAudioPlay.get().playOrPause();
        AudioPlayer.get().playPause();
    }
}