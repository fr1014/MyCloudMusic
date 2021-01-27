package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search

data class SearchHotDetail(
    val code: Int,
    val data: List<SearchHotBean>,
    val message: String
)

data class SearchHotBean(
    val alg: String,
    val content: String,
    val iconType: Int,
    val iconUrl: String,
    val score: Long,
    val searchWord: String,
    val source: Int,
    val url: String
)