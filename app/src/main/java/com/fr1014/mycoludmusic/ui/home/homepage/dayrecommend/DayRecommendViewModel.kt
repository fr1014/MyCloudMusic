package com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.DayRecommend
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mymvvm.base.BaseViewModel
import io.reactivex.functions.Consumer

class DayRecommendViewModel(application: Application,model:DataRepository) : BaseViewModel<DataRepository>(application, model) {

    private val dayRecommendDataLive : MutableLiveData<DayRecommend> by lazy {
        MutableLiveData()
    }

    fun getDayRecommendData():LiveData<DayRecommend> = dayRecommendDataLive

    fun getDayRecommend(){
        addSubscribe(model.wyDayRecommend
                .compose(RxSchedulers.apply())
                .subscribe(Consumer {
                    dayRecommendDataLive.postValue(it)
                }))
    }
}