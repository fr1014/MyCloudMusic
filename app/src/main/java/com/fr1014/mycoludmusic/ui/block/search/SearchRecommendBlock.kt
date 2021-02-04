package com.fr1014.mycoludmusic.ui.block.search

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotBean
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail
import com.fr1014.mycoludmusic.databinding.BlockSearchRecommendBinding
import com.fr1014.mycoludmusic.ui.search.SearchResultFragment
import com.fr1014.mycoludmusic.ui.search.SearchViewModel
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
    }

    fun setViewModel(viewModel: SearchViewModel) {
        mAdapter = SearchHotDetailAdapter(viewModel, R.layout.item_search_hot)
        mViewBinding?.rvSearchHot?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    fun loading(isLoading: Boolean) {
        mViewBinding?.apply {
            if (!isLoading) {
                loadingView.llLoading.visibility = View.GONE
                tvHotTitle.visibility = View.VISIBLE
            } else {
                loadingView.llLoading.visibility = View.VISIBLE
                tvHotTitle.visibility = View.GONE
            }
        }
    }

    fun setData(searchHotDetail: SearchHotDetail) {
        mAdapter?.setData(searchHotDetail.data)
    }
}

class SearchHotDetailAdapter(private val mViewModel: SearchViewModel, layoutResId: Int) : BaseAdapter<SearchHotBean, BaseViewHolder>(layoutResId), BaseAdapter.OnItemClickListener {

    init {
        onItemClickListener = this
    }

    override fun convert(holder: BaseViewHolder, data: SearchHotBean) {
        holder.apply {
            val adapterPosition = holder.adapterPosition
            //android:textAppearance="@style/TextAppearance.AppCompat.Body1"
            //android:textStyle="bold"
            getView<TextView>(R.id.tv_seq).apply {
                text = adapterPosition.plus(1).toString()
                if (adapterPosition < 3) {
                    setTextColor(context.resources.getColor(R.color.red))
                    typeface = Typeface.DEFAULT_BOLD
                } else {
                    setTextColor(context.resources.getColor(R.color.gray))
                }
            }
            getView<TextView>(R.id.tv_searchWord).apply {
                text = data.searchWord
                if (adapterPosition < 3) {
                    typeface = Typeface.DEFAULT_BOLD
                }
            }

            GlideApp.with(holder.itemView)
                    .load(data.iconUrl)
                    .into(holder.getView(R.id.iv_icon))
        }

        holder.addOnClickListener(R.id.item_searchWord)
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.item_searchWord -> {
                val data = getData(position)
                mViewModel.getSearchKey().postValue(data.searchWord)
                mViewModel.saveHistory(data.searchWord)
                Navigation.findNavController(view).navigate(R.id.search_result, SearchResultFragment.createBundle(data.searchWord))
            }
        }
    }

}