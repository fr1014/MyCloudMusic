package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale

data class Product(
    val albumId: Long,
    val albumName: String,
    val albumType: Int,
    val artistName: String,
    val bundling: Int,
    val coverUrl: String,
    val newAlbum: Boolean,
    val newest: Boolean,
    val period: Int,
    val price: Float,
    val rank: Int,
    val rankIncr: Int,
    val saleNum: Long,
    val saleType: Int,
    val status: Int
)