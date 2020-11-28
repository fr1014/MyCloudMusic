package com.fr1014.mycoludmusic.data.source.local.room;

import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;

import java.util.List;

public interface LocalDataSource {
    List<MusicEntity> getAll();

    void insertAll(List<MusicEntity> musicEntities);

    void delete(MusicEntity musicEntity);
}
