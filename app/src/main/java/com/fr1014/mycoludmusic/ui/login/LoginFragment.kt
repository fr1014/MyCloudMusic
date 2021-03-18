package com.fr1014.mycoludmusic.ui.login

import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.app.BaseConfig
import com.fr1014.mycoludmusic.databinding.FragmentLoginBinding
import com.fr1014.mycoludmusic.eventbus.LoginStatusEvent
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.utils.AccountUtils
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mymvvm.base.BaseFragment
import org.greenrobot.eventbus.EventBus

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun getViewBinding(container: ViewGroup?) = FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {
        mViewBinding.apply {
            btLogin.setOnClickListener {
                mViewModel.getWYUserProfile(account.text.toString(), password.text.toString())
            }
        }
    }

    override fun initViewObservable() {
        mViewModel.getUserEntity().observe(this, Observer {
            if (it.code == 200){
                Preferences.saveUserProfile(it.profile)
                BaseConfig.refreshLoginStatus()
                AccountUtils.saveLoginTourist(false)
                startActivity(MainActivity::class.java)
                activity?.finish()
                CommonUtils.toastShort("登录成功，开启你的音乐之旅")
            }else{
                CommonUtils.toastShort(it.message)
            }
        })
    }
}