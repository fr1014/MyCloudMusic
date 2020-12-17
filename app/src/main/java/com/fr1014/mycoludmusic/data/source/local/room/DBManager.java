package com.fr1014.mycoludmusic.data.source.local.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.MyDisposableObserver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class DBManager {
    private DataRepository model;
    MutableLiveData<Boolean> musicChange;

    private DBManager() {
        model = MyApplication.provideRepository();
    }

    public static DBManager get() {
        return StaticInstanceHolder.instance;
    }

    public LiveData<Boolean> getMusicListMutable() {
        if (musicChange == null){
            musicChange = new MutableLiveData<>();
        }
        return musicChange;
    }

    public static class StaticInstanceHolder {
        private static final DBManager instance = new DBManager();
    }

    public LiveData<MusicEntity> getMusicEntityItem(Music music){
        return model.getItemLive(music.getTitle(), music.getArtist());
    }

    public LiveData<List<MusicEntity>> getHistoryListMusicEntity(){
        return model.getAllHistoryOrCurrentLive(true);
    }

    public LiveData<List<MusicEntity>> getCurrentListMusicEntity(){
        return model.getAllHistoryOrCurrentLive(false);
    }

    public void insert(Music music,boolean isHistory) {
        Observable.just(music)
                .compose(RxSchedulers.applyIO())
                .subscribe(new MyDisposableObserver<Music>(){
                    @Override
                    public void onNext(@NonNull Music music) {
                        MusicEntity entity = model.getItem(music.getTitle(), music.getArtist(),isHistory);
                        if (entity == null) {
                            MusicEntity musicEntity = new MusicEntity(music.getTitle(), music.getArtist(), music.getImgUrl(), music.getId(), music.getMUSICRID(),music.getDuration(),isHistory);
                            model.insert(musicEntity);
                            musicChange.postValue(true);
                        }
                    }
                });

    }

    public void delete(MusicEntity musicEntity) {
        Observable.just(musicEntity)
                .compose(RxSchedulers.applyIO())
                .subscribe(new MyDisposableObserver<MusicEntity>(){
                    @Override
                    public void onNext(@NonNull MusicEntity musicEntity) {
                        model.delete(musicEntity);
                    }
                });

    }

    //获取当前播放的歌曲
    public List<Music> getMusicCurrent() {
        List<Music> musicList = new ArrayList<>();
        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new MyDisposableObserver<String>(){
                    @Override
                    public void onNext(@NonNull String s) {
                        List<MusicEntity> musicEntities = model.getAllHistoryOrCurrent(false);
                        for (MusicEntity musicEntity : musicEntities) {
                            musicList.add(new Music(musicEntity.getId(), musicEntity.getArtist(), musicEntity.getTitle(),"", musicEntity.getImgUrl(), musicEntity.getMusicRid(),musicEntity.getDuration()));
                        }
//                        Collections.reverse(musicList);
                    }
                });
        return musicList;
    }

}
