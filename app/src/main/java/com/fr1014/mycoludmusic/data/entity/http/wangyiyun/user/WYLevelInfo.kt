package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user

data class WYLevelInfo(
        val code: Int,
        val data: LevelData,
        val full: Boolean
)

data class LevelData(
    val info: String,
    val level: Int,
    val nextLoginCount: Int,
    val nextPlayCount: Int,
    val nowLoginCount: Int,
    val nowPlayCount: Int,
    val progress: Double,
    val userId: Long
)