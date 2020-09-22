package com.fr1014.musicmanager;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private static final int PROGRESS_CHANGE = 1;
    private MediaPlayer player;
    private MyHandler handler = null;
    private List<Music> playingMusicList; //播放的音乐的集合
    private List<OnStateChangeListener> listenerList;
    private List<MyHandler.OnProgressChangeListener> progressChangeListeners = null;
    private Music currentMusic; //当前就绪的音乐
    private AudioManager audioManager;  //获取焦点之后是否自动播放
    private boolean isNeedReload;  //播放时是否需要重新加载
    private int playMode;  //播放模式
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_ORDER = 1;
    public static final int TYPE_RANDOM = 2;

    private Timer timer;


    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayList();   //初始化播放列表
        listenerList = new ArrayList<>();  //初始化监听器列表
        progressChangeListeners = new ArrayList<>(); //初始化进度监听器列表
        player = new MediaPlayer();
        player.setOnCompletionListener(onCompletionListener);
        handler = new MyHandler(player, progressChangeListeners);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE); //获取音频管理服务
    }

    public void addTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (player == null) return;
                    int duration = player.getDuration(); //获取歌曲的总长度
                    //获取播放进度
//                    int currentDuration =
                }
            };
        }
    }

    public interface OnStateChangeListener {
        void onPlayProgressChange(long played, long duration);

        void onPlay(Music item); //播放状态变为播放时

        void onPause(); //播放状态变为暂停时

        void onNotify(Music item, boolean canPlay);
    }

    private static final String TAG = "MusicService";

    public class MusicControl extends Binder {

        //注册监听器
        public void registerOnStateChangeListener(OnStateChangeListener listener) {
            listenerList.add(listener);
        }

        //注销监听器
        public void unregisterOnStateChangeListener(OnStateChangeListener listener) {
            listenerList.remove(listener);
        }

        //注册播放进度监听器
        public void registerOnProgressChangeListener(MyHandler.OnProgressChangeListener listener) {
            progressChangeListeners.add(listener);
        }

        //注销播放进度监听器
        public void unregisterOnProgressChangeListener(MyHandler.OnProgressChangeListener listener) {
            progressChangeListeners.remove(listener);
        }

        //添加一首歌曲
        public void addPlayList(Music item) {
            addPlayListInner(item);
        }

        //添加全部歌曲到列表并清空之前的列表
        public void addPlayList(List<Music> musics) {
            addPlayListInner(musics);
        }

        //获取当前正在播放的音乐
        public Music getCurrentMusic() {
            return getCurrentMusicInner();
        }

        //获取音乐播放列表
        public List<Music> getPlayList() {
            return getPlayListInner();
        }

        //获取播放器播放状态
        public boolean isPlaying() {
            return getPlayingInner();
        }

        //播放或暂停
        public void playOrPause() {
            if (player.isPlaying()) {
                pauseInner();
            } else {
                playInner();
            }
        }

        //继续播放
        public void continuePlay() {
            player.start();
        }

        //设置音乐的播放位置
        public void seekTo(int progress) {
            player.seekTo(progress);
        }

        //下一首
        public void playNext() {
            playNextInner();
        }

        //上一首
        public void playPre() {
            playPreInner();
        }

        public int getPlayMode() {
            return getPlayModeInner();
        }

        public void setPlayMode(int mode) {
            setPlayModeInner(mode);
        }
    }

    private void addPlayListInner(Music item) {
        if (!playingMusicList.contains(item)) {
            playingMusicList.add(0, item);
        }
        for (Music music : playingMusicList) {
            Log.d(TAG, "----addPlayListInner: " + music.toString());
        }
        currentMusic = item;
        isNeedReload = true;
        playInner();
    }

    private void addPlayListInner(List<Music> musics) {
        if (musics != null && musics.size() > 0) {
            playingMusicList.clear();
            playingMusicList.addAll(musics);
            currentMusic = null;
            playInner();
        }
    }

    private void playInner() {
        //获取音乐焦点
        audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //如果之前未选定要播放的音乐，就选择列表中的第一首音乐
        if (currentMusic == null && playingMusicList.size() > 0) {
            currentMusic = playingMusicList.get(0);
            isNeedReload = true;
        }
        playMusicItem(currentMusic, isNeedReload);
    }

    private void playMusicItem(Music item, boolean reload) {
        if (item == null) return;
        if (reload) {
            if (item.getSongUrl() == null) {
                for (OnStateChangeListener listener : listenerList) {
                    listener.onNotify(item, false);
                }
                return;
            } else {
                //需要重新加载音乐
                prepareToPlay(item);
            }
        }
        //如果无需重新加载，就是被暂停的歌曲
        if (!reload) {
            player.start();
            isNeedReload = true;
            for (OnStateChangeListener listener : listenerList) {
                listener.onPlay(currentMusic);
            }
        }
        //异步准备音乐已完成
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                for (OnStateChangeListener listener : listenerList) {
                    listener.onPlay(currentMusic);
                }
                //移除现有的更新消息，重新启动更新
                handler.removeMessages(PROGRESS_CHANGE);
                handler.sendEmptyMessage(PROGRESS_CHANGE);
            }
        });
    }

    //将要播放的音乐载入MediaPlay（并不是播放）
    private void prepareToPlay(Music item) {
        try {
            player.reset();
            //音乐的播放地址
//            player.setDataSource(getApplicationContext(), Uri.parse(item.getSongUrl()));
            player.setDataSource(item.getSongUrl());
            //异步准备播放音乐
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放上一首
    private void playPreInner() {
        int currentIndex = playingMusicList.indexOf(currentMusic);
        if (currentIndex - 1 >= 0) {
            currentMusic = playingMusicList.get(currentIndex - 1);
            isNeedReload = true;
            playInner();
        }
    }

    //播放下一首
    private void playNextInner() {
        //随机播放
        if (playMode == TYPE_RANDOM) {
            int index = new Random().nextInt(playingMusicList.size());
            currentMusic = playingMusicList.get(index);
        } else {
            //列表循环
            int currentIndex = playingMusicList.indexOf(currentMusic);
            if (currentIndex < playingMusicList.size() - 1) {
                currentMusic = playingMusicList.get(currentIndex + 1);
            } else {
                currentMusic = playingMusicList.get(0);
            }
        }
        isNeedReload = true;
        playInner();
    }

    private void pauseInner() {
        player.pause();
        for (OnStateChangeListener listener : listenerList) {
            listener.onPause();
        }
        isNeedReload = false; //暂停后不需要重新加载
    }

    private void seekToInner(int pos) {
        //将音乐拖动到指定时间
        player.seekTo(pos);
    }

    private int getPlayModeInner() {
        return playMode;
    }

    //设置播放模式
    private void setPlayModeInner(int mode) {
        this.playMode = mode;
    }

    //获取播放列表
    private List<Music> getPlayListInner() {
        return playingMusicList;
    }

    //获取当前就绪的音乐
    private Music getCurrentMusicInner() {
        return currentMusic;
    }

    private boolean getPlayingInner() {
        return player.isPlaying();
    }

    //初始化播放列表
    private void initPlayList() {
        playingMusicList = new ArrayList<>();
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //单曲循环后继续播放同样歌曲
            if (playMode == TYPE_SINGLE) {
                playInner();
                isNeedReload = true;
            } else {
                playNextInner();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new MusicControl();
    }

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player == null) {
            return;
        }
        if (player.isLooping()) {
            player.stop(); //停止播放音乐
        }
        player.release(); //播放资源
        player = null;
        playingMusicList.clear();
        listenerList.clear();
        audioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    public static class MyHandler extends Handler {
        private WeakReference<MediaPlayer> player;
        private WeakReference<List<OnProgressChangeListener>> onProgressChangeListeners;

        public MyHandler(MediaPlayer player, List<OnProgressChangeListener> onProgressChangeListeners) {
            this.player = new WeakReference<>(player);
            this.onProgressChangeListeners = new WeakReference<>(onProgressChangeListeners);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case PROGRESS_CHANGE:
                    MediaPlayer mediaPlayer = player.get();
                    //通知监听者当前的播放进度
                    long played = mediaPlayer.getCurrentPosition();
                    long duration = mediaPlayer.getDuration();
                    for (OnProgressChangeListener l : onProgressChangeListeners.get()) {
                        l.onPlayProgressChange(played, duration);
                    }
                    //间隔一秒发送一次更新播放进度的消息
                    sendEmptyMessageDelayed(PROGRESS_CHANGE, 1000);
                    break;
            }
        }

        public interface OnProgressChangeListener {
            void onPlayProgressChange(long played, long duration); //播放进度变化
        }
    }

}
