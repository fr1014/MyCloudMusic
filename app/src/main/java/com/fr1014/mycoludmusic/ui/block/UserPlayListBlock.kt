package com.fr1014.mycoludmusic.ui.block

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist
import com.fr1014.mycoludmusic.databinding.BlockUserPlaylistBinding
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListDetailFragment
import io.supercharge.shimmerlayout.ShimmerLayout

class UserPlayListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding : BlockUserPlaylistBinding
    private lateinit var mAdapter:UserPlayListAdapter

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockUserPlaylistBinding.inflate(LayoutInflater.from(context),this,false)
        addView(mViewBinding.root)
        mAdapter = UserPlayListAdapter(R.layout.item_user_playlist)
        mViewBinding.rvPlaylist.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setTitle(name:String){
        mViewBinding.tvName.text = name
    }

    fun setData(playlists : List<Playlist>){
        mAdapter.setData(playlists)
    }
}

class UserPlayListAdapter(layoutResId:Int) : BaseAdapter<Playlist,BaseViewHolder>(layoutResId),BaseAdapter.OnItemClickListener{

    init {
        onItemClickListener = this
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, data: Playlist) {
        holder.getView<TextView>(R.id.tv_title).text = data.name
        holder.getView<TextView>(R.id.tv_count).text = "${data.trackCount}首,by ${data.creator.nickname}"
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
                .into(holder.getView(R.id.iv_cover_like))
        holder.addOnClickListener(R.id.cl_playlist)
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>?, view: View?, position: Int) {
        when(view?.id){
            R.id.cl_playlist ->{
                val data = getData(position)
                val bundle = PlayListDetailFragment.createBundle(data.id, data.name, data.coverImgUrl)
                Navigation.findNavController(view).navigate(R.id.playListDetailFragment, bundle)
            }
        }
    }

}