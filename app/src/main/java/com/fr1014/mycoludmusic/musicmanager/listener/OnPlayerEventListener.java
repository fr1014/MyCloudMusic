package com.fr1014.mycoludmusic.musicmanager.listener;

import com.fr1014.mycoludmusic.musicmanager.Music;

/**
 *  播放进度监听器
 */
public interface OnPlayerEventListener {
    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 歌曲播放完成
     */
    void onPlayerComplete();

    /**
     * 继续播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
}
