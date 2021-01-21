package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist

/**
 * Create by fanrui on 2021/1/13
 * Describe:
 */
data class WYUserPlayList(
        val code: Int,
        val more: Boolean,
        val playlist: List<Playlist>,
        val version: String
)