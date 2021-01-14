package com.fr1014.mycoludmusic.ui.home.userinfo

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Playlist
import com.fr1014.mycoludmusic.databinding.FragmentUserInfoBinding
import com.fr1014.mycoludmusic.ui.home.toplist.PlayListDetailFragment
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mymvvm.base.BaseFragment

/**
 * Create by fanrui on 2021/1/14
 * Describe:
 */
class UserInfoFragment : BaseFragment<FragmentUserInfoBinding,UserInfoViewModel>() {
    private var myLikePlayList : Playlist? = null

    override fun getViewBinding(container: ViewGroup?): FragmentUserInfoBinding {
        return FragmentUserInfoBinding.inflate(layoutInflater);
    }

    override fun initViewModel(): UserInfoViewModel? {
        return activity?.let {
            val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
            ViewModelProvider(it,factory).get(UserInfoViewModel::class.java)
        }
    }

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        mViewBinding.clLike.setOnClickListener {
            val bundle = myLikePlayList?.id?.let {
                id -> PlayListDetailFragment.createBundle(id, myLikePlayList?.name, myLikePlayList?.coverImgUrl)
            }
            Navigation.findNavController(it).navigate(R.id.playListDetailFragment,bundle)
        }
    }

    override fun initData() {
        mViewModel.getWYUserPlayList()
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        mViewModel.getWYPlayList().observe(viewLifecycleOwner, Observer {
            if (!CollectionUtils.isEmptyList(it)){
                myLikePlayList = it[0]
                val options = RequestOptions().centerCrop().transform(RoundedCorners(30))
                Glide.with(this)
                        .load(myLikePlayList?.coverImgUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .apply(options)
                        .into(mViewBinding.ivCoverLike)
                mViewBinding.apply {
                    tvCount.text = "${myLikePlayList?.trackCount}首"
                }
            }
        })
    }
}