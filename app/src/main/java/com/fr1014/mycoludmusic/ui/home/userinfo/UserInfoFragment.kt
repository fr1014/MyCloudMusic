package com.fr1014.mycoludmusic.ui.home.userinfo

import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
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

    override fun initViewObservable() {
        mViewModel.getWYPlayList().observe(viewLifecycleOwner, Observer {
            if (!CollectionUtils.isEmptyList(it)){
                myLikePlayList = it[0]
                Glide.with(this)
                        .load(myLikePlayList?.coverImgUrl)
                        .into(mViewBinding.ivCoverLike)
            }
        })
    }
}