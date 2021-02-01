package com.fr1014.mycoludmusic.ui.login

import androidx.lifecycle.ViewModelProvider
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.databinding.ActivityLoginBinding
import com.fr1014.mycoludmusic.utils.StatusBarUtils
import com.fr1014.mymvvm.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): LoginViewModel {
        val factory = AppViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun initView() {
        //设置statusBar字体为黑色
        StatusBarUtils.setImmersiveStatusBar(window, false)
    }
}