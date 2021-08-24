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

data class MusicCoverEvent(val type: CoverStatusType) {
    var music: Music? = null
    var coverLocal: Bitmap? = null

    constructor(type: CoverStatusType, music: Music, coverLocal: Bitmap) : this(type) {
        this.music = music;
        this.coverLocal = coverLocal;
    }
}