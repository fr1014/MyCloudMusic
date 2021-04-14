package com.fr1014.mycoludmusic.ui.home.comment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.ActivityCommentBinding
import com.fr1014.mycoludmusic.ui.home.comment.paging3.CommentAdapter
import com.fr1014.mycoludmusic.ui.paging.FooterAdapter
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils
import com.fr1014.mycoludmusic.utils.StatusBarUtils
import com.fr1014.mymvvm.base.BaseActivity
import kotlinx.coroutines.launch

/**
 * Create by fanrui on 2021/4/11
 * Describe:
 */
const val COMMENT_BUNDLE = "comment_bundle"
const val ID = "id"
const val TYPE = "type"
const val PAGE_SIZE = "page_size"
const val COMMENT_COUNT = "comment_count"

class CommentActivity : BaseActivity<ActivityCommentBinding, CommonViewModel>(), View.OnClickListener {
    private val commentAdapter = CommentAdapter()
    var id: Long = 0L
    var type: Int = 0
    var pageSize: Int = 0
    var commentCount = 0L

    companion object {
        fun getInstance(context: Context, type: Int, id: Long, pageSize: Int, commentCount: Long) {
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putLong(ID, id)
            bundle.putInt(PAGE_SIZE, pageSize)
            bundle.putLong(COMMENT_COUNT, commentCount)
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(COMMENT_BUNDLE, bundle)
            context.startActivity(intent)
        }
    }

    override fun initParam() {
        intent?.getBundleExtra(COMMENT_BUNDLE)?.let {
            id = it.getLong(ID)
            type = it.getInt(TYPE)
            pageSize = it.getInt(PAGE_SIZE)
            commentCount = it.getLong(COMMENT_COUNT)
        }
    }

    override fun getViewBinding() = ActivityCommentBinding.inflate(layoutInflater)

    override fun initViewModel(): CommonViewModel {
        return ViewModelProvider(this, AppViewModelFactory.getInstance(MyApplication.getInstance())).get(CommonViewModel::class.java)
    }

    override fun initView() {
        StatusBarUtils.setImmersiveStatusBar(window, true)

        if (id == 0L) {
            CommonUtils.toastShort("发生未知错误，请退出重试")
            return
        }
        mViewBinding.apply {
            val lp = root.layoutParams as FrameLayout.LayoutParams
            lp.topMargin = ScreenUtils.getStatusBarHeight()
            root.layoutParams = lp
            countComment.text = if (commentCount != 0L) "评论($commentCount)" else "评论"
            rvComment.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = commentAdapter.withLoadStateFooter(FooterAdapter { commentAdapter.retry() })
            }

            back.setOnClickListener(this@CommentActivity)
            share.setOnClickListener(this@CommentActivity)
        }
    }

    override fun initData() {
        lifecycleScope.launch {
            mViewModel.commentList(type, id, pageSize).subscribe {
                commentAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun initViewObservable() {
        if (commentCount == 0L) {
            mViewModel.commentPagingSource?.commentInfo?.observe(this, Observer {
                mViewBinding.countComment.text = "评论(${it.data.totalCount})"
            })
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                finish()
            }
            else -> {
                CommonUtils.rd_ing()
            }
        }
    }
}