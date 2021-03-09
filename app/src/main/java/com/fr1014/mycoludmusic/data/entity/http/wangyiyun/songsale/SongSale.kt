package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale

//数字专辑榜
data class SongSale(
    val code: Int,
    val hasMore: Boolean,
    val products: List<Product>
)