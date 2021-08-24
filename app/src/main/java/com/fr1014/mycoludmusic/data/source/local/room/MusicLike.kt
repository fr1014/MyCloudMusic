package com.fr1014.mycoludmusic.data.source.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by fanrui on 2021/1/23
 * Describe:
 */
@Entity
data class MusicLike(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: String,  //歌曲id
//        @ColumnInfo(name = "playListId") val playListId: Long //歌单id
)