package com.fr1014.mycoludmusic.ui.search

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.MatchBean
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.ui.search.paging2.SearchDataSourceFactory
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesConst
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesUtil
import io.reactivex.functions.Consumer
import java.lang.StringBuilder

class SearchViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    private lateinit var factory: SearchDataSourceFactory
    lateinit var pageListLiveData: LiveData<PagedList<Music>>
    lateinit var networkStatus: LiveData<NetworkStatus>

    private val searchHistoriesLive: MutableLiveData<List<String>> by lazy {
        MutableLiveData()
    }

    private val searchMatchLive: MutableLiveData<List<MatchBean>> by lazy {
        MutableLiveData()
    }

    private val searchHotDetailLive: MutableLiveData<SearchHotDetail> by lazy {
        MutableLiveData()
    }

    private val searchKeyLive: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getSearchHistories(): LiveData<List<String>> = searchHistoriesLive

    fun getSearchMatch(): LiveData<List<MatchBean>> = searchMatchLive;

    fun getSearchHotDetail(): LiveData<SearchHotDetail> = searchHotDetailLive

    fun getSearchKey(): MutableLiveData<String> = searchKeyLive

    fun searchMatch(keyWord: String) {
        addSubscribe(model.getSearchMatch(keyWord, "mobile")
                .compose(RxSchedulers.apply())
                .map {
                    it.result.allMatch
                }
                .subscribe(Consumer {
                    searchMatchLive.postValue(it)
                }))
    }

    fun searchHotDetail() {
        addSubscribe(model.searchHotDetail
                .compose(RxSchedulers.apply())
                .subscribe(Consumer {
                    searchHotDetailLive.postValue(it)
                }))
    }

    fun search(searchKey: String): LiveData<PagedList<Music>> {
        factory = SearchDataSourceFactory(searchKey)
        networkStatus = Transformations.switchMap(factory.searchDataSource) { it.networkStatus }
        pageListLiveData = LivePagedListBuilder(factory,
                PagedList.Config.Builder()
                        .setPageSize(25)
                        .setPrefetchDistance(5)//距离底部还有多少条数据时开始预加载
                        .setEnablePlaceholders(false)
                        .build())
                .build()
        return pageListLiveData
    }

    fun retry() {
        factory.searchDataSource.value?.retry?.invoke()
    }

    fun clearHistory(){
        SharedPreferencesUtil.clear(SharedPreferencesConst.SEARCH_HISTORY)
        searchHistoriesLive.postValue(null)
    }

    fun saveHistory(searchWord: String) {
        val histories = SharedPreferencesUtil.getString(SharedPreferencesConst.SEARCH_HISTORY, SharedPreferencesConst.SEARCH_HISTORY_KEY, "")
        val hisList = if (histories == "") {
            ArrayList<String>()
        } else{
            val list = histories?.split(",")
            ArrayList(list!!)
        }
        //如果搜索关键词历史中有则移除旧纪录并添加新的
        if (hisList.contains(searchWord)){
            hisList.remove(searchWord)
        }
        hisList.add(searchWord)
        val sb = StringBuilder()
        val size = hisList.size
        hisList.forEachIndexed { index, s ->
            if (size - index <= 10) {
                sb.append(s).append(",")
            }
        }
        SharedPreferencesUtil.putString(SharedPreferencesConst.SEARCH_HISTORY, SharedPreferencesConst.SEARCH_HISTORY_KEY, sb.substring(0, sb.length - 1))
        hisList.reversed().apply {
            searchHistoriesLive.postValue(this)
        }
    }

    fun getHistory() {
        val histories = SharedPreferencesUtil.getString(SharedPreferencesConst.SEARCH_HISTORY, SharedPreferencesConst.SEARCH_HISTORY_KEY, "")
        if (histories == ""){
            searchHistoriesLive.postValue(null)
            return
        }
        val list = histories?.split(",")
        val hisList = if (list == null) ArrayList<String>() else ArrayList(list)
        hisList.reversed().apply {
            searchHistoriesLive.postValue(this)
        }
    }
}