package com.fr1014.mycoludmusic.ui.search

import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.R
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
import java.lang.reflect.Field

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
        initSearchView()
        initAdapter()
        initSystemBar()
        //避免自动弹出输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mViewBinding.includePlayAll.llPlaylist.visibility = View.GONE
        initListener()
    }

    private fun initSearchView() {

        mViewBinding.serachView.apply {
            //是否一直显示clearIcon
            setIconifiedByDefault(false)
            //设置搜索框展开时是否显示提交按钮，false不显示
            isSubmitButtonEnabled = false
            //让键盘的回车键设置成搜索
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            //搜索框是否展开，false表示展开
            isIconified = false
            //获取焦点
            isFocusable = true
            requestFocusFromTouch()
            //设置提示词
            queryHint = "请输入关键字"

            //设置输入框文字颜色
            //设置输入框文字颜色
            //设置输入框文字size
            val editText = findViewById<EditText>(R.id.search_src_text)
            editText.apply {
                setHintTextColor(ContextCompat.getColor(context, R.color.app_gray))
                setTextColor(ContextCompat.getColor(context, R.color.tv_bt_black))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F) //14sp
            }
        }
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

        // 设置搜索文本监听
        mViewBinding.apply {
            serachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //当点击搜索按钮时触发该方法
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        mViewBinding.serachView.clearFocus()
                        includePlayAll.llPlaylist.visibility = View.VISIBLE
                        mViewModel.search(it).observe(getLifecycleOwner(), Observer { pagedList ->
                            //paging2
                            viewAdapter.submitList(pagedList)
                        })

                        mViewModel.networkStatus.observe(getLifecycleOwner(), Observer { netStatus ->
                            viewAdapter.updateNetworkStatus(netStatus)
                            if (netStatus == NetworkStatus.COMPLETED) {

                            }
                        })
                    }
                    return false
                }

                //当搜索内容改变时触发该方法
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }

        mViewBinding.includePlayAll.llPlaylist.setOnClickListener {
            AudioPlayer.get().addAndPlay(viewAdapter.currentList?.toList())
        }

    }

    override fun onPause() {
        super.onPause()
        mViewBinding.serachView.clearFocus()
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

    private fun getLifecycleOwner() = this
}