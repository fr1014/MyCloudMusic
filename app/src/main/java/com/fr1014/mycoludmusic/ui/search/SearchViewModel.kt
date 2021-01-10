package com.fr1014.mycoludmusic.ui.search

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.search.paging2.SearchDataSourceFactory
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mymvvm.base.BusLiveData
import com.google.gson.Gson
import java.util.*
import java.util.regex.Pattern

class SearchViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    private val searchLive: BusLiveData<List<Music>>  = BusLiveData<List<Music>>()
    private lateinit var factory :SearchDataSourceFactory
    lateinit var pageListLiveData : LiveData<PagedList<Music>>

    fun search(searchKey: String) : LiveData<PagedList<Music>>{
        factory = SearchDataSourceFactory(searchKey)
        pageListLiveData = LivePagedListBuilder(factory,
                PagedList.Config.Builder()
                        .setPageSize(25)
                        .setEnablePlaceholders(false)
                        .build())
                .build()
        return pageListLiveData
    }

    //搜索
    fun getSearchLive() = searchLive

    fun resetQuery() {
        pageListLiveData.value?.dataSource?.invalidate()
    }

    fun retry(searchKey: String) {
//        factory.searchDataSource.value?.retry?.invoke()
    }

//    //获取搜索结果（酷我）
//    fun getSearchEntityKW(name: String?, count: Int) {
//        addSubscribe(model.getSearchResult(name, count)
//                .map<List<Music>> { responseBody ->
//                    val result = responseBody.string()
//                    val replace1 = result.replace("try{var jsondata=", "")
//                    val replace2 = replace1.replace("; song(jsondata);}catch(e){jsonError(e)}", "")
//                    val json = replace2.replace("'".toRegex(), "\"")
//                    val searchEntity = Gson().fromJson(json, KWSearchEntity::class.java)
//                    val musics: MutableList<Music> = ArrayList()
//                    for (abslistBean in searchEntity.abslist) {
//                        val music = Music()
//                        if (!TextUtils.isEmpty(abslistBean.artist)) {
////                                music.setArtist(abslistBean.getARTIST());
//                            music.artist = abslistBean.artist.replace("&nbsp;".toRegex(), " ").replace("###".toRegex(), "&")
//                        } else {
//                            music.artist = abslistBean.aartist.replace("&nbsp;".toRegex(), " ").replace("###".toRegex(), "&")
//                        }
//                        music.title = abslistBean.songname.replace("&nbsp;".toRegex(), " ")
//                        music.imgUrl = abslistBean.hts_MVPIC
//                        music.musicrid = abslistBean.musicrid
//                        musics.add(music)
//                    }
//                    musics
//                }.compose(RxSchedulers.apply())
//                .doOnSubscribe {
//                    //showDialog();
//                }
//                .subscribe { musicList ->
//                    //dismissDialog();
//                    searchLive.postValue(musicList)
//                })
//    }
}