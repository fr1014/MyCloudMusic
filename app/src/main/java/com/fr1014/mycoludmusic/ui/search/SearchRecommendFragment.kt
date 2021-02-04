package com.fr1014.mycoludmusic.ui.search

import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSearchRecommendBinding
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mymvvm.base.BaseFragment

class SearchRecommendFragment : BaseFragment<FragmentSearchRecommendBinding, SearchViewModel>() {

    private var searchHistoryAdapter: SearchHistoryAdapter? = null

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
        searchHistoryAdapter = SearchHistoryAdapter(mViewModel, R.layout.item_history)
        mViewBinding.rvSearchHistory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = searchHistoryAdapter
        }
        initListener()
    }

    private fun initListener() {
        mViewBinding.flDev.setOnClickListener {
            context?.let {
                AlertDialog.Builder(it)
                        .setMessage("确定清空全部历史记录？")
                        .setPositiveButton("清空") { dialog, which ->
                            mViewModel.clearHistory()
                            dialog.dismiss()
                        }
                        .setNegativeButton("取消") { dialog, which ->

                        }
                        .create()
                        .show()
            }
        }
    }

    override fun initData() {
        mViewModel.searchHotDetail()
        mViewModel.getHistory()
        mViewBinding.blockSearchRecommend.loading(true)
    }

    override fun initViewObservable() {
        mViewModel.getSearchHotDetail().observe(viewLifecycleOwner, Observer {
            mViewBinding.blockSearchRecommend.apply {
                loading(false)
                setData(it)
            }
        })

        mViewModel.getSearchHistories().observe(this, Observer {
            if (!CollectionUtils.isEmptyList(it)) {
                showHistory(true)
                searchHistoryAdapter?.setData(it)
            } else {
                showHistory(false)
            }
        })
    }

    private fun showHistory(isShow: Boolean) {
        mViewBinding.apply {
            tvHistoryTitle.visibility = if (isShow) View.VISIBLE else View.GONE
            rvSearchHistory.visibility = if (isShow) View.VISIBLE else View.GONE
            flDev.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }
}

class SearchHistoryAdapter(private val mViewModel: SearchViewModel, layoutResId: Int) : BaseAdapter<String, BaseViewHolder>(layoutResId), BaseAdapter.OnItemClickListener {

    init {
        onItemClickListener = this
    }

    override fun convert(holder: BaseViewHolder, data: String) {
        holder.setText(R.id.tv_item_history, data)
        holder.addOnClickListener(R.id.tv_item_history)
        if (holder.adapterPosition == itemCount - 1) {
            holder.getView<View>(R.id.place_view).visibility = View.VISIBLE
        }
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.tv_item_history -> {
                val data = getData(position)
                mViewModel.getSearchKey().postValue(data)
                mViewModel.saveHistory(data)
                Navigation.findNavController(view).navigate(R.id.search_result, SearchResultFragment.createBundle(data))
            }
        }
    }

}