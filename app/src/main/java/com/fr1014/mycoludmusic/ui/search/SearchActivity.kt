package com.fr1014.mycoludmusic.ui.search

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.SourceHolder
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.base.BasePlayActivity
import com.fr1014.mycoludmusic.customview.PlayStatusBarView
import com.fr1014.mycoludmusic.data.source.local.room.DBManager
import com.fr1014.mycoludmusic.databinding.ActivitySearchBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.ui.search.paging2.SearchResultAdapter
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.CoverLoadUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils

class SearchActivity : BasePlayActivity<ActivitySearchBinding, SearchViewModel>() {
    private lateinit var viewAdapter: SearchResultAdapter
    private var statusBarView: PlayStatusBarView? = null
    private var source: String? = null

    override fun getViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater).also { mViewBinding = it }
    }

    /**
     * 沉浸式状态栏
     */
    private fun initSystemBar() {
        val top = ScreenUtils.getStatusBarHeight()
        mViewBinding.llSearch.setPadding(0, top, 0, 0)
    }

    override fun initViewModel(): SearchViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SearchViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        initSystemBar()
        //避免自动弹出输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mViewBinding.includePlayAll.llPlaylist.visibility = View.GONE
        initListener()
    }

    override fun onServiceBound() {
        DBManager.get().getLocalMusicList(false).observe(this, Observer {
            if (!CollectionUtils.isEmptyList(it)) {
                if (statusBarView == null) {
                    statusBarView = PlayStatusBarView(this, supportFragmentManager)
                    statusBarView?.let { view ->
                        view.onPlayEventListener?.let { listener ->
                            AudioPlayer.get().addOnPlayEventListener(listener)
                        }
                        CoverLoadUtils.get().registerLoadListener(view)
                        mViewBinding.flPlaystatus.addView(statusBarView)
                        if (view.visibility != View.VISIBLE) {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    override fun initData() {
        source = SourceHolder.get().source
    }

    private fun initAdapter() {
        viewAdapter = SearchResultAdapter(mViewModel)
        mViewBinding.rvSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun initListener() {
        mViewBinding.ivBack.setOnClickListener { finish() }
        mViewBinding.etKeywords.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 当按了搜索之后关闭软键盘
                (mViewBinding.etKeywords.context.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        this@SearchActivity.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
                //viewModel.getSearchEntityWYY(binding.etKeywords.getText().toString(), 0);
                //viewModel.getSearchEntityKW(mViewBinding.etKeywords.getText().toString(), 30);

                val searchKey = mViewBinding.etKeywords.text.toString()
                mViewBinding.includePlayAll.llPlaylist.visibility = View.VISIBLE
                mViewModel.search(searchKey).observe(this, Observer { it ->
                    //paging2
                    viewAdapter.submitList(it)
                })

                mViewModel.networkStatus.observe(this, Observer {
                    viewAdapter.updateNetworkStatus(it)
                    if (it == NetworkStatus.COMPLETED) {

                    }
                })
                return@OnEditorActionListener true
            }
            false
        })

        mViewBinding.includePlayAll.llPlaylist.setOnClickListener {
            AudioPlayer.get().addAndPlay(viewAdapter.currentList?.toList())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        statusBarView?.let {
            CoverLoadUtils.get().removeLoadListener(it)
            it.onPlayEventListener?.let { listener ->
                AudioPlayer.get().removeOnPlayEventListener(listener)
            }
        }
    }
}