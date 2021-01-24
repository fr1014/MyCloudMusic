package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user

data class WYManagePlayList(
    val body: Body,
    val cookie: List<Any>,
    val status: Int
)

data class Body(
    val cloudCount: Int,
    val code: Int,
    val count: Int,
    val trackIds: String
)