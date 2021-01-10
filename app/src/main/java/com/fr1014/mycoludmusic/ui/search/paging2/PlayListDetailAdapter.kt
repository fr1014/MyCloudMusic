package com.fr1014.mycoludmusic.ui.search.paging2

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.SourceHolder
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.search.SearchViewModel
import com.fr1014.mycoludmusic.utils.ScreenUtil

/**
 * Create by fanrui on 2021/1/10
 * Describe:
 */
class PlayListDetailAdapter(private val mViewModel: SearchViewModel) : PagedListAdapter<Music, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    //是否显示底部的View
    private var isDisplayMarginView = false

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return (oldItem.title + oldItem.artist) == (newItem.title + newItem.artist)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            R.layout.item_playlist_detail -> PlayListViewHolder.newInstance(parent).also { holder ->
//                holder.itemView.setOnClickListener {
//
//                }
//            }
//
//            else -> FooterViewHolder.newInstance(parent).also {
//                it.itemView.setOnClickListener {
//                    galleryViewModel.retry()
//                }
//        }
        return PlayListViewHolder.newInstance(parent).also { holder ->
            holder.itemView.setOnClickListener {
                when (SourceHolder.get().source) {
                    "酷我" -> mViewModel.getKWSongUrl(getItem(holder.adapterPosition) as Music)
                    "网易" -> mViewModel.getWYYSongUrl(getItem(holder.adapterPosition) as Music)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
//            R.layout.gallery_footer -> (holder as FooterViewHolder).bindWithNetworkStatus(
//                    networkStatus
//            )
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

//            //是否显示最底部的MarginView
//            if (isDisplayMarginView) {
//                holder.getView<View>(R.id.margin_barsize).setVisibility(if (isShowDivider(holder)) View.VISIBLE else View.GONE)
//            }
        }

        private fun setAuthorLeftMargin(dp: Int) {
            val layoutParams = getView<View>(R.id.tv_author).layoutParams as LinearLayout.LayoutParams
            layoutParams.leftMargin = ScreenUtil.dp2px(itemView.context, dp.toFloat())
            getView<View>(R.id.tv_author).layoutParams = layoutParams
        }
    }

    fun setDisplayMarginView(displayMarginView: Boolean) {
        isDisplayMarginView = displayMarginView
    }
}