package com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend

import android.annotation.SuppressLint
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class DayRecommendAdapter(layoutResId: Int) : BaseAdapter<SongsBean, BaseViewHolder>(layoutResId) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, data: SongsBean) {
        GlideApp.with(holder.itemView)
                .load(data.al.picUrl + "?param=200y200")
                .apply(RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().transform(RoundedCorners(30)))
                .into(holder.getView(R.id.iv_cover))
        holder.getView<TextView>(R.id.tv_title).text = data.name
        val sb = StringBuilder()
        for (i in data.ar.indices) {
            data.ar[i].apply {
                sb.append(name).append('/')
            }
        }
        holder.getView<TextView>(R.id.tv_info).text = "${sb.substring(0, sb.length - 1)} - ${data.al.name}"
    }
}