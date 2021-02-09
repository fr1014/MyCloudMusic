package com.fr1014.mycoludmusic.ui.home.playlist.paging2

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListViewModel
import com.fr1014.mycoludmusic.ui.home.playlist.dialog.PlayListInfoDialog
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus
import com.fr1014.mycoludmusic.utils.PaletteBgUtils
import de.hdodenhof.circleimageview.CircleImageView

class PlayListDetailAdapter(private val mViewModel: PlayListViewModel, private val mOwner: LifecycleOwner) : PagedListAdapter<Music, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkStatus: NetworkStatus? = null
    private var hasFooter = false
    private var onPlayAllClickListener: OnPlayAllClickListener? = null
    private var playListCount: Int? = null
    private var playListDetailEntity: PlayListDetailEntity? = null
    private var showDialogInfo = true

    constructor(mViewModel: PlayListViewModel, mOwner: LifecycleOwner, showDialogInfo: Boolean) : this(mViewModel, mOwner) {
        this.showDialogInfo = showDialogInfo
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    fun setPlayListCount(count: Int) {
        playListCount = count
    }

    fun setHeadInfo(playListDetailEntity: PlayListDetailEntity) {
        this.playListDetailEntity = playListDetailEntity
    }

    fun updateNetworkStatus(networkStatus: NetworkStatus?) {
        this.networkStatus = networkStatus
        if (networkStatus == NetworkStatus.INITIAL_LOADING) hideFooter() else showFooter()
    }

    private fun hideFooter() {
        if (hasFooter) {
            notifyItemRemoved(itemCount - 1)
        }
        hasFooter = false
    }

    private fun showFooter() {
        if (hasFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasFooter = true
            notifyItemInserted(itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 2 else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) {
            R.layout.loading_view
        } else if (position == 0) {
            R.layout.head_playlist_detail
        } else {
            R.layout.item_playlist_detail
        }
    }

    interface OnPlayAllClickListener {
        fun clickPlayAll()
    }

    fun setOnPlayAllClick(onPlayAllClickListener: OnPlayAllClickListener) {
        this.onPlayAllClickListener = onPlayAllClickListener
    }

    private fun showInfoDialog(context: Context) {
        if (showDialogInfo) {
            val playListInfoDialog = PlayListInfoDialog(context)
            playListDetailEntity?.let { playListInfoDialog.setData(it) }
            playListInfoDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_playlist_detail -> PlayListViewHolder.newInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    AudioPlayer.get().addAndPlay(getItem(holder.adapterPosition - 1) as Music)
                }
            }
            R.layout.head_playlist_detail -> HeaderViewHolder.newInstance(mViewModel, mOwner, parent).also { holder ->
                holder.itemView.apply {
                    findViewById<LinearLayout>(R.id.play_all).setOnClickListener {
                        onPlayAllClickListener?.clickPlayAll()
                    }
                    findViewById<FrameLayout>(R.id.fl_cover).setOnClickListener {
                        showInfoDialog(context)
                    }
                    findViewById<TextView>(R.id.tv_description).setOnClickListener {
                        showInfoDialog(context)
                    }
                }
            }
            else -> FooterViewHolder.newInstance(parent).also {
                it.itemView.setOnClickListener {
                    mViewModel.retry()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.loading_view -> (holder as FooterViewHolder).bindWithNetworkStatus(
                    networkStatus, itemCount
            )
            R.layout.head_playlist_detail -> {
                playListDetailEntity?.let { (holder as HeaderViewHolder).setHeadData(it) }
                playListCount?.let { (holder as HeaderViewHolder).setHeadCount(it) }
            }
            else -> {
                val music = getItem(position - 1) ?: return
                (holder as PlayListViewHolder).bindWithPhotoItem(music)
            }
        }
    }
}

class PlayListViewHolder(itemView: View) : BaseViewHolder(itemView) {

    companion object {
        fun newInstance(parent: ViewGroup): PlayListViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_detail, parent, false)
            return PlayListViewHolder(view)
        }
    }

    fun bindWithPhotoItem(music: Music) {
        getView<ConstraintLayout>(R.id.cl_status).visibility = View.VISIBLE
        setText(R.id.tv_song_name, music.title)
        setText(R.id.tv_rank, layoutPosition.toString())
        //歌手 - 专辑
        if (TextUtils.isEmpty(music.album)) {
            setText(R.id.tv_author, music.artist)
        } else {
            setText(R.id.tv_author, music.artist + " - " + music.album)
        }
    }

}

class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_view, parent, false)
            return FooterViewHolder(view)
        }
    }

    fun bindWithNetworkStatus(networkStatus: NetworkStatus?, count: Int) {
        with(itemView) {
            when (networkStatus) {
                NetworkStatus.FAILED -> {
                    getView<TextView>(R.id.tv_loading).text = "点击重试"
                    getView<LottieAnimationView>(R.id.lav_loading).visibility = View.GONE
                    isClickable = true
                }
                NetworkStatus.COMPLETED -> {
                    getView<TextView>(R.id.tv_loading).text = "-- 我是有底线的 --"
                    getView<LottieAnimationView>(R.id.lav_loading).visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    getView<TextView>(R.id.tv_loading).text = "正在加载..."
                    getView<LottieAnimationView>(R.id.lav_loading).visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
        getView<View>(R.id.view_divider).visibility = if (isShowDivider(count)) View.VISIBLE else View.GONE
    }

    private fun isShowDivider(count: Int): Boolean {
        return layoutPosition == count - 1
    }
}

class HeaderViewHolder(mViewModel: PlayListViewModel, mOwner: LifecycleOwner, itemView: View) : BaseViewHolder(itemView) {

    init {
        mViewModel.getCoverBitmap().observe(mOwner, Observer {
            Glide.with(itemView)
                    .load(it)
                    .apply(RequestOptions().centerCrop().transform(RoundedCorners(32)))
                    .into(itemView.findViewById<ImageView>(R.id.iv_cover))
            PaletteBgUtils.paletteDownBg(itemView.findViewById(R.id.iv_bg), it)
        })
    }

    fun setHeadCount(count: Int) {
        itemView.findViewById<TextView>(R.id.tv_count).text = count.toString()
    }

    fun setHeadData(playListDetailEntity: PlayListDetailEntity) {
        playListDetailEntity.playlist?.apply {
            itemView.findViewById<TextView>(R.id.tv_name).text = name
            val creatorCover = itemView.findViewById<CircleImageView>(R.id.creator_cover)
            Glide.with(creatorCover)
                    .load(creator.avatarUrl + "?param=60y60")
                    .into(creatorCover)
            itemView.findViewById<TextView>(R.id.tv_creator_name).text = creator.nickname
            itemView.findViewById<TextView>(R.id.tv_description).apply {
                if (TextUtils.isEmpty(description)) {
                    visibility = View.GONE
                } else {
                    text = description
                }
            }
        }
    }

    companion object {
        fun newInstance(mViewModel: PlayListViewModel, mOwner: LifecycleOwner, parent: ViewGroup): HeaderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.head_playlist_detail, parent, false)
            return HeaderViewHolder(mViewModel, mOwner, view)
        }
    }

}