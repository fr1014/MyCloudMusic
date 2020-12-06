package com.fr1014.mycoludmusic.data.source.local.room;

import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

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

    public void insert(Music music) {
        Observable.just(music)
                .compose(RxSchedulers.applyIO())
                .subscribe(new Observer<Music>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Music music) {
                        MusicEntity entity = model.getItem(music.getTitle(), music.getArtist());
                        if (entity == null) {
                            MusicEntity musicEntity = new MusicEntity(music.getTitle(), music.getArtist(), music.getImgUrl(),music.getSongUrl(), music.getId(), music.getMUSICRID());
                            model.insert(musicEntity);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void delete(Music music) {
        Observable.just(music)
                .compose(RxSchedulers.applyIO())
                .subscribe(new Observer<Music>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Music music) {
                        MusicEntity musicEntity = new MusicEntity(music.getTitle(), music.getArtist(), music.getImgUrl(), music.getSongUrl(),music.getId(), music.getMUSICRID());
                        model.delete(musicEntity);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public List<Music> getMusicLocal() {
        List<Music> musicList = new ArrayList<>();
        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        List<MusicEntity> musicEntities = model.getAll();
                        for (MusicEntity musicEntity : musicEntities) {
                            musicList.add(new Music(musicEntity.getId(), musicEntity.getArtist(), musicEntity.getTitle(), "", musicEntity.getImgUrl(), musicEntity.getMusicRid()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return musicList;
    }

}
