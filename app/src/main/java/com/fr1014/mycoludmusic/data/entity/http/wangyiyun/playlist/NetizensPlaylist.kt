package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist

/**
 * Create by fanrui on 2021/1/15
 * Describe:网易精选碟
 */
data class NetizensPlaylist(
        val cat: String,
        val code: Int,
        val more: Boolean,
        val playlists: List<Playlist>,
        val total: Int
)