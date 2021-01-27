package com.fr1014.mycoludmusic.ui.search

import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSearchRecommendBinding
import com.fr1014.mymvvm.base.BaseFragment

class SearchRecommendFragment : BaseFragment<FragmentSearchRecommendBinding,SearchViewModel>() {

    override fun getViewBinding(container: ViewGroup?): FragmentSearchRecommendBinding {
        return FragmentSearchRecommendBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SearchViewModel? {
        activity?.let {
            val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
            return ViewModelProvider(it, factory).get(SearchViewModel::class.java)
        }
        return null
    }

    override fun initView() {
        mViewBinding.blockSearchRecommend.setViewModel(mViewModel)
    }

    override fun initData() {
        mViewModel.searchHotDetail()
        mViewBinding.blockSearchRecommend.loading(true)
    }

    override fun initViewObservable() {
        mViewModel.getSearchHotDetail().observe(viewLifecycleOwner, Observer {
            mViewBinding.blockSearchRecommend.apply {
                loading(false)
                setData(it)
            }
        })
    }

}