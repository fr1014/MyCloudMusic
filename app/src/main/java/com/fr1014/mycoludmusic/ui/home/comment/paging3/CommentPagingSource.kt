package com.fr1014.mycoludmusic.ui.home.comment.paging3

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.CommentItem
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.QueryComment
import com.fr1014.mycoludmusic.http.WYYServiceProvider
import com.fr1014.mycoludmusic.http.api.WYApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

/**
 * Create by fanrui on 2021/4/10
 * Describe:
 */
class CommentPagingSource(val type: Int, val id: Long, val pageSize: Int) : RxPagingSource<QueryComment, CommentItem>() {

    private val mWYYService by lazy {
        WYYServiceProvider.create(WYApiService::class.java)
    }

    override fun loadSingle(params: LoadParams<QueryComment>): Single<LoadResult<QueryComment, CommentItem>> {
        // Start refresh at page 1 if undefined.
        val nextPageNumber = params.key?.pageKey ?: 1
        val cursor = params.key?.cursor
        return mWYYService
                .getWYComment(type, id, 3, cursor, pageSize, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map<LoadResult<QueryComment, CommentItem>> { commentIem ->
                    LoadResult.Page(
                            data = commentIem.data.comments,
                            prevKey = null,
                            nextKey = QueryComment(nextPageNumber.plus(1), commentIem.data.cursor)
                    )
                }
                .onErrorReturn { e ->
                    when (e) {
                        // Retrofit calls that return the body type throw either IOException for
                        // network failures, or HttpException for any non-2xx HTTP status codes.
                        // This code reports all errors to the UI, but you can inspect/wrap the
                        // exceptions to provide more context.
                        is IOException -> LoadResult.Error(e)
                        is HttpException -> LoadResult.Error(e)
                        else -> throw e
                    }
                }
    }


    override fun getRefreshKey(state: PagingState<QueryComment, CommentItem>): QueryComment? {
        return null
    }
}