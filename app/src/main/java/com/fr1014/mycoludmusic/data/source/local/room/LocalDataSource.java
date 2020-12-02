package com.fr1014.mycoludmusic.data.source.local.room;

import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;

import java.util.List;

public interface LocalDataSource {
    LiveData<List<MusicEntity>> getAllLive();

    LiveData<MusicEntity> getItemLive(String title,String artist);

   List<MusicEntity> getAll();

    MusicEntity getItem(String title,String artist);

    void insertAll(List<MusicEntity> musicEntities);

    void insert(MusicEntity musicEntity);

    void delete(MusicEntity musicEntity);
}
