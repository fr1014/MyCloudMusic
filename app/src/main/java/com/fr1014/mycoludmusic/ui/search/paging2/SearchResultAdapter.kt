package com.fr1014.mycoludmusic.ui.search.paging2

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.search.SearchViewModel
import com.fr1014.mycoludmusic.utils.ScreenUtils

/**
 * Create by fanrui on 2021/1/10
 * Describe:
 */
class SearchResultAdapter(private val mViewModel: SearchViewModel) : PagedListAdapter<Music, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkStatus: NetworkStatus? = null
    private var hasFooter = false

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return (oldItem.title + oldItem.artist) == (newItem.title + newItem.artist)
            }
        }
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
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.loading_view else R.layout.item_playlist_detail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_playlist_detail -> PlayListViewHolder.newInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    AudioPlayer.get().addAndPlay(getItem(holder.adapterPosition) as Music)
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
                    networkStatus
            )
            else -> {
                val music = getItem(position) ?: return
                (holder as PlayListViewHolder).bindWithPhotoItem(music)
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
            getView<ConstraintLayout>(R.id.cl_status).visibility = View.GONE
            setText(R.id.tv_song_name, music.title)
            //歌手 - 专辑
            if (TextUtils.isEmpty(music.album)) {
                setText(R.id.tv_author, music.artist)
            } else {
                setText(R.id.tv_author, music.artist + " - " + music.album)
            }

            //是否为原唱
            if (TextUtils.equals(music.original, "1")) {
                getView<View>(R.id.tv_prompt).visibility = View.VISIBLE
                setText(R.id.tv_prompt, itemView.context.getString(R.string.original))
                setAuthorLeftMargin(4)
            } else {
                getView<View>(R.id.tv_prompt).visibility = View.GONE
                setAuthorLeftMargin(0)
            }

            //别名/介绍
            if (TextUtils.isEmpty(music.subTitle)) {
                getView<View>(R.id.tv_subtitle).visibility = View.GONE
            } else {
                getView<View>(R.id.tv_subtitle).visibility = View.VISIBLE
                setText(R.id.tv_subtitle, music.subTitle)
            }
        }

        private fun setAuthorLeftMargin(dp: Int) {
            val layoutParams = getView<View>(R.id.tv_author).layoutParams as LinearLayout.LayoutParams
            layoutParams.leftMargin = ScreenUtils.dp2px(dp.toFloat())
            getView<View>(R.id.tv_author).layoutParams = layoutParams
        }

    }

    class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {
        companion object {
            fun newInstance(parent: ViewGroup): FooterViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_view, parent, false)
                return FooterViewHolder(view)
            }
        }

        fun bindWithNetworkStatus(networkStatus: NetworkStatus?) {
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
        }
    }
}