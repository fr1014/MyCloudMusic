package com.fr1014.mycoludmusic.musicmanager.player;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.fr1014.mycoludmusic.utils.FileUtils;

/**
 * MediaSession可以实现耳机来控制播放
 * https://juejin.cn/post/6844903575814930439
 */
public class MediaSessionManager {
    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private PlayService playService;
    private MediaSessionCompat mediaSession;

    public static MediaSessionManager get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static MediaSessionManager instance = new MediaSessionManager();
    }

    private MediaSessionManager() {
    }

    public void init(PlayService playService) {
        this.playService = playService;
        setupMediaSession();
    }

    private void setupMediaSession() {
        mediaSession = new MediaSessionCompat(playService, TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mediaSession.setCallback(callback);
        mediaSession.setActive(true);
    }

    public void updatePlaybackState() {
        int state = (MyAudioPlay.get().isPlaying() || MyAudioPlay.get().isPreparing()) ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        mediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, MyAudioPlay.get().getAudioPosition(), 1)
                        .build());
    }

    public void updateMetaData(Music music) {
        if (music == null) {
            mediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, FileUtils.getCoverLocal("MediaSessionManager", music));

//        metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AppCache.get().getLocalMusicList().size());

        mediaSession.setMetadata(metaData.build());
    }

    private final MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            MyAudioPlay.get().playOrPause();
        }

        @Override
        public void onPause() {
            MyAudioPlay.get().playOrPause();
        }

        @Override
        public void onSkipToNext() {
            MyAudioPlay.get().playNext();
        }

        @Override
        public void onSkipToPrevious() {
            MyAudioPlay.get().playPre();
        }

        @Override
        public void onStop() {
            MyAudioPlay.get().stopPlayer();
        }

        @Override
        public void onSeekTo(long pos) {
            MyAudioPlay.get().seekTo((int) pos);
        }
    };
}
