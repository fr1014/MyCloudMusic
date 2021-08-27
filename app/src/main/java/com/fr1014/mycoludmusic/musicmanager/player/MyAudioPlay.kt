package com.fr1014.mycoludmusic.musicmanager.player

import android.content.Context
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.musicmanager.receiver.NoisyAudioStreamReceiver
import com.fr1014.mycoludmusic.utils.CommonUtils
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

/**
 * Time: 2021/8/2
 * Author: fanrui07
 * Description:
 */
private const val TIME_UPDATE: Long = 300L

class MyAudioPlay private constructor() : CommonAudioPlayer() {
    private lateinit var noisyAudioStreamReceiver: NoisyAudioStreamReceiver // 来电/耳机拔出时广播
    private lateinit var noisyFilter: IntentFilter
    private lateinit var audioFocusManager: AudioFocusManager               // 音频焦点管理
    private var abandonAudioFocus: Boolean = true
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private val dataRepository: DataRepository = MyApplication.provideRepository()
    private var toast: Toast? = null
    private var newMusicAdded = false
    private var newMusicAddedPlayPosition: Int = -1

    private lateinit var handler: Handler
    /*
     * 在定义变量时，加上 by lazy 操作符，当变量第一次使用时会执行 lambda 方法块里的代码为变量初始化值，
     * 再次使用该变量时，则会使用上一次赋的值。
     */
    private val mPublishRunnable: Runnable by lazy {
        object : Runnable {
            override fun run() {
                if (isPlaying()) {
                    EventBus.getDefault().post(PlayerEvent(PlayerType.OnPublish, mediaPlayer.currentPosition))
                }
                handler.postDelayed(this, TIME_UPDATE)
            }
        }
    }

    companion object {
        @JvmStatic
        fun get() = SingletonHelper.instance
    }

    private object SingletonHelper {
        val instance = MyAudioPlay()
    }

    override fun init(context: Context) {
        super.init(context)
        handler = Handler(Looper.getMainLooper())
        noisyAudioStreamReceiver = NoisyAudioStreamReceiver()
        //当拔掉有线耳机时，或当A2DP音频接收器断开，并且音频系统将要自动将音频路由切换到扬声器时，可以发送此Intent
        noisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        audioFocusManager = AudioFocusManager(applicationContext)
    }

    private fun notifyShowPlay(music: Music?) {
        Notifier.get().showPlay(music)
    }

    override fun getCurrentPlayPosition(): Int {
        return if (newMusicAdded) {
            newMusicAdded = false
            newMusicAddedPlayPosition
        } else {
            super.getCurrentPlayPosition()
        }
    }

    /**
     * 首次进入APP 或 Service未杀死再次进入APP 时调用该方法
     */
    fun updateMusicList() {
        musicListChange()
    }

    override fun initPlayList(musics: List<Music>): CommonAudioPlayer {
        if (MusicListManageUtils.get().addMusicList(musics)) {
            musicListChange()
        }
        return this
    }

    override fun addPlayMusic(music: Music) {
        if (MusicListManageUtils.get().addMusic(music) < 0) {
            if (!newMusicAdded) {
                newMusicAdded = true
                getCurrentMusic()?.let {
                    newMusicAddedPlayPosition = it.indexOf(getCurrentMusicList())
                }
            }
            musicListChange()
        }
        super.addPlayMusic(music)
    }

    fun musicListChange() {
        EventBus.getDefault().post(PlayerEvent(MusicListManageUtils.get().getCurrentMusicList(), PlayerType.OnPlayListChange))
    }

    /**
     * 外部禁止直接调用该方法 - 使用addPlayMusic(music: Music)
     */
    override fun play(music: Music) {
        EventBus.getDefault().post(PlayerEvent(music, PlayerType.OnChange))
        notifyShowPlay(music)
        coroutineScope.launch {
            if (music.isOnlineMusic) {   //网络歌曲
                if (TextUtils.isEmpty(music.songUrl)) {
                    music.getSongUrl(dataRepository)
                }
                if (TextUtils.isEmpty(music.imgUrl) || music.duration == 0L) {
                    music.getSongInfo(dataRepository)
                }
                music.loadRemoteCover(COVER_FROM_COMMON)
            }
            try {
                super.play(music)
                MediaSessionManager.get().updateMetaData(music)
                MediaSessionManager.get().updatePlaybackState()
            } catch (e: Exception) {
                CommonUtils.toastShort("该歌曲暂时无法播放，已跳过该首歌曲" + e.message)
                playNext()
            }
            music.songUrl = ""         //供应商防止盗链过段时间会重制url，所以这里至空重新拉取url
            music.savePlayingMusic()
        }

    }

    override fun startPlayer() {
        EventBus.getDefault().post(PlayerEvent(PlayerType.OnPlayerStart))
        if (audioFocusManager.requestAudioFocus()) {
            super.startPlayer()
            handler.post(mPublishRunnable)
            notifyShowPlay(getCurrentMusic())
            MediaSessionManager.get().updatePlaybackState()
            try {
                applicationContext.registerReceiver(noisyAudioStreamReceiver, noisyFilter)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun pausePlayer(abandonAudioFocus: Boolean) {
        this.abandonAudioFocus = abandonAudioFocus
        pausePlayer()
        MediaSessionManager.get().updatePlaybackState()
    }

    override fun pausePlayer() {
        EventBus.getDefault().post(PlayerEvent(PlayerType.OnPlayerPause))
        super.pausePlayer()
        Notifier.get().showPause(getCurrentMusic())
        handler.removeCallbacks(mPublishRunnable)
        try {
            applicationContext.unregisterReceiver(noisyAudioStreamReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        if (abandonAudioFocus) audioFocusManager.abandonAudioFocus() else abandonAudioFocus = true
    }

    override fun switchPlayMode(): PlayModeEnum {
        val playMode = super.switchPlayMode()
        if (playMode != getPlayMode()){
            musicListChange()
        }
        toast?.cancel()
        toast = when (playMode) {
            PlayModeEnum.SINGLE -> CommonUtils.toastShort("单曲循环")
            PlayModeEnum.LOOP -> CommonUtils.toastShort("循环播放")
            PlayModeEnum.SHUFFLE -> CommonUtils.toastShort("随机播放")
        }
        return playMode
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    fun seekTo(msec: Int) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec)
            MediaSessionManager.get().updatePlaybackState()
            EventBus.getDefault().post(PlayerEvent(PlayerType.OnBufferingUpdate, msec))
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        EventBus.getDefault().post(PlayerEvent(PlayerType.OnPlayerComplete))
        super.onCompletion(mp)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        EventBus.getDefault().post(PlayerEvent(percent, PlayerType.OnBufferingUpdate))
        super.onBufferingUpdate(mp, percent)
    }

    override fun quit() {
        coroutineScope.cancel()
        super.quit()
    }
}