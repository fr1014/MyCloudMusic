package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.enum

/**
 * Create by fanrui on 2021/4/10
 * Describe:
 */
enum class CommentType(val type: Int) {
    SONG(0), //歌曲
    MV(1),  //MV
    PLAYLIST(2), //歌单
    SONGSALE(3), //专辑
    RADIO(4),  //电台
    VIDEO(5), //视频
    DYNAMIC(6) //动态
}