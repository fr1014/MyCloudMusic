package com.fr1014.mycoludmusic.musicmanager.player

import android.graphics.BitmapFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.FileUtils
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesConst
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Time: 2021/8/9
 * Author: fanrui07
 * Description: Music播放队列维护Utils
 */
class MusicListManageUtils private constructor() {
    //维护的音乐播放列表, 外部获取musicList需使用MusicManageUtils.get().getCurrentMusicList()
    var loopMusicList: LinkedList<Music> = LinkedList(getMusicList(SharedPreferencesConst.MUSIC_LIST, SharedPreferencesConst.MUSIC_LIST_KEY))
    var shuffleMusicList: LinkedList<Music> = LinkedList(getMusicList(SharedPreferencesConst.SHUFFLE_MUSIC_LIST, SharedPreferencesConst.SHUFFLE_MUSIC_LIST_KEY))
    var currentMusic = getMusic(SharedPreferencesConst.CURRENT_MUSIC, SharedPreferencesConst.CURRENT_MUSIC_KEY)
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private val dataRepository: DataRepository = MyApplication.provideRepository()
    private var playPosition: Int = -1   //当前播放歌曲在musicList中的index

    companion object {
        @JvmStatic
        fun get() = SingletonHolder.instance
    }

    /**
     * 当网易的付费歌曲从酷我拉取后Id会修改
     */
    fun changeMusicId(music: Music, toId: String) {
        val loopMusicIndex = music.indexOf(loopMusicList)
        val shuffleMusicIndex = music.indexOf(shuffleMusicList)
        if (loopMusicIndex > 0) {
            loopMusicList[loopMusicIndex].changeMusicSource(toId)
        }
        if (shuffleMusicIndex > 0) {
            shuffleMusicList[shuffleMusicIndex].changeMusicSource(toId)
        }
    }

    fun getCurrentPlayPosition(): Int {
        currentMusic?.let {
            playPosition = it.indexOf(getCurrentMusicList())
        }
        return playPosition
    }

    fun getCurrentMusicList(): LinkedList<Music> {
        if (Preferences.getPlayMode() == PlayModeEnum.SHUFFLE.value()) {
            return shuffleMusicList;
        }
        return loopMusicList;
    }

    /**
     * add Music
     *
     * @return true 当前播放队列不包含该music
     */
    fun addMusic(music: Music): Int {
        val indexOf = music.indexOf(loopMusicList)
        if (indexOf < 0) {
            loopMusicList.addFirst(music)
            shuffleMusicList.addFirst(music)
        }
        return indexOf
    }

    /**
     * add MusicList
     *
     * @return true 当前播放队列不包含该musicList
     */
    fun addMusicList(musicList: List<Music>): Boolean {
        if (!loopMusicList.containsAll(musicList)) {
            if (!CollectionUtils.isEmptyList(loopMusicList)) loopMusicList.clear()
            loopMusicList.addAll(musicList)
            if (!CollectionUtils.isEmptyList(shuffleMusicList)) shuffleMusicList.clear()
            shuffleMusicList.addAll(musicList.shuffle())
            return true
        }
        return false
    }

    fun saveMusicList() {
        setMusicList<Music>(SharedPreferencesConst.MUSIC_LIST, SharedPreferencesConst.MUSIC_LIST_KEY, loopMusicList)
        setMusicList<Music>(SharedPreferencesConst.SHUFFLE_MUSIC_LIST, SharedPreferencesConst.SHUFFLE_MUSIC_LIST_KEY, shuffleMusicList)
    }

    private fun <T> setMusicList(name: String, key: String, musicList: List<T>) {
        if (CollectionUtils.isEmptyList(musicList)) return
        val editor = SharedPreferencesUtil.getEditor(name)
        val strJson = Gson().toJson(musicList)
        editor.clear()
        editor.putString(key, strJson)
        editor.apply()
    }

    private fun getMusicList(name: String, key: String): List<Music> {
        val musicList = LinkedList<Music>()
        val strJson = SharedPreferencesUtil.getSharedPreferences(name).getString(key, null)
                ?: return musicList
        musicList.addAll(Gson().fromJson<List<Music>>(strJson, object : TypeToken<List<Music>>() {}.type))
        return musicList
    }

    fun putMusic(name: String, key: String, music: Music?) {
        if (music == null) return
        val editor = SharedPreferencesUtil.getEditor(name)
        //转换成json数据，再保存
        val strJson = Gson().toJson(music)
        editor.clear()
        editor.putString(key, strJson)
        editor.apply()
    }

    private fun getMusic(name: String, key: String): Music? {
        val strJson = SharedPreferencesUtil.getSharedPreferences(name).getString(key, null)
                ?: return null
        return Gson().fromJson(strJson, object : TypeToken<Music>() {}.type)
    }

    fun loadMusicCover(music: Music) {
        val coverLocal = FileUtils.getCoverLocal(music)
        if (coverLocal != null) {
            EventBus.getDefault().post(MusicCoverEvent(CoverStatusType.Success, music, coverLocal))
            return
        }
        coroutineScope.launch {
            loadMusicCoverRemote(music)
        }
    }

    private suspend fun loadMusicCoverRemote(music: Music) {
        EventBus.getDefault().post(MusicCoverEvent(CoverStatusType.Loading))
        withContext(Dispatchers.IO) {
            try {
                val responseBody = dataRepository.kkwApiService.getSongCover("${music.imgUrl}?param=700y700")
                val bitmap = BitmapFactory.decodeStream(responseBody.byteStream())
                FileUtils.saveCoverToLocal(bitmap, music)
            } catch (e: Exception) {
                EventBus.getDefault().post(MusicCoverEvent(CoverStatusType.Fail))
            }
        }
    }

    private object SingletonHolder {
        val instance = MusicListManageUtils()
    }
}