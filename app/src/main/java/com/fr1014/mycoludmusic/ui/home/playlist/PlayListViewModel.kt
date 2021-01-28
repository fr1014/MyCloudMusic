package com.fr1014.mycoludmusic.ui.home.playlist

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDataSourceFactory
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel

class PlayListViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    private lateinit var factory: PlayListDataSourceFactory
    lateinit var pageListLiveData: LiveData<PagedList<Music>>
    lateinit var networkStatus: LiveData<NetworkStatus>
    var needScrollToTop = true
    private val coverBitmapLive: MutableLiveData<Bitmap> by lazy {
        MutableLiveData()
    }

    fun getCoverBitmap(): MutableLiveData<Bitmap> = coverBitmapLive

    fun getPlayList(ids: Array<Long>): LiveData<PagedList<Music>> {
        factory = PlayListDataSourceFactory(ids)
        networkStatus = Transformations.switchMap(factory.playListDataSource) { it.networkStatus }
        pageListLiveData = LivePagedListBuilder(factory,
                PagedList.Config.Builder()
                        .setPageSize(25)
                        .setPrefetchDistance(5)
                        .setEnablePlaceholders(false)
                        .build())
                .build()
        return pageListLiveData
    }

    fun retry() {
        factory.playListDataSource.value?.retry?.invoke()
    }
}