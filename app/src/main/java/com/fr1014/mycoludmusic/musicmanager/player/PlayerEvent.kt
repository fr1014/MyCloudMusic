package com.fr1014.mycoludmusic.musicmanager.player

/**
 * Time: 2021/8/9
 * Author: fanrui07
 * Description:
 */

enum class PlayerType {
    OnChange,
    OnPlayerComplete,
    OnPlayerStart,
    OnPlayerPause,
    OnPublish,
    OnBufferingUpdate,
    OnPlayListChange
}

data class PlayerEvent(var type: PlayerType) {
    var music: Music? = null
    var progress: Int = 0  // 更新进度
    var percent: Int = 0   // 缓冲百分比
    var musicList: List<Music>? = null

    constructor(music: Music, type: PlayerType) : this(type) {
        this.music = music
    }

    constructor(percent: Int, type: PlayerType) : this(type) {
        this.percent = percent
    }

    constructor(type: PlayerType, progress: Int) : this(type) {
        this.progress = progress
    }

    constructor(musicList: List<Music>, type: PlayerType) : this(type) {
        this.musicList = musicList
    }
}