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
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class DayRecommendAdapter(layoutResId: Int) : BaseAdapter<SongsBean, BaseViewHolder>(layoutResId) ,BaseAdapter.OnItemClickListener{

    init {
        onItemClickListener = this
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, data: SongsBean) {
        GlideApp.with(holder.itemView)
                .load(data.al.picUrl + "?param=200y200")
                .apply(RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().transform(RoundedCorners(30)))
                .into(holder.getView(R.id.iv_cover))
        holder.getView<TextView>(R.id.tv_title).text = data.name
        holder.getView<TextView>(R.id.tv_info).text = "${data.getArInfo()} - ${data.al.name}"
        holder.addOnClickListener(R.id.cl_item_recommend)
        if (holder.adapterPosition == (itemCount - 1)){
            holder.getView<View>(R.id.view_place_holder).visibility = View.VISIBLE
        }
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.cl_item_recommend ->{
                val data = getData(position)
                AudioPlayer.get().addAndPlay(Music(data.id,data.getArInfo(),data.name,"","",""))
            }
        }
    }
}

fun SongsBean.getArInfo() : String{
    val sb = StringBuilder()
    for (i in ar.indices) {
        ar[i].apply {
            sb.append(name).append('/')
        }
    }
    return sb.substring(0, sb.length - 1)
}