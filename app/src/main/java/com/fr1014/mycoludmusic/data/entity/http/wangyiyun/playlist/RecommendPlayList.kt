package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist

data class RecommendPlayList(
    val category: Int,
    val code: Int,
    val hasTaste: Boolean,
    val result: List<PlayListResult>
)

data class PlayListResult(
    val alg: String,
    val canDislike: Boolean,
    val copywriter: String,
    val highQuality: Boolean,
    val id: Long,
    val name: String,
    val picUrl: String,
    val playCount: Long,
    val trackCount: Int,
    val trackNumberUpdateTime: Long,
    val type: Int
)