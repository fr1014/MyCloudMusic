package com.fr1014.mycoludmusic.musicmanager.player;

import android.content.Context;
import android.media.AudioManager;

import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;

import static android.content.Context.AUDIO_SERVICE;

/**
 * 创建时间:2020/9/28
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {

    private AudioManager audioManager;
    private boolean isPausedByFocusLossTransient;

    public AudioFocusManager(Context context) {
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    public boolean requestAudioFocus() {
        return audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void abandonAudioFocus() {
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            // 重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN:
                if (isPausedByFocusLossTransient) {
                    // 通话结束，恢复播放
                    MyAudioPlay.get().startPlayer();
                    AudioPlayer.get().startPlayer();
                }

                // 恢复音量
                MyAudioPlay.get().mediaPlayer.setVolume(1f, 1f);
                AudioPlayer.get().getMediaPlayer().setVolume(1f, 1f);

                isPausedByFocusLossTransient = false;
                break;
            // 永久丢失焦点，如被其他播放器抢占
            case AudioManager.AUDIOFOCUS_LOSS:
                MyAudioPlay.get().pausePlayer();
                AudioPlayer.get().pausePlayer();
                break;
            // 短暂丢失焦点，如来电
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                MyAudioPlay.get().pausePlayer(false);
                AudioPlayer.get().pausePlayer(false);
                isPausedByFocusLossTransient = true;
                break;
            // 瞬间丢失焦点，如通知
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 音量减小为一半
                MyAudioPlay.get().mediaPlayer.setVolume(0.5f, 0.5f);
                AudioPlayer.get().getMediaPlayer().setVolume(0.5f, 0.5f);
                break;
        }
    }
}
