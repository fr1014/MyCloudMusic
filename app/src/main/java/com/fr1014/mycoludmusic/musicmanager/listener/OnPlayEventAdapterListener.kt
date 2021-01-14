package com.fr1014.mycoludmusic.musicmanager.listener

import com.fr1014.mycoludmusic.musicmanager.Music

abstract class OnPlayEventAdapterListener : OnPlayerEventListener {
    override fun onChange(music: Music) {
    }

    override fun onPlayerComplete() {
    }

    override fun onPlayerStart() {
    }

    override fun onPlayerPause() {
    }

    override fun onPublish(progress: Int) {
    }

    override fun onBufferingUpdate(percent: Int) {
    }
}