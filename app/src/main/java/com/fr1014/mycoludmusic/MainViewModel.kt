package com.fr1014.mycoludmusic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.MyDisposableObserver
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mymvvm.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.*

class MainViewModel(application: Application, model: DataRepository) : BaseViewModel<DataRepository>(application, model) {
    private val logoutLive: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    fun getLogoutLive():LiveData<Boolean> = logoutLive

    fun logout(){
        addSubscribe(model.wyLogout
                .compose(RxSchedulers.apply())
                .subscribe(Consumer {
                    if (it.code == 200) {
                        logoutLive.postValue(true)
                    } else {
                        logoutLive.postValue(false)
                    }
                })
        )
    }

    //    public void insertMusicList(List<Music> musicList){
    //        List<MusicEntity> musicEntities = new ArrayList<>();
    //        for (Music music : musicList){
    //            MusicEntity musicEntity = new MusicEntity(music.getTitle(),music.getArtist(),music.getImgUrl(),music.getId(),music.getMUSICRID(),music.getDuration(),false);
    //            musicEntities.add(musicEntity);
    //        }
    //        model.insertAll(musicEntities);
    //    }
    //
    //    public void deleteOldMusicList(){
    //        Observable.just("")
    //                .compose(RxSchedulers.applyIO())
    //                .subscribe(new MyDisposableObserver<String>(){
    //                    @Override
    //                    public void onNext(@io.reactivex.annotations.NonNull String s) {
    //                        List<MusicEntity> allOldCurrentMusic = model.getAllHistoryOrCurrent(false);
    //                        for (MusicEntity musicEntity : allOldCurrentMusic){
    //                            model.delete(musicEntity);
    //                        }
    //                    }
    //                });
    //    }

    fun delOldInsertNewMusicList(musicList: List<Music>) {
        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(object : MyDisposableObserver<String>() {
                    override fun onNext(s: String) {
                        val allOldCurrentMusic = model.getAllHistoryOrCurrent(false)
                        for (musicEntity in allOldCurrentMusic) {
                            model.delete(musicEntity)
                        }
                        val musicEntities: MutableList<MusicEntity> = ArrayList()
                        for (music in musicList) {
                            val musicEntity = MusicEntity(music.title, music.artist, music.imgUrl, music.id, music.musicrid, music.duration, false)
                            musicEntities.add(musicEntity)
                        }
                        model.insertAll(musicEntities)
                    }
                })
    }
}