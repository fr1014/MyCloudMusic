package com.fr1014.mycoludmusic.ui.search.paging2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fr1014.mycoludmusic.musicmanager.player.Music


/**
 * Create by fanrui on 2021/1/10
 * Describe:
 */
class SearchDataSourceFactory(private val searchKey : String) : DataSource.Factory<Int, Music>() {
    private val _searchDataSource = MutableLiveData<SearchDataSource>()
    val searchDataSource: LiveData<SearchDataSource> = _searchDataSource

    override fun create(): DataSource<Int, Music> {
        return SearchDataSource(searchKey).also {
            _searchDataSource.postValue(it)
        }
    }
}