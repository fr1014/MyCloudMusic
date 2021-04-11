package com.fr1014.mycoludmusic.ui.home.comment

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentCommentBinding
import com.fr1014.mycoludmusic.ui.home.comment.paging3.CommentAdapter
import com.fr1014.mycoludmusic.ui.home.comment.paging3.CommentViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mymvvm.base.BaseFragment
import kotlinx.coroutines.launch

/**
 * Create by fanrui on 2021/4/11
 * Describe:
 */
const val ID = "id"
const val TYPE = "type"
const val PAGE_SIZE = "page_size"

class CommentFragment : BaseFragment<FragmentCommentBinding, CommentViewModel>() {
    private val commentAdapter = CommentAdapter()
    var id: Long = 0L
    var type: Int = 0
    var pageSize: Int = 0

    companion object {
        fun getInstance(type: Int,id:Long,pageSize:Int): CommentFragment {
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putLong(ID, id)
            bundle.putInt(PAGE_SIZE,pageSize)
            val commentFragment = CommentFragment()
            commentFragment.arguments = bundle
            return commentFragment
        }
    }

    override fun initParam() {
        arguments?.let {
            id = it.getLong(ID)
            type = it.getInt(TYPE)
            pageSize = it.getInt(PAGE_SIZE)
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentCommentBinding {
        return FragmentCommentBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): CommentViewModel {
        return ViewModelProvider(this, AppViewModelFactory.getInstance(MyApplication.getInstance())).get(CommentViewModel::class.java)
    }

    override fun initView() {
        if (id == 0L) {
            CommonUtils.toastShort("发生未知错误，请退出重试")
            return
        }
        mViewBinding.apply {
            rvComment.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = commentAdapter
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.commentList(type, id, pageSize).subscribe {
                commentAdapter.submitData(lifecycle, it)
            }
        }
    }

    fun onBackPressed() {
        activity?.apply {
            if (activity is MainActivity) {
                supportFragmentManager.beginTransaction()
                        .hide(this@CommentFragment)
                        .commitAllowingStateLoss()
            }
        }
    }
}