package com.fr1014.mycoludmusic.data.entity.http.wangyiyun

/**
 * Create by fanrui on 2021/1/15
 * Describe:
 */
data class Creator(
        val accountStatus: Int,
        val anchor: Boolean,
        val authStatus: Int,
        val authenticationTypes: Int,
        val authority: Int,
        val avatarDetail: Any,
        val avatarImgId: Long,
        val avatarImgIdStr: String,
        val avatarImgId_str: String,
        val avatarUrl: String,
        val backgroundImgId: Long,
        val backgroundImgIdStr: String,
        val backgroundUrl: String,
        val birthday: Long,
        val city: Int,
        val defaultAvatar: Boolean,
        val description: String,
        val detailDescription: String,
        val djStatus: Int,
        val expertTags: Any,
        val experts: Any,
        val followed: Boolean,
        val gender: Int,
        val mutual: Boolean,
        val nickname: String,
        val province: Int,
        val remarkName: Any,
        val signature: String,
        val userId: Int,
        val userType: Int,
        val vipType: Int
)