package com.fr1014.mycoludmusic.ui.login

import android.view.ViewGroup
import androidx.navigation.Navigation
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.databinding.FragmentSplashBinding
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mymvvm.base.BaseFragment

class SplashFragment : BaseFragment<FragmentSplashBinding, LoginViewModel>() {

    override fun getViewBinding(container: ViewGroup?) = FragmentSplashBinding.inflate(layoutInflater)

    override fun initView() {
        mViewBinding.tvLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.login)
        }
        mViewBinding.tvVerify.setOnClickListener {
            CommonUtils.toastShort("暂不支持该登录方式")
//            Navigation.findNavController(it).navigate(R.id.login_verify)
        }
    }
}