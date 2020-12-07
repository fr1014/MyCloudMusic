package com.fr1014.mycoludmusic.data.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicEntity(
        @ColumnInfo(name = "name") val title: String,//歌曲名
        @ColumnInfo(name = "artist") val artist: String?,   //歌手
        @ColumnInfo(name = "imageUrl") val imgUrl: String?, //专辑图片地址
        @ColumnInfo(name = "wyy_id") var id: Long?, //网易歌曲id
        @ColumnInfo(name = "kw_id") var musicRid: String? //网易歌曲id
) {
    @PrimaryKey(autoGenerate = true)
    var musicId: Long = 0
}
