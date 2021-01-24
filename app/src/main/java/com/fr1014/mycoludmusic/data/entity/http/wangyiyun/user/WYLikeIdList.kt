package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user

/**
 * Create by fanrui on 2021/1/23
 * Describe:
 */
data class WYLikeIdList(
    val checkPoint: Long,
    val code: Int,
    val ids: List<Long>
)