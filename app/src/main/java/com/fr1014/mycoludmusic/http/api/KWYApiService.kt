package com.fr1014.mycoludmusic.http.api

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongDetailEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongUrlEntity
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Time: 2021/8/6
 * Author: fanrui07
 * Description:
 */
interface KWYApiService {
    @GET("song/url")
    suspend fun getWYSongUrl(@Query("id") ids: String, @Query("timestamp") timestamp: String): SongUrlEntity

    @GET("song/detail")
    suspend fun getWYSongDetail(@Query("ids") ids: String): SongDetailEntity
}