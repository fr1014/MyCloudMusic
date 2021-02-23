package com.fr1014.mycoludmusic.utils

import com.fr1014.mycoludmusic.musicmanager.Music

object MusicUtils {

    /**
     * 判断两首歌曲是否为同一首歌曲
     */
    fun isSameMusic(first: Music, second: Music) :Boolean{
       return ((first.id != 0L && first.id == second.id) || (first.musicrid != null && first.musicrid == second.musicrid))
    }
}