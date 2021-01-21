package com.fr1014.mycoludmusic.ui.home.playlist.paging2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongDetailEntity
import com.fr1014.mycoludmusic.http.WYYServiceProvider
import com.fr1014.mycoludmusic.http.api.WYApiService
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.ExecuteOnceObserver
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import java.util.*

class PlayListDataSource(private val ids: Array<Long>) : PageKeyedDataSource<Int, Music>() {

    var retry: (() -> Any)? = null
    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> = _networkStatus

    private val mWYYService by lazy {
        WYYServiceProvider.create(WYApiService::class.java)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Music>) {
        val idsRange = ids.getRangeIds(0, 99)
        mWYYService.getWYSongDetail(idsRange)
                .compose(RxSchedulers.apply())
                .map {
                    val musics: MutableList<Music> = ArrayList()
                    val songs: List<SongDetailEntity.SongsBean> = it.songs
                    for (song in songs) {
                        val music = Music()
                        val sb = StringBuilder()
                        for (i in song.ar.indices) {
                            val ar = song.ar[i]
                            if (i < song.ar.size - 1) {
                                sb.append(ar.name).append('&')
                            } else {
                                sb.append(ar.name)
                            }
                        }
                        music.apply {
                            id = song.id.toLong()
                            artist = sb.toString()
                            title = song.name
                            imgUrl = song.al.picUrl
                            duration = song.dt.toLong()
                            album = song.al.name
                        }
                        musics.add(music)
                    }
                    musics
                }
                .doOnSubscribe {
                    retry = null
                    _networkStatus.postValue(NetworkStatus.LOADING)
                }
                .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                    callback.onResult(it, null, 100)
                }, onExecuteOnceError = {
                    retry = { loadInitial(params, callback) }
                    _networkStatus.postValue(NetworkStatus.FAILED)
                }, onExecuteOnceComplete = {
                    _networkStatus.postValue(NetworkStatus.COMPLETED)
                })
                )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        val idsRange = ids.getRangeIds(params.key, 99)
        mWYYService.getWYSongDetail(idsRange)
                .compose(RxSchedulers.apply())
                .map {
                    val musics: MutableList<Music> = ArrayList()
                    val songs: List<SongDetailEntity.SongsBean> = it.songs
                    for (song in songs) {
                        val music = Music()
                        val sb = StringBuilder()
                        for (i in song.ar.indices) {
                            val ar = song.ar[i]
                            if (i < song.ar.size - 1) {
                                sb.append(ar.name).append('&')
                            } else {
                                sb.append(ar.name)
                            }
                        }
                        music.apply {
                            id = song.id.toLong()
                            artist = sb.toString()
                            title = song.name
                            imgUrl = song.al.picUrl
                            duration = song.dt.toLong()
                            album = song.al.name
                        }
                        musics.add(music)
                    }
                    musics
                }
                .doOnSubscribe {
                    retry = null
                    _networkStatus.postValue(NetworkStatus.LOADING)
                }
                .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                    callback.onResult(it, params.key.plus(100))
                }, onExecuteOnceError = {
                    if (it.toString() == "java.lang.NullPointerException: it.songs must not be null") {
                        _networkStatus.postValue(NetworkStatus.COMPLETED)
                    } else {
                        retry = { loadAfter(params, callback) }
                        _networkStatus.postValue(NetworkStatus.FAILED)
                    }

                }, onExecuteOnceComplete = {
                    _networkStatus.postValue(NetworkStatus.COMPLETED)
                })
                )
    }
}

fun Array<Long>.getRangeIds(start: Int, loadRange: Int): String {
    var mLoadRange: Int
    if (isNotEmpty()) {
        mLoadRange = loadRange
        if (start >= size) {
            return ""
        }
        if ((start + loadRange) >= size) {
            mLoadRange = size - start - 1
        }
        val idsRange = StringBuilder()
        for (position in start..(start + mLoadRange)) {
            if (position == size) {
                break
            }
            idsRange.append(this[position])
            idsRange.append(",")
        }
        return idsRange.substring(0, idsRange.length - 1)
    }
    return ""
}