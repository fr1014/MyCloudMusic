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
    fun getAllLive(): LiveData<List<MusicEntity>>

    @Query("SELECT * FROM musicentity where name = :title and artist = :artist")
    fun getItemLive(title:String, artist:String): LiveData<MusicEntity>

    @Query("SELECT * FROM musicentity")
    fun getAll(): List<MusicEntity>

    @Query("SELECT * FROM musicentity where history = :history")
    fun getAllHistoryOrCurrent(history: Boolean): List<MusicEntity>

    @Query("SELECT * FROM musicentity where history = :history")
    fun getAllHistoryOrCurrentLive(history: Boolean): LiveData<List<MusicEntity>>

    @Query("SELECT * FROM musicentity where name = :title and artist = :artist and history = :history")
    fun getItem(title:String, artist:String,history: Boolean): MusicEntity


    @Insert
    fun insertAll(musicEntities: List<MusicEntity>)

    @Insert
    fun insert(musicEntity: MusicEntity)

    @Delete
    fun delete(musicEntity: MusicEntity)

    @Query("DELETE FROM musicentity")
    fun deleteAll();
}