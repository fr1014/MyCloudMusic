package com.fr1014.mycoludmusic.ui.home.songsale

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.Product
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import io.reactivex.functions.Consumer

class SongSaleViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    private val productsLive: MutableLiveData<List<Product>> by lazy {
        MutableLiveData()
    }

    private val albumDetailLive: MutableLiveData<List<SongsBean>> by lazy {
        MutableLiveData()
    }

    fun getProducts(): LiveData<List<Product>> = productsLive

    fun getAlbumDetail(): LiveData<List<SongsBean>> = albumDetailLive

    fun getSongSaleList(type: String, albumType: Int) {
        addSubscribe(
                model.getWYSongSaleList(type, albumType)
                        .compose(RxSchedulers.apply())
                        .subscribe({
                            productsLive.postValue(it.products)
                        }, {
                            Log.d("hello", "请求参数: type = $type, albumType = $albumType")
                            Log.d("hello", "getSongSaleList: " + it.message)
                        })
        )
    }

    fun getAlbumDetail(id: Long) {
        addSubscribe(
                model.getWYAlbumDetail(id)
                        .compose(RxSchedulers.apply())
                        .subscribe({
                            albumDetailLive.postValue(it.songs)
                        }, {
                            Log.d("hello", "getAlbumDetail: " + it.message)
                        })
        )
    }
}