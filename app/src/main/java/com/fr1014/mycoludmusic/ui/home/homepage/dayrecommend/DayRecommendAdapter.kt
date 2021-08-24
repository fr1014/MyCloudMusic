package com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.musicmanager.player.Music
import com.fr1014.mycoludmusic.musicmanager.player.MusicSource
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class DayRecommendAdapter(layoutResId: Int) : BaseAdapter<SongsBean, BaseViewHolder>(layoutResId), BaseAdapter.OnItemClickListener {
    private var showItemCover = true

    constructor(layoutResId: Int, showItemCover: Boolean) : this(layoutResId) {
        this.showItemCover = showItemCover
    }

    init {
        onItemClickListener = this
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, data: SongsBean) {
        if (showItemCover) {
            GlideApp.with(holder.itemView)
                    .load(data.al.picUrl + "?param=200y200")
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().transform(RoundedCorners(30)))
                    .into(holder.getView(R.id.iv_cover))
        } else {
            holder.getView<TextView>(R.id.tv_order).apply {
                text = holder.adapterPosition.toString()
                visibility = View.VISIBLE
            }
        }
        holder.apply {
            getView<TextView>(R.id.tv_song_name).text = data.name
            getView<TextView>(R.id.tv_author).text = "${data.getArInfo()} - ${data.al.name}"
            addOnClickListener(R.id.item_view)
            getView<View>(R.id.view_place_holder).visibility = if (holder.layoutPosition == (itemCount - 1)) View.VISIBLE else View.GONE
        }
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.item_view -> {
                val data = getData(position)
                MyAudioPlay.get().addPlayMusic(Music(data.id.toString(), data.getArInfo(), data.name, data.mv.toString(), MusicSource.WY_MUSIC.sourceType))
            }
        }
    }
}

fun SongsBean.getArInfo(): String {
    val sb = StringBuilder()
    for (i in ar.indices) {
        ar[i].apply {
            sb.append(name).append('/')
        }
    }
    return sb.substring(0, sb.length - 1)
}