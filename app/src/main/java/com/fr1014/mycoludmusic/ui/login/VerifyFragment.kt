package com.fr1014.mycoludmusic.ui.login

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.databinding.FragmentVerifyBinding
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mymvvm.base.BaseFragment

class VerifyFragment : BaseFragment<FragmentVerifyBinding, LoginViewModel>() {

    override fun getViewBinding(container: ViewGroup?) = FragmentVerifyBinding.inflate(layoutInflater)

    override fun initView() {
        mViewBinding.apply {
            btLogin.setOnClickListener {
                if (btLogin.text.equals("登录")) {
                    mViewModel.getWYPhoneCaptcha(phone.text.toString(), captcha.text.toString())
                } else {
                    mViewModel.getWYPhoneVerify(phone.text.toString())
                }
            }
        }
    }

    override fun initData() {
        mViewModel.getUserAccountInfo()
    }

    override fun initViewObservable() {
        mViewModel.getVerifyStatus().observe(this, Observer {
            mViewBinding.apply {
                if (it) {
                    captcha.visibility = View.VISIBLE
                    btLogin.text = "登录"
                } else {
                    btLogin.text = "获取验证码"
                }
            }
        })
        mViewModel.getUserEntity().observe(this, Observer {
            if (it.code == 200) {
                Preferences.saveUserProfile(it.profile)
                startActivity(MainActivity::class.java)
                activity?.finish()
                CommonUtils.toastShort("登录成功，开启你的音乐之旅")
            } else {
                CommonUtils.toastShort(it.message)
            }
        })
    }
}