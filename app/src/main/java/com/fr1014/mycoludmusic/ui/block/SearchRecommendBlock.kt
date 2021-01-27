package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotBean
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail
import com.fr1014.mycoludmusic.databinding.BlockSearchRecommendBinding
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class SearchRecommendBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var mViewBinding: BlockSearchRecommendBinding? = null
    private var mAdapter: SearchHotDetailAdapter? = null

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockSearchRecommendBinding.inflate(LayoutInflater.from(context), this, false)
        addView(mViewBinding?.root)
        mAdapter = SearchHotDetailAdapter(R.layout.item_search_hot)
        mViewBinding?.rvSearchHot?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    fun loadingViewVisible(visibility: Int) {
        mViewBinding?.loadingView?.llLoading?.visibility = visibility
    }

    fun setData(searchHotDetail: SearchHotDetail) {
        mAdapter?.setData(searchHotDetail.data)
    }
}

class SearchHotDetailAdapter(layoutResId: Int) : BaseAdapter<SearchHotBean, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, data: SearchHotBean) {
        holder.apply {
            setText(R.id.tv_seq, (holder.adapterPosition + 1).toString())
            setText(R.id.tv_searchWord,data.searchWord)
            GlideApp.with(holder.itemView)
                    .load(data.iconUrl)
                    .fitCenter()
                    .into(holder.getView(R.id.iv_icon))
        }
    }

}