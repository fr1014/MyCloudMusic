package com.fr1014.mycoludmusic.ui.search.paging2

import androidx.paging.PageKeyedDataSource
import com.fr1014.mycoludmusic.SourceHolder
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
class SearchDataSource(private val searchKey: String) : PageKeyedDataSource<Int, Music>() {

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
                getKWSearchResult(searchKey, 0, callback, null, null)
            }
            "网易" -> {
                getWYSearchResult(searchKey, 0, callback, null, null)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        //向下加载
        when (source) {
            "酷我" -> {
                getKWSearchResult(searchKey, params.key, null, params, callback)
            }
            "网易" -> {
                getWYSearchResult(searchKey, params.key, null, params, callback)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        TODO("Not yet implemented")
        //向上加载
    }

    private fun getWYSearchResult(searchKey: String, offset: Int, callbackInitial: LoadInitialCallback<Int, Music>?, params: LoadParams<Int>?, callbackAfter: LoadCallback<Int, Music>?) {
        mWYYService.getWYSearch(searchKey, offset)
                .map<List<Music>> { wySearchDetail ->
                    val musics: MutableList<Music> = ArrayList()
                    for (song in wySearchDetail.result.songs) {
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
                    musics
                }
                .compose(RxSchedulers.apply())
                .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                    callbackInitial?.onResult(it, null, 30)
                    //如果为loadInitial则不执行该语句
                    callbackAfter?.onResult(it, params?.key?.plus(30))
                }))
    }

    private fun getKWSearchResult(searchKey: String, page: Int, callbackInitial: LoadInitialCallback<Int, Music>?, params: LoadParams<Int>?, callbackAfter: LoadCallback<Int, Music>?) {
        //酷我搜索新接口（内容更全）
        mKWService.getKWSearchResult(searchKey, page, 30)
                .compose(RxSchedulers.apply())
                .map<List<Music>> {
                    val musicList: MutableList<Music> = ArrayList()
                    val pattern = "([\\s\\S]+)-([\\s\\S]+)"
                    val r = Pattern.compile(pattern)
                    for (bean in it.abslist) {
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
                    musicList
                }
                .subscribe(ExecuteOnceObserver(onExecuteOnceNext = {
                    callbackInitial?.onResult(it, null, 1)
                    //如果为loadInitial则不执行该语句
                    callbackAfter?.onResult(it, params?.key?.plus(1))
                }))
    }
}