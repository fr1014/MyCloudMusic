package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean

data class DayRecommend(
        val code: Int,
        val data: DayRecommendData
)

data class DayRecommendData(
        val dailySongs: List<SongsBean>,
        val orderSongs: List<Any>,
        val recommendReasons: List<RecommendReason>
)

data class RecommendReason(
        val reason: String,
        val songId: Int
)
