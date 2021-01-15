package com.fr1014.mycoludmusic.ui.home.userinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.LevelData
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Playlist
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel

/**
 * Create by fanrui on 2021/1/14
 * Describe:
 */
class UserInfoViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {
    private val playlistWYLive: MutableLiveData<List<Playlist>> by lazy {
        MutableLiveData()
    }

    private val levelInfoLive: MutableLiveData<LevelData> by lazy {
        MutableLiveData()
    }

    fun getLevelInfo(): LiveData<LevelData> {
        return levelInfoLive
    }

    fun getWYPlayList(): LiveData<List<Playlist>> {
        return playlistWYLive
    }

    fun getWYUserPlayList() {
        addSubscribe(
                Preferences.getUserProfile()?.userId?.toLong()?.let { it ->
                    model.getWYUserPlayList(it)
                            .compose(RxSchedulers.apply())
                            .subscribe {
                                playlistWYLive.postValue(it.playlist)
                            }
                }
        )
    }

    fun getWYLevelInfo() {
        addSubscribe(
                model.wyLevelInfo
                        .compose(RxSchedulers.apply())
                        .subscribe {
                            levelInfoLive.postValue(it.data)
                        }
        )
    }
}