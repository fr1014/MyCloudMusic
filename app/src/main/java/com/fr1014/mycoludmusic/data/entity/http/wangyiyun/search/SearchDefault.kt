package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search

data class SearchDefault(
        val code: Int,
        val data: SearChDefaultBean,
        val message: Any
)

data class SearChDefaultBean
(
    val action: Int,
    val alg: String,
    val gap: Int,
    val realkeyword: String,
    val searchType: Int,
    val showKeyword: String,
    val source: Any
)