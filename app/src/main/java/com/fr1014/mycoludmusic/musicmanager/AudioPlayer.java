package com.fr1014.mycoludmusic.musicmanager;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.source.local.room.DBManager;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.musicmanager.receiver.NoisyAudioStreamReceiver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CollectionUtils;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 创建时间:2020/9/28
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class AudioPlayer implements LoadResultListener {
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;

    private Context context;
    private AudioFocusManager audioFocusManager;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private NoisyAudioStreamReceiver noisyReceiver;
    private IntentFilter noisyFilter;
    private List<Music> musicList = new ArrayList<>();
    private final List<OnPlayerEventListener> listeners = new ArrayList<>();
    private int state = STATE_IDLE;
    public CompositeDisposable mCompositeDisposable;
    DataRepository dataRepository;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public static AudioPlayer get() {
        return SingletonHolder.instance;
    }

    @Override
    public void coverLoading() {

    }

    @Override
    public void coverLoadSuccess(Bitmap coverLocal) {
        notifyShowPlay(getPlayMusic());
    }

    @Override
    public void coverLoadFail() {

    }

    private static class SingletonHolder {
        private static AudioPlayer instance = new AudioPlayer();
    }

    private AudioPlayer() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        CoverLoadUtils.get().registerLoadListener(this);
        musicList = DBManager.get().getMusicCurrent();
        audioFocusManager = new AudioFocusManager(context);
        mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        noisyReceiver = new NoisyAudioStreamReceiver();
        dataRepository = MyApplication.provideRepository();
        /*
         * 广播意图，是由于音频输出变化而导致音频即将变得“嘈杂”的应用程序的提示。
         * 例如，当拔掉有线耳机时，或当A2DP音频接收器断开，并且音频系统将要自动将音频路由切换到扬声器时，可以发送此意图。
         * 控制音频流的应用程序可能会在收到此意图后考虑暂停，减小音量或采取其他措施，以免使用户听到来自扬声器的音频而感到惊讶。
         */
        noisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        mediaPlayer.setOnCompletionListener(mp -> {
            playNext();
        });
        mediaPlayer.setOnPreparedListener(mp -> {
            if (isPreparing()) {
                startPlayer();
                Music music = getPlayMusic();
                resetMusicUrl(music);
                DBManager.get().insert(music, true);
            }
        });
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            for (OnPlayerEventListener listener : listeners) {
                listener.onBufferingUpdate(percent);
            }
        });
        //必写，不然不会拦截error，会到onCompletion中处理，导致逻辑问题
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
    }

    public void notifyShowPlay(Music music) {
        Notifier.get().showPlay(music);
    }

    public void addOnPlayEventListener(OnPlayerEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayerEventListener listener) {
        listeners.remove(listener);
    }

    public void addAndPlay(Music music) {
        int position = indexOf(music);
        if (position < 0) {
            musicList.add(music);
//            DBManager.get().insert(music,false);
            position = musicList.size() - 1;
        }
        play(position);
    }

    public void addAndPlay(List<Music> musics) {
        if (CollectionUtils.isEmptyList(musics)) return;
        if (!CollectionUtils.isEmptyList(musicList)) {
            musicList.clear();
        }
        musicList = musics;
        play(0);
//        for (Music music : musics){
//            DBManager.get().insert(music,false);
//        }
    }

    public void play(int position) {
        if (musicList.isEmpty()) {
            return;
        }

        setPlayPosition(position);
        Music music = getPlayMusic();
        notifyShowPlay(music);
        //网络歌曲，每次都需要重新获取url
        if (music.isOnlineMusic() && TextUtils.isEmpty(music.getSongUrl())) {
            getSongUrl(music);
            return;
        }
        play(music);
    }

    private void play(Music music) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getSongUrl());
            mediaPlayer.prepareAsync();
            for (OnPlayerEventListener listener : listeners) {
                listener.onChange(music);
            }
            state = STATE_PREPARING;
            MediaSessionManager.get().updateMetaData(music);
            MediaSessionManager.get().updatePlaybackState();
        } catch (IOException e) {
            CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
            e.printStackTrace();
        }
    }

    private void getSongInfo(Music music) {
        if (!TextUtils.isEmpty(music.getMUSICRID())) {//酷我的歌
            try {
                long mid = Long.parseLong(music.getMUSICRID().replace("MUSIC_", ""));
                addDisposable(dataRepository.getKWSongDetail(mid)
                        .compose(RxSchedulers.apply())
                        .subscribe(new Consumer<KWSongDetailEntity>() {
                            @Override
                            public void accept(KWSongDetailEntity kwSongDetailEntity) throws Exception {
                                music.setImgUrl(kwSongDetailEntity.getData().getAlbumpic());
                                music.setDuration(CommonUtil.stringToDuration(kwSongDetailEntity.getData().getSongTimeMinutes()));
                                CoverLoadUtils.get().loadRemoteCover(music);
                            }
                        }));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (music.getId() != 0) {//网易的歌
            addDisposable(dataRepository.getSongDetail(music.getId())
                    .map(songDetailEntity -> {
                        if (songDetailEntity.getSongs() != null && songDetailEntity.getSongs().size() > 0) {
                            music.setImgUrl(songDetailEntity.getSongs().get(0).getAl().getPicUrl());
                            music.setDuration(songDetailEntity.getSongs().get(0).getDt());
                        }
                        return music;
                    })
                    .compose(RxSchedulers.apply())
                    .subscribe(new Consumer<Music>() {
                        @Override
                        public void accept(Music music) throws Exception {
                            CoverLoadUtils.get().loadRemoteCover(music);
                        }
                    }));
        }
    }

    private void getSongUrl(Music music) {
        if (!TextUtils.isEmpty(music.getMUSICRID())) {//酷我的歌
            addDisposable(dataRepository.getKWSongUrl(music.getMUSICRID())
                    .compose(RxSchedulers.apply())
                    .subscribe(responseBody -> {
                        music.setSongUrl(responseBody.string());
                        play(music);
                    }));
        } else if (music.getId() != 0) {//网易的歌
            addDisposable(dataRepository.getWYSongUrl(music.getId())
                    .compose(RxSchedulers.apply())
                    .subscribe(songUrlEntity -> {
                        music.setSongUrl(songUrlEntity.getData().get(0).getUrl());
                        play(music);
                    }));
        }
        if (TextUtils.isEmpty(music.getImgUrl())) {
            getSongInfo(music);
        }else {
            CoverLoadUtils.get().loadRemoteCover(music);
        }
    }

    public void delete(int position) {
        int playPosition = getPlayPosition();
        musicList.remove(position);
//        Music music = musicList.remove(position);
//        DBManager.get().delete(music);
        if (playPosition > position) {
            setPlayPosition(playPosition - 1);
        } else if (playPosition == position) {
            if (isPlaying() || isPreparing()) {
                setPlayPosition(playPosition - 1);
                playNext();
            } else {
                stopPlayer();
                for (OnPlayerEventListener listener : listeners) {
                    listener.onChange(getPlayMusic());
                }
            }
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

    public void startPlayer() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            mediaPlayer.start();
            state = STATE_PLAYING;
            handler.post(mPublishRunnable);
            notifyShowPlay(getPlayMusic());
            MediaSessionManager.get().updatePlaybackState();
            context.registerReceiver(noisyReceiver, noisyFilter);

            for (OnPlayerEventListener listener : listeners) {
                listener.onPlayerStart();
            }
        }
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
        Notifier.get().showPause(getPlayMusic());
        MediaSessionManager.get().updatePlaybackState();
        context.unregisterReceiver(noisyReceiver);
        if (abandonAudioFocus) {
            audioFocusManager.abandonAudioFocus();
        }

        for (OnPlayerEventListener listener : listeners) {
            listener.onPlayerPause();
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

    public int playNext() {
//        stopPlayer();
        int nextPosition = -1;
        if (musicList.isEmpty()) {
            play(nextPosition);
            return nextPosition;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                nextPosition = new Random().nextInt(musicList.size());
                break;
            case SINGLE:
                nextPosition = getPlayPosition();
                break;
            case LOOP:
            default:
                nextPosition = getPlayPosition() + 1;
                break;
        }
        nextPosition = checkPosition(nextPosition);
        play(nextPosition);
        return nextPosition;
    }

    public int playPre() {
//        stopPlayer();
        int prePosition = -1;
        if (musicList.isEmpty()) {
            play(prePosition);
            return prePosition;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                prePosition = new Random().nextInt(musicList.size());
                break;
            case SINGLE:
                prePosition = getPlayPosition();
                break;
            case LOOP:
            default:
                prePosition = getPlayPosition() - 1;
                break;
        }
        prePosition = checkPosition(prePosition);
        play(prePosition);
        return prePosition;
    }

    private int checkPosition(int position) {
        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }
        return position;
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec);
            MediaSessionManager.get().updatePlaybackState();
            for (OnPlayerEventListener listener : listeners) {
                listener.onPublish(msec);
            }
        }
    }

    private final Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayerEventListener listener : listeners) {
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
            }
            handler.postDelayed(this, TIME_UPDATE);
        }
    };

    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    public long getAudioPosition() {
        if (isPlaying() || isPausing()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public Music getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        return musicList.get(getPlayPosition());
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

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

    public int getPlayPosition() {
        int position = Preferences.getPlayPosition();
        if (position < 0 || position >= musicList.size()) {
            position = 0;
            Preferences.savePlayPosition(position);
        }
        return position;
    }

    private void setPlayPosition(int position) {
        Preferences.savePlayPosition(position);
    }

    private void resetMusicUrl(Music music) {
        String url = music.getSongUrl();
        if (url.contains("http")) {
            music.setSongUrl("");
        }
    }

    private int indexOf(Music music) {
        if (CollectionUtils.isEmptyList(musicList)) return -1;
        for (int index = 0; index < musicList.size(); index++) {
            Music m = musicList.get(index);
            if (TextUtils.equals(m.getArtist(), music.getArtist()) && TextUtils.equals(m.getTitle(), music.getTitle())) {
                return index;
            }
        }
        return -1;
    }
}
