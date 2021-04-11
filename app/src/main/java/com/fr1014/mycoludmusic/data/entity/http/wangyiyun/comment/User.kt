package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment

data class User(
    val anonym: Int,
    val authStatus: Int,
    val avatarDetail: AvatarDetail,
    val avatarUrl: String,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val isHug: Boolean,
    val liveInfo: Any,
    val locationInfo: Any,
    val nickname: String,
    val relationTag: Any,
    val remarkName: Any,
    val userId: Long,
    val userType: Int,
    val vipRights: Any,
    val vipType: Int
)