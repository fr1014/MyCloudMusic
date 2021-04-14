package com.fr1014.mycoludmusic.ui.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.Comment
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.home.comment.paging3.CommentPagingSource
import com.fr1014.mymvvm.base.BaseViewModel
import com.fr1014.mymvvm.base.BusLiveData
import java.util.*
import java.util.Arrays.copyOf
import kotlin.collections.ArrayList

open class CommonViewModel : BaseViewModel<DataRepository> {

    var commentPagingSource : CommentPagingSource? = null

    private val playListDetailIds: BusLiveData<Array<Long>> by lazy {
        BusLiveData()
    }

    private val playlistWYLive: MutableLiveData<List<Playlist>> by lazy {
        MutableLiveData()
    }

    private val playListDetailLive: MutableLiveData<PlayListDetailEntity> by lazy {
        MutableLiveData()
    }

    constructor(application: Application) : super(application) {}
    constructor(application: Application, model: DataRepository?) : super(application, model) {}

    fun commentList(type: Int, id: Long, pageSize: Int) = Pager(PagingConfig(pageSize = 6)) {
        commentPagingSource  = CommentPagingSource(type, id, pageSize)
        commentPagingSource!!
    }.observable.cachedIn(viewModelScope)


    val playListDetailInfo: LiveData<PlayListDetailEntity> = playListDetailLive

    val playlistWYInfo: LiveData<List<Playlist>> = playlistWYLive

    fun getPlayListDetail(id: Long): LiveData<Array<Long>> {
        getPlayListDetailEntity(id)
        return playListDetailIds
    }

    private fun getPlayListDetailEntity(id: Long) {
        addSubscribe(model.getPlayListDetail(id)
                .compose(RxSchedulers.apply())
                .subscribe { playListDetailEntity ->
                    val tracks = playListDetailEntity.playlist.trackIds
                    val ids = ArrayList<Long>()
                    for (index in tracks.indices) {
                        ids.add(tracks[index].id)
                    }
                    val array: Array<Long> = ids.toTypedArray()
                    playListDetailLive.postValue(playListDetailEntity)
                    playListDetailIds.postValue(array)
                })
    }

    fun getWYUserPlayList() {
        val userId = Preferences.getUserProfile()?.userId ?: return
        addSubscribe(model
                .getWYUserPlayList(userId, System.currentTimeMillis().toString())
                .compose(RxSchedulers.apply())
                .subscribe {
                    playlistWYLive.postValue(it.playlist)
                }
        )
    }

    fun getLikeIdList(uid: Long?) {
        addSubscribe(model!!.getWYLikeIdList(uid, System.currentTimeMillis().toString())
                .compose(RxSchedulers.applyIO())
                .subscribe { (_, _, ids) ->
                    model!!.deleteAllLikeIds()
                    val musicLikes: MutableList<MusicLike> = ArrayList()
                    for (id in ids) {
                        musicLikes.add(MusicLike(id))
                    }
                    model!!.insertAllLikeIds(musicLikes)
                }
        )
    }
}