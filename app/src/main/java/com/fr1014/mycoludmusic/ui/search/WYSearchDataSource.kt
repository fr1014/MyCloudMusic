package com.fr1014.mycoludmusic.ui.search

import androidx.paging.PageKeyedDataSource
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchEntity.SearchBean

/**
 * Create by fanrui on 2021/1/8
 * Describe:
 */
class WYSearchDataSource : PageKeyedDataSource<Int, SearchBean>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, SearchBean>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchBean>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchBean>) {
        TODO("Not yet implemented")
    }
}