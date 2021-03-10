package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale

data class Artist(
    val albumSize: Int,
    val alias: List<Any>,
    val briefDesc: String,
    val followed: Boolean,
    val id: Int,
    val img1v1Id: Long,
    val img1v1Id_str: String,
    val img1v1Url: String,
    val musicSize: Int,
    val name: String,
    val picId: Long,
    val picId_str: String,
    val picUrl: String,
    val topicPerson: Int,
    val trans: String,
    val transNames: List<String>
)