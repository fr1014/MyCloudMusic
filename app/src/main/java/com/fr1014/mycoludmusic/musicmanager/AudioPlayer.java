package com.fr1014.mycoludmusic.musicmanager;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.fr1014.mycoludmusic.data.source.local.preference.Preferences;
import com.fr1014.mycoludmusic.musicmanager.receiver.NoisyAudioStreamReceiver;
import com.fr1014.mycoludmusic.utils.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创建时间:2020/9/28
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class AudioPlayer {
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;

    private Context context;
    private List<Music> musicList;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private AudioFocusManager audioFocusManager;
    // TODO: 2020/9/29
    private NoisyAudioStreamReceiver noisyReceiver;
    private IntentFilter noisyFilter;
    private List<OnPlayEventListener> listeners = new ArrayList<>();

    private int state = STATE_IDLE;

    private AudioPlayer() {

    }

    public static AudioPlayer get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioPlayer instance = new AudioPlayer();
    }

    public void init(Context context) {
        this.context = context;
        this.musicList = new ArrayList<>();
        this.mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        audioFocusManager = new AudioFocusManager(context);

        mediaPlayer.setOnCompletionListener(mp -> {

        });

        mediaPlayer.setOnPreparedListener(mp -> {
            if (isPreparing()) {
                startPlayer();
            }
        });

        mediaPlayer.setOnErrorListener((mp, what, extra) -> true);
    }

    public void addOnPlayEventListener(OnPlayEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayEventListener listener) {
        listeners.remove(listener);
    }

    public void addAndPlay(Music music) {
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            position = musicList.size() - 1;
        }
        play(position);
    }

    public void play(int position) {
        if (musicList.isEmpty()) {
            return;
        }
        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }

        //保存播放位置
        setPlayPosition(position);
        Music music = getPlayMusic();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getSongUrl());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            for (OnPlayEventListener listener : listeners) {
                listener.onChange(music);
            }
            //通知栏
            Notifier.getInstance().showPlay(music);
            // TODO: 2020/9/28 耳机控制
        } catch (IOException e) {
            e.printStackTrace();
            CommonUtil.toastShort("当前歌曲无法播放");
        }
    }

    public void next() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() + 1);
                break;
        }
    }

    public void prev() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() - 1);
                break;
        }
    }

    public void playPause() {
        if (isPreparing()) {
            stopPlayer();
        } else if (isPlaying()) {
            pausePlayer();
        } else if (isPausing()) {
            startPlayer();
        } else {
            play(getPlayPosition());
        }
    }

    public void stopPlayer() {
        if (isIdle()) {
            return;
        }

        pausePlayer();
        mediaPlayer.reset();
        state = STATE_IDLE;
    }

    public long getAudioPosition() {
        if (isPlaying() || isPausing()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Music getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        return musicList.get(getPlayPosition());
    }

    public int getPlayPosition() {
        return Preferences.getPlayPosition();
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    private void setPlayPosition(int position) {
        Preferences.savePlayPosition(position);
    }

    public void pausePlayer() {
        pausePlayer(true);
    }

    public void pausePlayer(boolean abandonAudioFocus) {
        if (!isPlaying()) {
            return;
        }
        mediaPlayer.pause();
        state = STATE_PAUSE;
        handler.removeCallbacks(mPublishRunnable);
        // TODO: 2020/9/29 通知栏暂停
        // TODO: 2020/9/29 MediaSessionManager
//        context.unregisterReceiver();
        if (abandonAudioFocus) {
            audioFocusManager.abandonAudioFocus();
        }
        for (OnPlayEventListener listener : listeners) {
            listener.onPlayerPause();
        }
    }

    public void startPlayer() {
        if (!isPreparing() && !isPausing()) {
            return;
        }
        if (audioFocusManager.requestAudioFocus()) {
            mediaPlayer.start();
            state = STATE_PLAYING;
            handler.post(mPublishRunnable);
        }
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayEventListener listener : listeners) {
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, TIME_UPDATE);
            }
        }
    };

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    public boolean isPausing() {
        return state == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return state == STATE_PREPARING;
    }

    public boolean isIdle() {
        return state == STATE_IDLE;
    }
}
