package com.fr1014.mycoludmusic.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity

@Dao
interface MusicDao {
    @Query("SELECT * FROM musicentity")
    fun getAll(): LiveData<List<MusicEntity>>

    @Query("SELECT * FROM musicentity where name = :title and artist = :artist")
    fun getItem(title:String,artist:String): LiveData<MusicEntity>

    @Insert
    fun insertAll(musicEntities: List<MusicEntity>)

    @Insert
    fun insert(musicEntity: MusicEntity)

    @Delete
    fun delete(musicEntity: MusicEntity)
}