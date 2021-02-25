package com.fr1014.mycoludmusic.ui.home.songsale

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSongSaleBinding
import com.fr1014.mymvvm.base.BaseFragment

class SongSaleFragment : BaseFragment<FragmentSongSaleBinding, SongSaleViewModel>() {

    override fun getViewBinding(container: ViewGroup?): FragmentSongSaleBinding {
        return FragmentSongSaleBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SongSaleViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SongSaleViewModel::class.java)
    }

    override fun initView() {

    }

}