package com.fr1014.mycoludmusic.musicmanager.player

import android.content.Context
import android.media.MediaPlayer
import com.fr1014.mycoludmusic.app.AppCache
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.musicmanager.constants.Actions
import com.fr1014.mycoludmusic.utils.CollectionUtils

/**
 * Time: 2021/8/2
 * Author: fanrui07
 * Description: 音频播放管理
 */
private const val STATE_IDLE = 0;
private const val STATE_PREPARING = 1;
private const val STATE_PLAYING = 2;
private const val STATE_PAUSE = 3;

open class CommonAudioPlayer : MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    protected lateinit var applicationContext: Context
    private var state: Int = STATE_IDLE
    lateinit var mediaPlayer: MediaPlayer

    open fun init(context: Context) {
        applicationContext = context.applicationContext
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnBufferingUpdateListener(this)
    }

    open fun getCurrentPlayPosition() = MusicListManageUtils.get().getCurrentPlayPosition()

    fun getCurrentMusicList() = MusicListManageUtils.get().getCurrentMusicList()

    fun getCurrentMusic() = MusicListManageUtils.get().currentMusic

    private fun setCurrentMusic(music: Music) {
        MusicListManageUtils.get().currentMusic = music
    }

    private fun addMusic(music: Music) = MusicListManageUtils.get().addMusic(music)

    private fun addMusicList(musics: List<Music>) = MusicListManageUtils.get().addMusicList(musics)

    /**
     * 播放全部，需clear之前的播放队列
     */
    open fun initPlayList(musics: List<Music>): CommonAudioPlayer {
        addMusicList(musics)
        return this
    }

    /**
     * 添加一首歌到播放队列并播放
     */
    open fun addPlayMusic(music: Music) {
        val currentMusicList = getCurrentMusicList()
        val indexOf = addMusic(music)
        if (indexOf < 0) {
            play(currentMusicList[0])
        } else {
            val position = music.indexOf(currentMusicList)
            play(currentMusicList[position])
        }
    }

    /**
     * 播放队列中的第一首
     */
    fun play() {
        val musicList = getCurrentMusicList()
        if (musicList.size > 0) play(musicList[0])
    }

    open fun play(music: Music) {
        try {
            mediaPlayer.reset()
            setCurrentMusic(music)
            mediaPlayer.setDataSource(music.songUrl)
            mediaPlayer.prepareAsync()
            state = STATE_PREPARING
        } catch (e: Exception) {
            throw Exception()
        }
    }

    fun playOrPause() {
        when {
            isPlaying() -> {
                pausePlayer()
            }
            isPausing() -> {
                startPlayer()
            }
            else -> {
                getCurrentMusic()?.let { play(it) }
            }
        }
    }

    open fun startPlayer() {
        mediaPlayer.start()
        state = STATE_PLAYING
    }

    open fun pausePlayer() {
        mediaPlayer.pause()
        state = STATE_PAUSE
    }

    fun stopPlayer() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        state = STATE_IDLE
    }

    open fun playNext(): Music? {
        val musicList = getCurrentMusicList()
        val music = if (CollectionUtils.isEmptyList(musicList)) getCurrentMusic() else musicList[checkPlayPosition(getCurrentPlayPosition() + 1)]
        music?.let { play(music) }
        return music
    }

    open fun playPre(): Music? {
        val musicList = getCurrentMusicList()
        val music = if (CollectionUtils.isEmptyList(musicList)) getCurrentMusic() else musicList[checkPlayPosition(getCurrentPlayPosition() - 1)]
        music?.let { play(music) }
        return music
    }

    private fun checkPlayPosition(position: Int): Int {
        val musicList = getCurrentMusicList()
        if (position >= musicList.size) {
            return 0;
        } else if (position < 0) {
            return musicList.size - 1
        }
        return position
    }

    override fun onPrepared(mp: MediaPlayer?) {
        startPlayer()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean = true

    override fun onCompletion(mp: MediaPlayer?) {
        if (getPlayMode() == PlayModeEnum.SINGLE) { //单曲循环
            getCurrentMusic()?.let { music ->
                play(music)
            }
            return
        }
        playNext()
    }

    open fun getAudioPosition(): Long {
        return if (isPlaying() || isPausing()) mediaPlayer.currentPosition.toLong() else 0
    }

    fun getPlayMode(): PlayModeEnum = PlayModeEnum.valueOf(Preferences.getPlayMode())

    open fun switchPlayMode(): PlayModeEnum {
        var playMode = getPlayMode()
        playMode = when (playMode) {
            PlayModeEnum.SINGLE -> PlayModeEnum.LOOP
            PlayModeEnum.LOOP -> PlayModeEnum.SHUFFLE
            else -> PlayModeEnum.SINGLE
        }
        Preferences.savePlayMode(playMode.value())
        // todo change musicList
        return playMode
    }

    open fun isPlaying(): Boolean {
        return state == STATE_PLAYING
    }

    open fun isPausing(): Boolean {
        return state == STATE_PAUSE
    }

    open fun isPreparing(): Boolean {
        return state == STATE_PREPARING
    }

    open fun isIdle(): Boolean {
        return state == STATE_IDLE
    }

    open fun quit() {
        mediaPlayer.release()
        AppCache.get().clearStack()
        PlayService.startCommand(applicationContext, Actions.ACTION_STOP)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }
}
