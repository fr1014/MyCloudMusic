package com.fr1014.mycoludmusic.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Profile
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mymvvm.base.BaseViewModel
import io.reactivex.functions.Consumer

/**
 * Create by fanrui on 2021/1/12
 * Describe:
 */
class LoginViewModel(application: Application, model: DataRepository) : BaseViewModel<DataRepository>(application, model) {
    private val profile: MutableLiveData<Profile> by lazy {
        MutableLiveData()
    }

    fun getProfile() : LiveData<Profile>{
        return profile
    }

    fun getWYUserProfile(phone: String, password: String) {
        addSubscribe(model.getWYUserProfile(phone, password)
                .compose(RxSchedulers.apply())
                .subscribe({
                    profile.postValue(it.profile)
                }, {
                    Log.d("hello", "getWYUserProfile: "+it.message)
                })
        )
    }
}