package com.fr1014.mycoludmusic.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSearchResultBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.ui.search.paging2.SearchResultAdapter
import com.fr1014.mymvvm.base.BaseFragment

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding, SearchViewModel>() {
    private lateinit var viewAdapter: SearchResultAdapter
    private var searchKey: String? = null

    companion object {
        const val SEARCH_KEY = "SEARCH_WORD"

        fun createBundle(searchWord: String): Bundle {
            return Bundle().apply {
                putString(SEARCH_KEY, searchWord)
            }
        }
    }

    override fun initParam() {
        arguments?.let {
            searchKey = it.getString(SEARCH_KEY)
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentSearchResultBinding {
        return FragmentSearchResultBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SearchViewModel? {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return activity?.let { ViewModelProvider(it, factory).get(SearchViewModel::class.java) }
    }

    override fun initView() {
        initAdapter()
        initListener()
    }

    private fun initAdapter() {
        viewAdapter = SearchResultAdapter(mViewModel)
        mViewBinding.rvSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun initListener() {
        mViewBinding.includePlayAll.llPlaylist.setOnClickListener {
            AudioPlayer.get().addAndPlay(viewAdapter.currentList?.toList())
        }
    }

    override fun initData() {
        searchKey?.let {
            search(it)
        }
    }

    fun search(searchWord: String) {
        mViewModel.search(searchWord).observe(viewLifecycleOwner, Observer { pagedList ->
            mViewBinding.includePlayAll.llPlaylist.visibility = View.VISIBLE
            //paging2
            viewAdapter.submitList(pagedList)
        })

        mViewModel.networkStatus.observe(viewLifecycleOwner, Observer { netStatus ->
            viewAdapter.updateNetworkStatus(netStatus)
            if (netStatus == NetworkStatus.COMPLETED) {

            }
        })
    }


}