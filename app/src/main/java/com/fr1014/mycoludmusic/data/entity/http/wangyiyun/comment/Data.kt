package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment

data class Data(
        val comments: List<CommentItem>,
        val currentComment: Any,
        val cursor: String,
        val hasMore: Boolean,
        val sortType: Int,
        val sortTypeList: List<SortType>,
        val totalCount: Int
)