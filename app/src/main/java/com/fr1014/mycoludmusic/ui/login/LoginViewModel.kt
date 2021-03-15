package com.fr1014.mycoludmusic.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fr1014.mycoludmusic.data.DataRepository
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
    private val verifyStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    fun getVerifyStatus(): LiveData<Boolean> = verifyStatus

    fun getUserEntity(): LiveData<UserEntity> {
        return userEntityLive
    }

    fun getWYUserProfile(phone: String, password: String) {
        addSubscribe(model.getWYUserProfile(phone, password)
                .compose(RxSchedulers.apply())
                .subscribe({
                    userEntityLive.postValue(it)
                }, {
                    Log.d("hello", "getWYUserProfile: " + it.message)
                    CommonUtils.toastLong("需要网易云的账号登录哦，手机号+密码登录的形式")
                })
        )
    }

    fun getWYPhoneCaptcha(phone: String, captcha: String) {
        addSubscribe(model.getWYPhoneCaptcha(phone, captcha)
                .compose(RxSchedulers.apply())
                .subscribe {
                    if (it.code == 200) {
                        getUserAccountInfo()
                    }
                })
    }

    fun getUserAccountInfo() {
        addSubscribe(model.userAccountInfo
                .compose(RxSchedulers.apply())
                .subscribe {
                    userEntityLive.postValue(it)
                })
    }

    fun getWYPhoneVerify(phone: String) {
        addSubscribe(model.getWYPhoneVerify(phone)
                .compose(RxSchedulers.apply())
                .subscribe {
                    verifyStatus.postValue(it.code == 200)
                    val message = if (it.code == 200) "验证码发送成功" else it.message
                    CommonUtils.toastShort(message)
                })
    }
}