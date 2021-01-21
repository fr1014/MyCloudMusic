package com.fr1014.mycoludmusic.ui.home.userinfo

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist
import com.fr1014.mycoludmusic.databinding.FragmentUserInfoBinding
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListDetailFragment
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.ScreenUtil
import com.fr1014.mymvvm.base.BaseFragment

/**
 * Create by fanrui on 2021/1/14
 * Describe:
 */
class UserInfoFragment : BaseFragment<FragmentUserInfoBinding, UserInfoViewModel>() {
    private var myLikePlayList: Playlist? = null

    override fun getViewBinding(container: ViewGroup?): FragmentUserInfoBinding {
        return FragmentUserInfoBinding.inflate(layoutInflater);
    }

    override fun initViewModel(): UserInfoViewModel? {
        return activity?.let {
            val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
            ViewModelProvider(it, factory).get(UserInfoViewModel::class.java)
        }
    }

    override fun initView() {
        mViewBinding.toolbar.clToolbar.background.alpha = 0
        initUserInfo()
        initListener()
    }

    private fun initUserInfo() {
        mViewBinding.apply {
            Preferences.getUserProfile()?.let {
                it.avatarUrl.let { url ->
                    Glide.with(userInfo.ivHead)
                            .load(url)
                            .into(object : CustomTarget<Drawable>() {
                                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                    userInfo.ivHead.setImageDrawable(resource)
                                    toolbar.ivTitle.setImageDrawable(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                            })
                }
                it.nickname.let { name ->
                    userInfo.tvName.text = name
                    toolbar.tvTitle.text = name
                }
            }
        }
    }

    private fun initListener() {
        mViewBinding.userLike.clPlaylist.setOnClickListener {
            val bundle = myLikePlayList?.id?.let { id ->
                PlayListDetailFragment.createBundle(id, myLikePlayList?.name, myLikePlayList?.coverImgUrl)
            }
            Navigation.findNavController(it).navigate(R.id.playListDetailFragment, bundle)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mViewBinding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > ScreenUtil.dp2px(context, 64F)) {
                    userTitleVisibility(View.VISIBLE)
                    context?.let {
                        mViewBinding.toolbar.root.setBackgroundColor(it.getColor(R.color.white))
                    }
                } else {
                    changeToolBarAlpha(scrollY)
                    userTitleVisibility(View.GONE)
                }
            }

        }
        mViewBinding.toolbar.ivBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun changeToolBarAlpha(scrollY: Int) {
        val alpha: Float = scrollY.div(ScreenUtil.dp2px(context, 64F).toFloat())
        mViewBinding.toolbar.clToolbar.background.alpha = (alpha * 255).toInt()
    }

    override fun initData() {
        mViewModel.getWYUserPlayList()
        mViewModel.getWYLevelInfo()
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        mViewModel.getWYPlayList().observe(viewLifecycleOwner, Observer {
            if (!CollectionUtils.isEmptyList(it)) {
                myLikePlayList = it[0]
                mViewBinding.userLike.apply {
                    tvCount.text = "${myLikePlayList?.trackCount}首"
                    if (myLikePlayList?.trackCount == 0) {
                        clPlaylist.isClickable = false
                    }
                    val options = RequestOptions().centerCrop().transform(RoundedCorners(30))
                    Glide.with(ivCoverLike)
                            .load(myLikePlayList?.coverImgUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .apply(options)
                            .into(ivCoverLike)
                }
                if (it.size > 2) {
                    mViewBinding.playlistCollection.apply {
                        setTitle("收藏歌单(${it.size - 2}个)")
                        setData(it.subList(2, it.size))
                    }
                } else {
                    mViewBinding.playlistCollection.visibility = View.GONE
                }
            } else {
                userLayoutVisibility(View.GONE)
            }
        })
        mViewModel.getLevelInfo().observe(viewLifecycleOwner, Observer {
            mViewBinding.userInfo.tvLevel.text = "Lv.${it.level}"
        })
    }

    private fun userLayoutVisibility(visibility: Int) {
        mViewBinding.userInfo.root.visibility = visibility
        mViewBinding.playlistCollection.visibility = visibility
    }

    private fun userTitleVisibility(visibility: Int) {
        mViewBinding.toolbar.llUserInfo.visibility = visibility
    }
}