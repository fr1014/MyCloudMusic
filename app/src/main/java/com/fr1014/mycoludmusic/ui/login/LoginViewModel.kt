package com.fr1014.mycoludmusic.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Profile
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.UserEntity
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mymvvm.base.BaseViewModel

/**
 * Create by fanrui on 2021/1/12
 * Describe:
 */
class LoginViewModel(application: Application, model: DataRepository) : BaseViewModel<DataRepository>(application, model) {
    private val userEntityLive: MutableLiveData<UserEntity> by lazy {
        MutableLiveData()
    }

    fun getUserEntity() : LiveData<UserEntity>{
        return userEntityLive
    }

    fun getWYUserProfile(phone: String, password: String) {
        addSubscribe(model.getWYUserProfile(phone, password)
                .compose(RxSchedulers.apply())
                .subscribe({
                    userEntityLive.postValue(it)
                }, {
                    Log.d("hello", "getWYUserProfile: " + it.message)
                    CommonUtils.toastLong("需要网易云的账号登录哦，目前仅支持手机号+密码登录的形式")
                })
        )
    }
}