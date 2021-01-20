package com.fr1014.mycoludmusic.ui.home.playlist.paging2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fr1014.mycoludmusic.musicmanager.Music

class PlayListDataSourceFactory(private val ids: Array<Long>) : DataSource.Factory<Int, Music>() {
    private val _playListDataSource = MutableLiveData<PlayListDataSource>()
    val playListDataSource: LiveData<PlayListDataSource> = _playListDataSource

    override fun create(): DataSource<Int, Music> {
        return PlayListDataSource(ids).also {
            _playListDataSource.postValue(it)
        }
    }
}