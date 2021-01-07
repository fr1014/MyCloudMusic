package com.fr1014.mycoludmusic

import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.base.BasePlayActivity
import com.fr1014.mycoludmusic.customview.PlayStatusBarView
import com.fr1014.mycoludmusic.databinding.ActivitySearchBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.home.toplist.PlayListDetailAdapter
import com.fr1014.mycoludmusic.ui.home.toplist.TopListViewModel
import com.fr1014.mycoludmusic.utils.CoverLoadUtils
import com.fr1014.mycoludmusic.utils.ScreenUtil

class SearchActivity : BasePlayActivity<ActivitySearchBinding?, TopListViewModel?>() {
    private lateinit var viewAdapter: PlayListDetailAdapter
    private var statusBarView: PlayStatusBarView? = null
    private var source: String? = null

    /**
     * 沉浸式状态栏
     */
    private fun initSystemBar() {
        val top = ScreenUtil.getStatusHeight(MyApplication.getInstance())
        mViewBinding!!.llSearch.setPadding(0, top, 0, 0)
    }

    override fun getViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater).also { mViewBinding = it }
    }

    override fun initViewModel(): TopListViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(TopListViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        initSystemBar()
        initListener()
    }

    override fun onServiceBound() {
        statusBarView = PlayStatusBarView(this, supportFragmentManager)
        AudioPlayer.get().addOnPlayEventListener(statusBarView)
        CoverLoadUtils.get().registerLoadListener(statusBarView)
        mViewBinding!!.flPlaystatus.addView(statusBarView)
    }

    override fun initData() {
        source = SourceHolder.get().source
    }

    override fun initViewObservable() {
        mViewModel!!.search.observe(this, { music -> viewAdapter.setData(music) })
        mViewModel!!.songUrl.observe(this, { music ->
            if (!TextUtils.isEmpty(music.songUrl)) {
                AudioPlayer.get().addAndPlay(music)
            } else {
                AudioPlayer.get().playNext()
            }
        })
    }

    private fun initAdapter() {
        viewAdapter = PlayListDetailAdapter(false)
        viewAdapter.setDisplayMarginView(true)
        mViewBinding!!.rvSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

    }

    private fun initListener() {
        mViewBinding!!.ivBack.setOnClickListener { finish() }
        mViewBinding!!.etKeywords.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 当按了搜索之后关闭软键盘
                (mViewBinding!!.etKeywords.context.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        this@SearchActivity.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
                //viewModel.getSearchEntityWYY(binding.etKeywords.getText().toString(), 0);
                //viewModel.getSearchEntityKW(mViewBinding.etKeywords.getText().toString(), 30);
                val searchKey = mViewBinding!!.etKeywords.text.toString()
                when (source) {
                    "酷我" -> mViewModel!!.getSearchEntityKW(searchKey, 0, 30)
                    "网易" -> mViewModel!!.getSearchEntityWYY(searchKey, 0)
                }
                return@OnEditorActionListener true
            }
            false
        })
        viewAdapter!!.onItemClickListener = BaseAdapter.OnItemClickListener { adapter, view, position ->
            when (source) {
                "酷我" -> mViewModel!!.getKWSongUrl(adapter.getData(position) as Music)
                "网易" -> mViewModel!!.getWYYSongUrl(adapter.getData(position) as Music)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (statusBarView != null) {
            AudioPlayer.get().removeOnPlayEventListener(statusBarView)
            CoverLoadUtils.get().removeLoadListener(statusBarView)
        }
    }
}