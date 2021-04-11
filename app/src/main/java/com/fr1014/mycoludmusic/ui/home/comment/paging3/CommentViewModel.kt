package com.fr1014.mycoludmusic.ui.home.comment.paging3

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mymvvm.base.BaseViewModel

/**
 * Create by fanrui on 2021/4/10
 * Describe:
 */
class CommentViewModel(application: Application, model: DataRepository) : BaseViewModel<DataRepository>(application, model) {

    fun commentList(type: Int, id: Long, pageSize: Int) = Pager(PagingConfig(pageSize = 6)) {
        CommentPagingSource(type, id, pageSize)
    }.observable.cachedIn(viewModelScope)
}