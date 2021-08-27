package com.fr1014.mycoludmusic.musicmanager.player

import android.graphics.Bitmap

/**
 * Time: 2021/8/11
 * Author: fanrui07
 * Description:
 */
enum class CoverStatusType {
    Loading,
    Success,
    Fail,
}

const val COVER_FROM_COMMON = "COMMON"
data class MusicCoverEvent(val type: CoverStatusType, val from: String) {
    var music: Music? = null
    var coverLocal: Bitmap? = null

    constructor(type: CoverStatusType,from: String, music: Music, coverLocal: Bitmap) : this(type, from) {
        this.music = music
        this.coverLocal = coverLocal
    }
}