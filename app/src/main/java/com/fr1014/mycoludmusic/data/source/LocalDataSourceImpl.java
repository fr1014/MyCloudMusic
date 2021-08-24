package com.fr1014.mycoludmusic.data.source;

import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.local.room.AppDatabase;
import com.fr1014.mycoludmusic.data.source.local.room.LocalDataSource;
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike;

import java.util.List;

public class LocalDataSourceImpl implements LocalDataSource {
    private volatile static LocalDataSourceImpl instance;
    private AppDatabase db;

    private LocalDataSourceImpl() {
        db = AppDatabase.Companion.getInstance();
    }

    public static LocalDataSourceImpl getInstance() {
        if (instance == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (instance == null) {
                    instance = new LocalDataSourceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public LiveData<List<MusicEntity>> getAllLive() {
        return db.musicDao().getAllLive();
    }

    @Override
    public LiveData<List<MusicEntity>> getAllHistoryOrCurrentLive(boolean history) {
        return db.musicDao().getAllHistoryOrCurrentLive(history);
    }

    @Override
    public LiveData<MusicEntity> getItemLive(String title, String artist) {
        return db.musicDao().getItemLive(title, artist);
    }

    @Override
    public List<MusicEntity> getAll() {
        return db.musicDao().getAll();
    }

    @Override
    public List<MusicEntity> getAllHistoryOrCurrent(boolean history) {
        return db.musicDao().getAllHistoryOrCurrent(history);
    }

    @Override
    public MusicEntity getItem(String title, String artist, boolean isHistory) {
        return db.musicDao().getItem(title, artist, isHistory);
    }

    @Override
    public void insertAll(List<MusicEntity> musicEntities) {
        db.musicDao().insertAll(musicEntities);
    }

    @Override
    public void insert(MusicEntity musicEntity) {
        db.musicDao().insert(musicEntity);
    }

    @Override
    public void delete(MusicEntity musicEntity) {
        db.musicDao().delete(musicEntity);
    }

    @Override
    public void deleteAllMusicEntity() {
        db.musicDao().deleteAll();
    }

    @Override
    public LiveData<List<MusicLike>> getLikeIdsLive() {
        return db.musicLikeDao().getAllLive();
    }

    @Override
    public List<MusicLike> getLikeIds() {
        return db.musicLikeDao().getAllMusicLikes();
    }

    @Override
    public MusicLike getItemLive(String id) {
        return db.musicLikeDao().getItemLive(id);
    }

    @Override
    public void insert(MusicLike musicLike) {
        db.musicLikeDao().insert(musicLike);
    }

    @Override
    public void delete(MusicLike musicLike) {
        db.musicLikeDao().delete(musicLike);
    }

    @Override
    public void insertAllLikeIds(List<MusicLike> musicLikes) {
        db.musicLikeDao().insertAll(musicLikes);
    }

    @Override
    public void deleteAllLikeIds() {
        db.musicLikeDao().deleteAll();
    }
}
