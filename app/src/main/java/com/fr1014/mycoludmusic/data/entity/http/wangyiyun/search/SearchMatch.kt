package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search

data class SearchRecommend(
    val code: Int,
    val result: Result
)

data class Result(
    val allMatch: List<MatchBean>
)

data class MatchBean(
    val alg: String,
    val feature: String,
    val keyword: String,
    val lastKeyword: String,
    val type: Int
)