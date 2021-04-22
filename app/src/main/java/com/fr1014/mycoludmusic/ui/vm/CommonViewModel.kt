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
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.mv.MVData
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.home.comment.paging3.CommentPagingSource
import com.fr1014.mymvvm.base.BaseViewModel
import com.fr1014.mymvvm.base.BusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

open class CommonViewModel : BaseViewModel<DataRepository> {

    var commentPagingSource: CommentPagingSource? = null

    private val mvDataLive: MutableLiveData<MVData> by lazy {
        MutableLiveData()
    }

    private val songInfoLive: MutableLiveData<SongsBean> by lazy {
        MutableLiveData()
    }

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
        commentPagingSource = CommentPagingSource(type, id, pageSize)
        commentPagingSource!!
    }.observable.cachedIn(viewModelScope)

    val mvData: LiveData<MVData> = mvDataLive

    val songInfo: LiveData<SongsBean> = songInfoLive

    val playListDetailInfo: LiveData<PlayListDetailEntity> = playListDetailLive

    val playlistWYInfo: LiveData<List<Playlist>> = playlistWYLive

    fun getPlayListDetail(id: Long): LiveData<Array<Long>> {
        getPlayListDetailEntity(id)
        return playListDetailIds
    }

    /*
                dataRepository.getWYSongDetail(music.getId() + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(songDetailEntity -> {
                        if (songDetailEntity.getSongs() != null && songDetailEntity.getSongs().size() > 0) {
                            music.setImgUrl(songDetailEntity.getSongs().get(0).getAl().getPicUrl());
                            music.setDuration(songDetailEntity.getSongs().get(0).getDt());
                        }
                        return music;
                    })
                    .subscribe(new SingleObserver<Music>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Music music) {
                            CoverLoadUtils.get().loadRemoteCover(music);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
     */

    fun getWYMVInfo(id: Long) {
        addSubscribe(model.getWYMVInfo(id)
                .compose(RxSchedulers.applyIO())
                .subscribe {
                    mvDataLive.postValue(it.data)
                })
    }

    fun getWYSongInfo(id: Long) {
        addSubscribe(model.getWYSongDetail(id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    songInfoLive.postValue(it.songs[0])
                })
        )
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