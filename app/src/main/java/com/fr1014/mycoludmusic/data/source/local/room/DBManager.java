package com.fr1014.mycoludmusic.data.source.local.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.ExecuteOnceObserver;
import com.fr1014.mycoludmusic.rx.MyDisposableObserver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CollectionUtils;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class DBManager {
    private DataRepository model;

    private DBManager() {
        model = MyApplication.provideRepository();
    }

    public static DBManager get() {
        return StaticInstanceHolder.instance;
    }

    public static class StaticInstanceHolder {
        private static final DBManager instance = new DBManager();
    }

    public LiveData<MusicEntity> getMusicEntityItem(Music music) {
        return model.getItemLive(music.getTitle(), music.getArtist());
    }

    public LiveData<List<MusicEntity>> getLocalMusicList(boolean isHistory) {
        return model.getAllHistoryOrCurrentLive(isHistory);
    }

    public void insert(Music music, boolean isHistory) {
        Observable.just(music)
                .compose(RxSchedulers.applyIO())
                .subscribe(new MyDisposableObserver<Music>() {
                    @Override
                    public void onNext(@NonNull Music music) {
                        MusicEntity entity = model.getItem(music.getTitle(), music.getArtist(), isHistory);
                        if (entity == null) {
                            MusicEntity musicEntity = new MusicEntity(music.getTitle(), music.getArtist(), music.getImgUrl(), music.getId(), music.getMUSICRID(), music.getDuration(), isHistory);
                            model.insert(musicEntity);
                        }
                    }
                });

    }

    public void delOldInsertNewMusicList(List<Music> musicList,boolean isHistory) {
        Observable.just(musicList)
                .compose(RxSchedulers.applyIO())
                .subscribe(new ExecuteOnceObserver<List<Music>>() {
                    @Override
                    public void onNext(List<Music> musicList) {
                        List<MusicEntity> allOldCurrentMusic = model.getAllHistoryOrCurrent(false);
                        for (MusicEntity musicEntity : allOldCurrentMusic) {
                            model.delete(musicEntity);
                        }
                        List<MusicEntity> musicEntities = new ArrayList<>();
                        for (Music music : musicList) {
                            MusicEntity entity = new MusicEntity(music.getTitle(), music.getArtist(),music.getImgUrl(), music.getId(),music.getMUSICRID(),music.getDuration(),isHistory);
                            musicEntities.add(entity);
                        }
                        model.insertAll(musicEntities);
                    }
                });
    }

    public void delete(MusicEntity musicEntity) {
        Observable.just(musicEntity)
                .compose(RxSchedulers.applyIO())
                .subscribe(new MyDisposableObserver<MusicEntity>() {
                    @Override
                    public void onNext(@NonNull MusicEntity musicEntity) {
                        model.delete(musicEntity);
                    }
                });

    }

}
