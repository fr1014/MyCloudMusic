package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment

data class CommentItem(
    val args: Any,
    val beReplied: Any,
    val commentId: Long,
    val commentLocationType: Int,
    val content: String,
    val expressionUrl: Any,
    val extInfo: ExtInfo,
    val liked: Boolean,
    val likedCount: Int,
    val parentCommentId: Int,
    val pendantData: Any,
    val repliedMark: Boolean,
    val showFloorComment: ShowFloorComment,
    val source: Any,
    val status: Int,
    val tag: Tag,
    val time: Long,
    val user: User
)