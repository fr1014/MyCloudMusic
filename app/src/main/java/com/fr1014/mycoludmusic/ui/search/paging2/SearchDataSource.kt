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
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Create by fanrui on 2021/1/8
 * Describe:
 */
class SearchDataSource() : PageKeyedDataSource<Int, Music>() {

    private var source = SourceHolder.get().source
    private lateinit var searchKey:String

    fun setSearchKey(searchKey: String){
        this.searchKey = searchKey
    }

    private val mKWService by lazy {
        KWServiceProvider.create(KWApiService::class.java)
    }
    private val mWYYService by lazy {
        WYYServiceProvider.create(WYApiService::class.java)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Music>) {
        when (source) {
            "酷我" -> {
                mKWService.getKWSearchResult(searchKey, 0, 30)
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
                            callback.onResult(it, null, 2)
                        }))
            }
//            "网易" -> viewModel.getSearchEntityWYY(searchKey, 0)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        //向下加载
        when (source) {
            "酷我" -> {
                mKWService.getKWSearchResult(searchKey, params.key, 30)
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
                            callback.onResult(it, params.key + 1)
                        }))
            }
//            "网易" -> viewModel.getSearchEntityWYY(searchKey, 0)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        TODO("Not yet implemented")
        //向上加载
    }
}