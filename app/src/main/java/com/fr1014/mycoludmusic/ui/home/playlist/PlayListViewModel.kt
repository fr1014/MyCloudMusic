package com.fr1014.mycoludmusic.ui.home.playlist

import android.app.Application
import android.graphics.Bitmap
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.CollectPlaylist
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDataSource
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils

class PlayListViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    var collectPlayListType = 1

    private val coverBitmapLive: MutableLiveData<Bitmap> by lazy {
        MutableLiveData()
    }
    private val collectPlayListLive: MutableLiveData<CollectPlaylist> by lazy {
        MutableLiveData()
    }

    fun getCollectPlayList(): LiveData<CollectPlaylist> = collectPlayListLive

    fun getCoverBitmap(): MutableLiveData<Bitmap> = coverBitmapLive

    fun getPlayList(ids: Array<Long>) = Pager(PagingConfig(pageSize = 20)) {
        PlayListDataSource(ids)
    }.flow.cachedIn(viewModelScope)

    // 收藏 / 取消收藏歌单
    fun collectPlayList(id: Long) {
        addSubscribe(
                model.getWYCollectPlayList(collectPlayListType, id, System.currentTimeMillis().toString())
                        .compose(RxSchedulers.apply())
                        .subscribe { collectPlayList ->
                            CommonUtils.toastShort(if (collectPlayListType == 1) "收藏成功" else "取消收藏")
                            if (collectPlayList.code == 200) {
                                collectPlayListType = if (collectPlayListType == 1) {
                                    2
                                } else {
                                    1;
                                }
                                collectPlayListLive.postValue(collectPlayList)
                            } else {
                                if (TextUtils.isEmpty(collectPlayList.msg)) {
                                    CommonUtils.toastShort(collectPlayList.msg)
                                }
                            }
                        })
    }
}