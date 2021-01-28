package com.fr1014.mycoludmusic.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.SourceHolder
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.base.BasePlayActivity
import com.fr1014.mycoludmusic.customview.PlayStatusBarView
import com.fr1014.mycoludmusic.data.source.local.room.DBManager
import com.fr1014.mycoludmusic.databinding.ActivitySearchBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.CoverLoadUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils

const val SEARCH_WORD_KEY = "SEARCH_WORD_KEY"
const val SEARCH_SHOW_WORD_KEY = "SEARCH_SHOW_WORD_KEY"
const val SEARCH_TYPE_KEY = "SEARCH_TYPE_KEY"
const val SEARCH_BUNDLE = "SEARCH_BUNDLE"

class SearchActivity : BasePlayActivity<ActivitySearchBinding, SearchViewModel>() {
    private var statusBarView: PlayStatusBarView? = null
    private var source: String? = null
    private lateinit var navController: NavController
    private var searchWord: String? = null
    private var searchShowWord: String? = null
    private var searchType: Int? = null

    companion object {
        fun startSearchActivity(context: Context, searchShowWord: String, searchWord: String, searchType: Int) {
            val intent = Intent(context, SearchActivity::class.java)
            Bundle().apply {
                putString(SEARCH_WORD_KEY, searchWord)
                putString(SEARCH_SHOW_WORD_KEY, searchShowWord)
                putInt(SEARCH_TYPE_KEY, searchType)
                intent.putExtra(SEARCH_BUNDLE, this)
            }
            context.startActivity(intent)
        }
    }

    override fun initParam() {
        intent.getBundleExtra(SEARCH_BUNDLE)?.let {
            searchWord = it.getString(SEARCH_WORD_KEY)
            searchShowWord = it.getString(SEARCH_SHOW_WORD_KEY)
            searchType = it.getInt(SEARCH_TYPE_KEY)
        }
    }

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
        initSystemBar()
        initSearchView()
        navController = Navigation.findNavController(this, R.id.nav_search_host)
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
            queryHint = if (searchShowWord== null) "请输入关键字" else searchShowWord

            //设置输入框文字颜色
            //设置输入框文字颜色
            //设置输入框文字size
            val editText = findViewById<EditText>(R.id.search_src_text)
            editText.apply {
                setHintTextColor(ContextCompat.getColor(context, R.color.app_gray))
                setTextColor(ContextCompat.getColor(context, R.color.tv_bt_black))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F) //14sp
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (TextUtils.isEmpty(query)) {
                                if (searchWord != null) {
                                    mViewBinding.serachView.apply {
                                        clearFocus()
                                        navigation(searchWord!!)
                                    }
                                }
                            } else {
                                mViewBinding.serachView.apply {
                                    clearFocus()
                                    navigation(query.toString())
                                }
                            }
                            return true
                        }
                        return false
                    }
                })
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

    private fun initListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.search_result) {
                mViewBinding.serachView.clearFocus()
            } else if (destination.id == R.id.search_recommend) {
                mViewBinding.serachView.setQuery("", false)
            }
        }

        mViewBinding.ivBack.setOnClickListener { onBackPressed() }

        // 设置搜索文本监听
        mViewBinding.apply {
            serachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //当点击搜索按钮时触发该方法
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                //当搜索内容改变时触发该方法
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    override fun initViewObservable() {
        mViewModel.getSearchKey().observe(this, Observer {
            changeSearchViewText(it)
        })
    }

    private fun changeSearchViewText(text: String) {
        val editText = findViewById<EditText>(R.id.search_src_text)
        editText.setText(text)
    }

    private fun navigation(searchWord: String) {
        navController.navigate(R.id.search_result, SearchResultFragment.createBundle(searchWord))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (TextUtils.equals(navController.currentDestination?.label, "搜索结果")) {
            navController.popBackStack(R.id.search_recommend, true)
            mViewBinding.serachView.setQuery("", false)
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
}