package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean

data class AlbumDetail(
    val album: Album,
    val code: Int,
    val songs: List<SongsBean>
)