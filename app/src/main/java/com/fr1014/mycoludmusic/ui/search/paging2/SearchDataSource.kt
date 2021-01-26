package com.fr1014.mycoludmusic.ui.search.paging2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fr1014.mycoludmusic.SourceHolder
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchDetail
import com.fr1014.mycoludmusic.http.KWServiceProvider
import com.fr1014.mycoludmusic.http.WYYServiceProvider
import com.fr1014.mycoludmusic.http.api.KWApiService
import com.fr1014.mycoludmusic.http.api.WYApiService
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.ExecuteOnceObserver
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.utils.CollectionUtils
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Create by fanrui on 2021/1/8
 * Describe:
 */
enum class NetworkStatus {
    INITIAL_LOADING,
    LOADING,
    FAILED,
    COMPLETED
}

class SearchDataSource(private val searchKey: String) : PageKeyedDataSource<Int, Music>() {
    var retry: (() -> Any)? = null
    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> = _networkStatus
    private var source = SourceHolder.get().source

    private val mKWService by lazy {
        KWServiceProvider.create(KWApiService::class.java)
    }
    private val mWYYService by lazy {
        WYYServiceProvider.create(WYApiService::class.java)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Music>) {
        if (searchKey == "") return
        when (source) {
            "酷我" -> {
                //酷我搜索新接口（内容更全）
                mKWService.getKWSearchResult(searchKey, 0, 30)
                        .compose(RxSchedulers.apply())
                        .map {
                            mapKWSearch(it)
                        }
                        .doOnSubscribe {
                            retry = null
                            _networkStatus.postValue(NetworkStatus.INITIAL_LOADING)
                        }
                        .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                            callback.onResult(it, null, 1)
                        }, onExecuteOnceError = {
                            retry = { loadInitial(params, callback) }
                            _networkStatus.postValue(NetworkStatus.FAILED)
                        }, onExecuteOnceComplete = {
                            _networkStatus.postValue(NetworkStatus.COMPLETED)
                        }))
            }
            "网易" -> {
                mWYYService.getWYSearch(searchKey, 0)
                        .compose(RxSchedulers.apply())
                        .map {
                            mapWYSearch(it)
                        }
                        .doOnSubscribe {
                            retry = null
                            _networkStatus.postValue(NetworkStatus.INITIAL_LOADING)
                        }
                        .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                            callback.onResult(it, null, 30)
                        }, onExecuteOnceError = {
                            retry = { loadInitial(params, callback) }
                            _networkStatus.postValue(NetworkStatus.FAILED)
                        }, onExecuteOnceComplete = {
                            _networkStatus.postValue(NetworkStatus.COMPLETED)
                        }))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        //向下加载
        when (source) {
            "酷我" -> {
                //酷我搜索新接口（内容更全）
                mKWService.getKWSearchResult(searchKey, params.key, 30)
                        .compose(RxSchedulers.apply())
                        .map {
                            mapKWSearch(it)
                        }
                        .doOnSubscribe {
                            retry = null
                            _networkStatus.postValue(NetworkStatus.LOADING)
                        }
                        .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                            callback.onResult(it, params.key.plus(1))
                        }, onExecuteOnceError = {
                            if (it.toString() == "java.lang.NullPointerException: Attempt to invoke interface method 'java.util.Iterator java.util.List.iterator()' on a null object reference") {
                                _networkStatus.postValue(NetworkStatus.COMPLETED)
                            } else {
                                retry = { loadAfter(params, callback) }
                                _networkStatus.postValue(NetworkStatus.FAILED)
                            }
                        }, onExecuteOnceComplete = {
                            _networkStatus.postValue(NetworkStatus.COMPLETED)
                        }))
            }
            "网易" -> {
                mWYYService.getWYSearch(searchKey, params.key)
                        .compose(RxSchedulers.apply())
                        .map {
                            mapWYSearch(it)
                        }
                        .doOnSubscribe {
                            retry = null
                            _networkStatus.postValue(NetworkStatus.LOADING)
                        }
                        .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                            callback.onResult(it, params.key.plus(30))
                        }, onExecuteOnceError = {
                            if (it.toString() == "java.lang.NullPointerException: Attempt to invoke interface method 'java.util.Iterator java.util.List.iterator()' on a null object reference") {
                                _networkStatus.postValue(NetworkStatus.COMPLETED)
                            } else {
                                retry = { loadAfter(params, callback) }
                                _networkStatus.postValue(NetworkStatus.FAILED)
                            }
                        }, onExecuteOnceComplete = {
                            _networkStatus.postValue(NetworkStatus.COMPLETED)
                        }))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        TODO("Not yet implemented")
        //向上加载
    }

    private fun mapWYSearch(entity: WYSearchDetail): List<Music> {
        val musics: MutableList<Music> = ArrayList()
        for (song in entity.result.songs) {
            val music = Music()
            val artists = song.ar
            val sb = StringBuilder()
            for (index in artists.indices) {
                if (index < artists.size - 1) {
                    sb.append(artists[index].name).append("&")
                } else {
                    sb.append(artists[index].name)
                }
            }
            music.apply {
                artist = sb.toString()
                title = song.name
                original = song.originCoverType.toString() + ""
                id = song.id.toLong()
            }
            if (!CollectionUtils.isEmptyList(song.alia)) {
                music.subTitle = song.alia[0].toString()
            }
            musics.add(music)
        }
        return musics
    }

    private fun mapKWSearch(entity: KWNewSearchEntity): List<Music> {
        val musicList: MutableList<Music> = ArrayList()
        val pattern = "([\\s\\S]+)-([\\s\\S]+)"
        val r = Pattern.compile(pattern)
        for (bean in entity.abslist) {
            val m = r.matcher(bean.name)
            val music = Music()
            music.apply {
                musicrid = bean.musicrid
                artist = bean.artist
                original = bean.originalsongtype
                album = bean.album
                subTitle = music.subTitle
                if (m.matches()) {
                    title = m.group(1)
                    subTitle = m.group(2)
                } else {
                    title = bean.name
                }
            }
            musicList.add(music)
        }
        return musicList
    }

}