package com.fr1014.mycoludmusic.data.source;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.local.room.AppDatabase;
import com.fr1014.mycoludmusic.data.source.local.room.LocalDataSource;
import java.util.List;

public class LocalDataSourceImpl implements LocalDataSource {
    private volatile static LocalDataSourceImpl instance;
    private AppDatabase db;

    private LocalDataSourceImpl(){
        db = Room.databaseBuilder(MyApplication.getInstance(),
                AppDatabase.class, "db_music").build();
    }

    public static LocalDataSourceImpl getInstance() {
        if (instance == null){
            synchronized (LocalDataSourceImpl.class){
                if (instance == null){
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
    public LiveData<MusicEntity> getItemLive(String title, String artist) {
        return db.musicDao().getItemLive(title,artist);
    }

    @Override
    public List<MusicEntity> getAll() {
        return db.musicDao().getAll();
    }

    @Override
    public MusicEntity getItem(String title,String artist) {
        return db.musicDao().getItem(title,artist);
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
}
