package com.fr1014.mycoludmusic.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity

@Dao
interface MusicDao {
    @Query("SELECT * FROM musicentity")
    fun getAll(): List<MusicEntity>

    @Insert
    fun insertAll(musicEntities: List<MusicEntity>)

    @Delete
    fun delete(musicEntity: MusicEntity)
}