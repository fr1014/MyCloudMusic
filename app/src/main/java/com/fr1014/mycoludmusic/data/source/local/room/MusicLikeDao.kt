package com.fr1014.mycoludmusic.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Create by fanrui on 2021/1/23
 * Describe:
 */
@Dao
interface MusicLikeDao {
    @Query("SELECT * FROM musiclike where id = :id")
    fun getItemLive(id: String): MusicLike

    @Query("SELECT * FROM musiclike")
    fun getAllLive(): LiveData<List<MusicLike>>

    @Query("SELECT * FROM musiclike")
    fun getAllMusicLikes(): List<MusicLike>

    @Insert
    fun insert(musicLike: MusicLike)

    @Insert
    fun insertAll(musicLikes: List<MusicLike>)

    @Delete
    fun delete(musicLike: MusicLike)

    @Query("DELETE FROM musiclike")
    fun deleteAll()
}