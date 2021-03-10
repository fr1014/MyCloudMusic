package com.fr1014.mycoludmusic.ui.home.songsale.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.Product
import com.fr1014.mycoludmusic.databinding.FragmentAlbumDetailBinding
import com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend.DayRecommendAdapter
import com.fr1014.mycoludmusic.ui.home.songsale.SongSaleViewModel
import com.fr1014.mymvvm.base.BaseFragment

const val ARG_ID = "arg_id"
const val ARG_COVER = "arg_cover"
const val ARG_NAME = "arg_name"
const val ARG_ARTIST = "arg_artist"

class AlbumDetailFragment : BaseFragment<FragmentAlbumDetailBinding, SongSaleViewModel>() {
    private var id: Long? = null
    private var cover: String? = null
    private var name: String? = null
    private var artist: String? = null
    private lateinit var mAdapter: DayRecommendAdapter

    override fun initParam() {
        arguments?.let {
            id = it.getLong(ARG_ID)
            cover = it.getString(ARG_COVER)
            name = it.getString(ARG_NAME)
            artist = it.getString(ARG_ARTIST)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(product: Product) =
                AlbumDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_ID, product.albumId)
                        putString(ARG_COVER, product.coverUrl)
                        putString(ARG_NAME, product.albumName)
                        putString(ARG_ARTIST, product.artistName)
                    }
                }
    }

    override fun initViewModel(): SongSaleViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SongSaleViewModel::class.java)
    }

    override fun getViewBinding(container: ViewGroup?) = FragmentAlbumDetailBinding.inflate(layoutInflater)

    override fun initView() {
        mAdapter = DayRecommendAdapter(R.layout.item_playlist_detail,false)
        mViewBinding.apply {
            rvAlbum.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }
    }

    override fun initData() {
        id?.let { mViewModel.getAlbumDetail(it) }
    }

    override fun initViewObservable() {
        mViewModel.getAlbumDetail().observe(viewLifecycleOwner, Observer {
            mAdapter.setData(it)
        })
    }

    fun onBackPressed(){
        activity?.apply {
            if (activity is MainActivity){
                supportFragmentManager.beginTransaction()
                        .remove(this@AlbumDetailFragment)
                        .commitAllowingStateLoss()
            }
        }
    }
}