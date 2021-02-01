package com.fr1014.mycoludmusic.ui.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.customview.blurimageview.FastBlurUtil
import com.fr1014.mycoludmusic.databinding.ActivityLoginBinding
import com.fr1014.mycoludmusic.eventbus.LoginStatusEvent
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.utils.BlurImageUtils
import com.fr1014.mymvvm.base.BaseActivity
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): LoginViewModel {
        val factory = AppViewModelFactory.getInstance(application)
        return ViewModelProvider(this,factory).get(LoginViewModel::class.java)
    }

    override fun initView() {
        mViewBinding.apply {
//            ivBg.setImageBitmap(FastBlurUtil.doBlur(BitmapFactory.decodeResource(resources, R.drawable.bg_login),5,true))
            btLogin.setOnClickListener {
                mViewModel.getWYUserProfile(account.text.toString(),password.text.toString())
            }
        }
    }

    override fun initViewObservable() {
        mViewModel.getProfile().observe(this, Observer {
            Preferences.saveUserProfile(it)
            EventBus.getDefault().post(LoginStatusEvent(true))
        })
    }
}