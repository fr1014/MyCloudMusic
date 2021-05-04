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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.block.PlayListHeaderBlock
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListViewModel
import com.fr1014.mycoludmusic.ui.home.playlist.dialog.PlayListInfoDialog
import com.fr1014.mycoludmusic.ui.mv.MVActivity
import com.fr1014.mycoludmusic.utils.PaletteBgUtils
import com.fr1014.mycoludmusic.ui.paging.AdapterDataObserverProxy
import com.fr1014.mycoludmusic.utils.CommonUtils
import de.hdodenhof.circleimageview.CircleImageView

class PlayListDetailAdapter(private val mViewModel: PlayListViewModel, private val mOwner: LifecycleOwner) : PagingDataAdapter<Music, RecyclerView.ViewHolder>(PlayListComparator) {
    private var onPlayAllClickListener: OnPlayAllClickListener? = null
    private var playListDetailEntity: PlayListDetailEntity? = null
    private var showDialogInfo = true
    private var playListInfoDialog: PlayListInfoDialog? = null

    private var headerViewHolder: HeaderViewHolder? = null
    private lateinit var parentFragmentManager: FragmentManager

    constructor(mViewModel: PlayListViewModel, mOwner: LifecycleOwner, showDialogInfo: Boolean, parentFragmentManager: FragmentManager) : this(mViewModel, mOwner) {
        this.showDialogInfo = showDialogInfo
        this.parentFragmentManager = parentFragmentManager
    }

    object PlayListComparator : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.equals(newItem)
        }

    }

    fun setPlayListCount(count: Int) {
        headerViewHolder?.setHeadCount(count)
    }

    fun setHeadInfo(playListDetailEntity: PlayListDetailEntity) {
        this.playListDetailEntity = playListDetailEntity
        headerViewHolder?.apply {
            setHeadData(playListDetailEntity)
            itemView.findViewById<PlayListHeaderBlock>(R.id.block_playlist_header).setData(playListDetailEntity, mViewModel, parentFragmentManager)
        }
    }

    fun setHeadInfo(type: Int) {
        headerViewHolder?.setHeadData(type)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
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
            if (playListInfoDialog == null) {
                playListInfoDialog = PlayListInfoDialog(context)
            }
            playListInfoDialog!!.apply {
                playListDetailEntity?.let { setData(it) }
                show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.head_playlist_detail -> {
                HeaderViewHolder.newInstance(mViewModel, mOwner, parent).also { holder ->
                    headerViewHolder = holder
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
            }
            else -> {
                PlayListViewHolder.newInstance(parent).also { holder ->
                    holder.itemView.apply {

                        setOnClickListener {
                            AudioPlayer.get().addAndPlay(getItem(holder.adapterPosition - 1) as Music)
                        }

                        findViewById<LinearLayout>(R.id.ll_mv).setOnClickListener {
                            if (!CommonUtils.isFastClick()) {
                                val music = getItem(holder.adapterPosition - 1) as Music
                                if (music.mvId != 0L) {
                                    mViewModel.getWYMVInfo(music)
                                }
                            }
                        }

                        findViewById<LinearLayout>(R.id.ll_more).setOnClickListener {
                            CommonUtils.rd_ing()
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //paging3 的坑
        // rvPlaylistDetail.adapter = pAdapter.withLoadStateFooter(MyLoadStateAdapter { pAdapter.retry() })
        //withLoadStateFooter 重新生成的adapter将  getItemViewType(position: Int) 覆写了 所以 此处不能根据viewType去绑定数据 只可依据position
        when (position > 0) {
            true -> {
                val music = getItem(position - 1) ?: return
                (holder as PlayListViewHolder).bindWithPhotoItem(music)
            }
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, 1))
    }

    //java.lang.IllegalStateException: Observer androidx.paging.PagingDataAdapter was not registered.
    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {

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
        setText(R.id.tv_order, layoutPosition.toString())
        //歌手 - 专辑
        if (TextUtils.isEmpty(music.album)) {
            setText(R.id.tv_author, music.artist)
        } else {
            setText(R.id.tv_author, music.artist + " - " + music.album)
        }
        if (music.mvId != 0L) {
            getView<LinearLayout>(R.id.ll_mv).visibility = View.VISIBLE
        }
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

    fun setHeadData(type: Int) {
        itemView.findViewById<PlayListHeaderBlock>(R.id.block_playlist_header).setData(type)
    }

    companion object {
        fun newInstance(mViewModel: PlayListViewModel, mOwner: LifecycleOwner, parent: ViewGroup): HeaderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.head_playlist_detail, parent, false)
            return HeaderViewHolder(mViewModel, mOwner, view)
        }
    }

}