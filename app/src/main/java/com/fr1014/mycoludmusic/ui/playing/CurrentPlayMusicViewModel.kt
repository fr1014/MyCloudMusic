package com.fr1014.mycoludmusic.ui.playing

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.ExecuteOnceObserver
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.FileUtils
import com.fr1014.mymvvm.base.BusLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.NumberFormatException
import java.util.regex.Pattern

/**
 * Create by fanrui on 2021/1/23
 * Describe:
 */
class CurrentPlayMusicViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {
    private val songLrcPathLive: BusLiveData<String> by lazy {
        BusLiveData<String>()
    }

    private val likeSongResultLive: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getSongLrcPath(): LiveData<String> = songLrcPathLive
    fun getLikeSongResult(): LiveData<Boolean> = likeSongResultLive

    fun getLikeList(): LiveData<MutableList<MusicLike>> = model.likeIdsLive

    /**
     * 添加歌曲到指定的歌单
     */
    fun getLikeSong(like: Boolean, pid: Long, tracks: String, timestamp: String) {
        val op = if (like) "add" else "del"
        addSubscribe(model.getWYManagePlayList(op, pid, tracks, timestamp)
                .compose(RxSchedulers.apply())
                .subscribe({
                    try {
                        likeSongResultLive.postValue(like)
                        saveLikeMusicId(tracks.toLong(), like)
                    } catch (e: Exception) {
                    }
                }, {
                    if (like) {
                        CommonUtils.toastShort("添加收藏失败")
                    } else {
                        CommonUtils.toastShort("取消收藏失败")
                    }
                }));
    }

    private fun saveLikeMusicId(ids: Long, isLikeMusic: Boolean) {
        Observable.just(ids)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                    if (isLikeMusic) {
                        model.insert(MusicLike(ids))
                    } else {
                        val musicLike = model.getItemLive(ids)
                        model.delete(musicLike)
                    }
                }))
    }

    fun getSongLrc(music: Music) {
        val filePath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.artist, music.title)
        if (!FileUtils.isFileEmpty(filePath)) {
            songLrcPathLive.postValue(filePath)
            return
        }
        if (!TextUtils.isEmpty(music.musicrid)) {
            val mid = music.musicrid.replace("MUSIC_", "")
            addSubscribe(model.getKWSongInfoAndLrc(mid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map { kwSongInfoAndLrcEntity ->
                        val lrcList = kwSongInfoAndLrcEntity.data.lrclist
                        val content = StringBuilder()
                        //正则格式化KW服务器返回的歌词时间
                        val rgex = "\\d+\\.\\d{1,3}"
                        val pattern = Pattern.compile(rgex)
                        for (lrcListBean in lrcList) {
                            content.append("[")
                            val time = lrcListBean.time
                            val m = pattern.matcher(time)
                            if (m.matches()) {
                                content.append(CommonUtils.strFormatTime(m.group()))
                            } else {   //如果正则匹配失败，取.前和.后2位的字符
                                val index = time.indexOf(".")
                                val endIndex = index + 2
                                content.append(CommonUtils.strFormatTime(time.substring(0, endIndex)))
                            }
                            content.apply {
                                append("]")
                                append(lrcListBean.lineLyric.trim { it <= ' ' })
                                append("\n")
                            }
                        }
                        FileUtils.saveLrcFile(filePath, content.toString())
                        filePath
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ filePath -> songLrcPathLive.postValue(filePath) }, { songLrcPathLive.postValue("") })
            )
        } else {
            addSubscribe(model.getWYSongLrcEntity(music.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map { wySongLrcEntity ->
                        val lyric = wySongLrcEntity.lrc.lyric
                        FileUtils.saveLrcFile(filePath, lyric)
                        filePath
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ songLrcPathLive.postValue(filePath) }) { songLrcPathLive.postValue("") }
            )
        }
    }
}