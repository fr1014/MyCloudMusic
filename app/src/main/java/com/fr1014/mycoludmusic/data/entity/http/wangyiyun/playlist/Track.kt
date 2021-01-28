package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist

data class Track(
        val a: Any,
        val al: Al,
        val alg: String,
        val alia: List<String>,
        val ar: List<Ar>,
        val cd: String,
        val cf: String,
        val copyright: Int,
        val cp: Int,
        val crbt: Any,
        val djId: Int,
        val dt: Int,
        val fee: Int,
        val ftype: Int,
        val h: H,
        val id: Long,
        val l: L,
        val m: M,
        val mark: Long,
        val mst: Int,
        val mv: Int,
        val name: String,
        val no: Int,
        val noCopyrightRcmd: Any,
        val originCoverType: Int,
        val originSongSimpleData: Any,
        val pop: Int,
        val pst: Int,
        val publishTime: Long,
        val rt: String,
        val rtUrl: Any,
        val rtUrls: List<Any>,
        val rtype: Int,
        val rurl: Any,
        val s_id: Int,
        val single: Int,
        val st: Int,
        val t: Int,
        val v: Int
)

data class Al(
        val id: Long,
        val name: String,
        val pic: Long,
        val picUrl: String,
        val pic_str: String,
        val tns: List<String>
)

data class Ar(
        val alias: List<Any>,
        val id: Long,
        val name: String,
        val tns: List<Any>
)

data class H(
        val br: Int,
        val fid: Int,
        val size: Int,
        val vd: Float
)

data class L(
        val br: Int,
        val fid: Int,
        val size: Int,
        val vd: Float
)

data class M(
        val br: Int,
        val fid: Int,
        val size: Int,
        val vd: Float
)

data class TrackIds(
        val id: Long,
        val v: Int,
        val at: Long,
        val alg: Any,
        val ratio: Int,
        val lr: Int
)