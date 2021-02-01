package com.fr1014.mycoludmusic.ui.login

import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.fr1014.mycoludmusic.databinding.FragmentLoginBinding
import com.fr1014.mycoludmusic.eventbus.LoginStatusEvent
import com.fr1014.mycoludmusic.musicmanager.Preferences
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
        mViewModel.getProfile().observe(this, Observer {
            Preferences.saveUserProfile(it)
            EventBus.getDefault().post(LoginStatusEvent(true))
        })
    }
}