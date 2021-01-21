package com.fr1014.mycoludmusic.data.entity.http.wangyiyun

data class Privileges(
        val id: Long,
        val fee: Int,
        val payed: Int,
        val st: Int,
        val pl: Int,
        val dl: Int,
        val sp: Int,
        val cp: Int,
        val subp: Int,
        val cs: Boolean,
        val maxbr: Int,
        val fl: Int,
        val toast: Boolean,
        val flag: Int,
        val preSell: Boolean,
        val playMaxbr: Int,
        val downloadMaxbr: Int,
        val chargeInfoList: List<ChargeInfoList>
)

data class ChargeInfoList(
        val rate: Int,
        val chargeUrl: Any,
        val chargeMessage: Any,
        val chargeType: Int
)
