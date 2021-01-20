package com.fr1014.mycoludmusic.ui.search

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.ui.search.paging2.SearchDataSourceFactory
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel

class SearchViewModel(application: Application,model:DataRepository) : CommonViewModel(application,model) {

    private lateinit var factory :SearchDataSourceFactory
    lateinit var pageListLiveData : LiveData<PagedList<Music>>
    lateinit var networkStatus : LiveData<NetworkStatus>

    fun search(searchKey: String) : LiveData<PagedList<Music>>{
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
}