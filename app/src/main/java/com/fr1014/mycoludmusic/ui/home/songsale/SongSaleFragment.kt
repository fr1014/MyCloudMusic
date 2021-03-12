package com.fr1014.mycoludmusic.ui.home.songsale

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSongSaleBinding
import com.fr1014.mymvvm.base.BaseFragment

class SongSaleFragment : BaseFragment<FragmentSongSaleBinding, SongSaleViewModel>() {
    private var mAlbumSaleFragment: Fragment? = null
    private var isAlbumSaleFragmentShow = false
    private var mSingleSaleFragment: Fragment? = null
    private var isSingleSaleFragmentShow = false

    override fun getViewBinding(container: ViewGroup?): FragmentSongSaleBinding {
        return FragmentSongSaleBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SongSaleViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SongSaleViewModel::class.java)
    }

    override fun initView() {
        showAlbumSaleFragment()

        mViewBinding.apply {
            tvAlbum.setOnClickListener {
                hideSingleSaleFragment()
                showAlbumSaleFragment()
            }

            tvSingle.setOnClickListener {
                hideAlbumSaleFragment()
                showSingleSaleFragment()
            }

            ivBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showAlbumSaleFragment() {
        if (isAlbumSaleFragmentShow) return
        mViewBinding.apply {
            tvAlbum.background = context?.getDrawable(R.drawable.bg_song_sale)
            tvSingle.background = null
        }

        val beginTransaction = parentFragmentManager.beginTransaction()
        if (mAlbumSaleFragment == null) {
            mAlbumSaleFragment = AlbumSaleFragment.getInstance(TYPE_ALBUM)
            beginTransaction.replace(R.id.container, mAlbumSaleFragment!!)
        } else {
            beginTransaction.show(mAlbumSaleFragment!!)
        }
        beginTransaction.commit()
        isAlbumSaleFragmentShow = true
    }

    private fun hideAlbumSaleFragment() {
        val beginTransaction = parentFragmentManager.beginTransaction()
        mAlbumSaleFragment?.apply {
            beginTransaction.hide(this)
            beginTransaction.commit()
        }
        isAlbumSaleFragmentShow = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showSingleSaleFragment() {
        if (isSingleSaleFragmentShow) return
        mViewBinding.apply {
            tvAlbum.background = null
            tvSingle.background = context?.getDrawable(R.drawable.bg_song_sale)
        }

        val beginTransaction = parentFragmentManager.beginTransaction()
        if (mSingleSaleFragment == null) {
            mSingleSaleFragment = AlbumSaleFragment.getInstance(TYPE_SINGLE)
            beginTransaction.add(R.id.container, mSingleSaleFragment!!)
        } else {
            beginTransaction.show(mSingleSaleFragment!!)
        }
        beginTransaction.commit()
        isSingleSaleFragmentShow = true
    }

    private fun hideSingleSaleFragment() {
        val beginTransaction = parentFragmentManager.beginTransaction()
        mSingleSaleFragment?.apply {
            beginTransaction.hide(this)
            beginTransaction.commit()
        }
        isSingleSaleFragmentShow = false
    }
}