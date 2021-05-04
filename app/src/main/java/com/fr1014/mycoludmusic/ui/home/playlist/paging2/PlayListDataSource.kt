package com.fr1014.mycoludmusic.ui.home.playlist.paging2

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.QueryComment
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.http.WYYServiceProvider
import com.fr1014.mycoludmusic.http.api.WYApiService
import com.fr1014.mycoludmusic.musicmanager.Music
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class PlayListDataSource(private val ids: Array<Long>) : RxPagingSource<QueryComment, Music>() {

    private val mWYYService by lazy {
        WYYServiceProvider.create(WYApiService::class.java)
    }

    override fun getRefreshKey(state: PagingState<QueryComment, Music>): QueryComment? {
        return null
    }

    override fun loadSingle(params: LoadParams<QueryComment>): Single<LoadResult<QueryComment, Music>> {
        val nextPageNumber = params.key?.pageKey ?: 0
        val idsRange = ids.getRangeIds(nextPageNumber, 299)
        return mWYYService.getWYSongDetail(idsRange)
                .subscribeOn(Schedulers.io())
                .map<LoadResult<QueryComment, Music>> {
                    val musics: MutableList<Music> = ArrayList()
                    val songs: List<SongsBean> = it.songs
                    for (song in songs) {
                        val music = Music()
                        val sb = StringBuilder()
                        for (i in song.ar.indices) {
                            val ar = song.ar[i]
                            if (i < song.ar.size - 1) {
                                sb.append(ar.name).append('&')
                            } else {
                                sb.append(ar.name)
                            }
                        }
                        music.apply {
                            id = song.id
                            artist = sb.toString()
                            title = song.name
                            imgUrl = song.al.picUrl
                            duration = song.dt
                            album = song.al.name
                            mvId = song.mv
                        }
                        musics.add(music)
                    }

                    LoadResult.Page(
                            data = musics,
                            prevKey = null,
                            nextKey = QueryComment(nextPageNumber.plus(300), ids.getRangeIds(nextPageNumber, 299))
                    )
                }
                .onErrorReturn { e ->
                    when (e) {
                        // Retrofit calls that return the body type throw either IOException for
                        // network failures, or HttpException for any non-2xx HTTP status codes.
                        // This code reports all errors to the UI, but you can inspect/wrap the
                        // exceptions to provide more context.
                        is NullPointerException -> LoadResult.Error(e)
                        is IOException -> LoadResult.Error(e)
                        is HttpException -> LoadResult.Error(e)
                        else -> throw e
                    }
                }

    }

    fun Array<Long>.getRangeIds(start: Int, loadRange: Int): String {
        var mLoadRange: Int
        if (isNotEmpty()) {
            mLoadRange = loadRange
            if (start >= size) {
                return ""
            }
            if ((start + loadRange) >= size) {
                mLoadRange = size - start - 1
            }
            val idsRange = StringBuilder()
            for (position in start..(start + mLoadRange)) {
                if (position == size) {
                    break
                }
                idsRange.append(this[position])
                idsRange.append(",")
            }
            return idsRange.substring(0, idsRange.length - 1)
        }
        return ""
    }
}