package com.fr1014.mycoludmusic.ui.home.songsale.detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.Product
import com.fr1014.mycoludmusic.databinding.FragmentAlbumDetailBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend.DayRecommendAdapter
import com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend.getArInfo
import com.fr1014.mycoludmusic.ui.home.songsale.SongSaleViewModel
import com.fr1014.mycoludmusic.utils.BlurImageUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
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
    private lateinit var mHeaderView: View
    private var headerHeight = 0

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
        mAdapter = DayRecommendAdapter(R.layout.item_playlist_detail, false)
        mViewBinding.apply {
            rvAlbum.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
                mHeaderView = layoutInflater.inflate(R.layout.header_detail_song_sale, this, false)
            }
            context?.let {
                GlideApp.with(it)
                        .asBitmap()
                        .load(cover)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                ivBg.setImageDrawable(BlurImageUtils.getForegroundDrawable(context, resource, 5))
                                mHeaderView.findViewById<ImageView>(R.id.iv_head).setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                        })

            }
        }
        mAdapter.headerView = mHeaderView
        initListener()
    }

    fun initListener() {
        mViewBinding.apply {
            rvAlbum.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val scrollY = getScrollY()
                    ivBg.alpha = scrollY.toFloat() / ScreenUtils.dp2px(146f)
                    tvTitle.text = if (scrollY > ScreenUtils.dp2px(146f)) name else "数字专辑"
                    playAll.llPlaylist.visibility = if (scrollY > ScreenUtils.dp2px(146f)) View.VISIBLE else View.GONE
                }
            })

            ivBack.setOnClickListener {
                onBackPressed()
            }

            playAll.llPlaylist.setOnClickListener {
                addAndPlay()
            }

            mHeaderView.findViewById<LinearLayout>(R.id.play_all).setOnClickListener {
                addAndPlay()
            }
        }
    }

    private fun addAndPlay() {
        val musics = ArrayList<Music>()
        for (data in mAdapter.datas) {
            musics.add(Music(data.id, data.getArInfo(), data.name, "", cover, ""))
        }
        AudioPlayer.get().addAndPlay(musics)
    }

    private fun getScrollY(): Int {
        val layoutManager = mViewBinding.rvAlbum.layoutManager as LinearLayoutManager
        // 获取第一个可见item的位置
        val position = layoutManager.findFirstVisibleItemPosition()
        if (position == 0) {
            // 获取header
            val headerView = layoutManager.findViewByPosition(0)
            // 获取第一个可见item的高度
            headerHeight = headerView!!.height
        }

        // 获取第一个可见item
        val firstVisiableChildView = layoutManager.findViewByPosition(position)
        // 获取第一个可见item的高度
        val itemHeight = firstVisiableChildView!!.height
        // 获取第一个可见item的位置
        return if (position == 0) {
            position * itemHeight - firstVisiableChildView.top
        } else {
            position * itemHeight - firstVisiableChildView.top + headerHeight
        }
    }

    override fun initData() {
        id?.let { mViewModel.getAlbumDetail(it) }
    }

    override fun initViewObservable() {
        mViewModel.getAlbumDetail().observe(viewLifecycleOwner, Observer {
            mViewBinding.includeLoadingView.llLoading.visibility = View.GONE
            mAdapter.setData(it)
            it.size.toString().apply {
                mViewBinding.playAll.tvCount.text = this
                mHeaderView.findViewById<TextView>(R.id.tv_count).text = this
            }
        })
    }

    fun onBackPressed() {
        activity?.apply {
            if (activity is MainActivity) {
                supportFragmentManager.beginTransaction()
                        .remove(this@AlbumDetailFragment)
                        .commitAllowingStateLoss()
            }
        }
    }
}