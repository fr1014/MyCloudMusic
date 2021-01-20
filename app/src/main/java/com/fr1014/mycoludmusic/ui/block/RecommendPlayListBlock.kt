package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.dataconvter.CommonPlaylist
import com.fr1014.mycoludmusic.databinding.BlockRecommendPlaylistBinding
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListDetailFragment

import io.supercharge.shimmerlayout.ShimmerLayout

class RecommendPlayListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding: BlockRecommendPlaylistBinding
    private lateinit var viewAdapter: RecommendPlayListAdapter

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockRecommendPlaylistBinding.inflate(LayoutInflater.from(context), this, false)
        addView(mViewBinding.root)
        viewAdapter = RecommendPlayListAdapter(R.layout.item_recommend_playlist)
        mViewBinding.rvRecommend.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = viewAdapter
        }
    }

    fun bindData(commonPlaylist: List<CommonPlaylist>) {
        viewAdapter.apply {
            setData(commonPlaylist)
        }
    }

    fun setTitle(title: String) {
        mViewBinding.tvTitle.text = title
    }
}

class RecommendPlayListAdapter(layoutResId: Int) : BaseAdapter<CommonPlaylist, BaseViewHolder>(layoutResId), BaseAdapter.OnItemClickListener {

    init {
        mOnItemClickListener = this
    }

    override fun convert(holder: BaseViewHolder, data: CommonPlaylist) {
        holder.getView<TextView>(R.id.tv_description).text = data.name
        holder.getView<ShimmerLayout>(R.id.shimmer).apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        val options = RequestOptions().centerCrop().transform(RoundedCorners(30))
        Glide.with(holder.itemView)
                .load(data.coverImgUrl)
                .apply(options)
                .placeholder(R.drawable.ic_placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false.also { holder.getView<ShimmerLayout>(R.id.shimmer)?.stopShimmerAnimation() }
                    }

                })
                .into(holder.getView(R.id.iv_cover))
        holder.addOnClickListener(R.id.item)
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.item -> {
                val data = getData(position)
                val bundle = PlayListDetailFragment.createBundle(data.id, data.name, data.coverImgUrl)
                Navigation.findNavController(view).navigate(R.id.playListDetailFragment, bundle)
            }
            else -> {
            }
        }
    }

}