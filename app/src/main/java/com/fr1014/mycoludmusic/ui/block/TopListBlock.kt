package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.databinding.BlockTopListBinding

class TopListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding: BlockTopListBinding;

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockTopListBinding.inflate(LayoutInflater.from(context), this, false)
        addView(mViewBinding.root)
    }

    fun setData(playListDetailEntity: PlayListDetailEntity?) {
        mViewBinding.apply {
            playListDetailEntity?.playlist?.let {
                tvTopName.text = "${it.name} >"

                loadImg(it.tracks[0].al.picUrl, ivCover1)
                tvSongInfo1.text = it.tracks[0].name

                loadImg(it.tracks[1].al.picUrl, ivCover2)
                tvSongInfo2.text = it.tracks[1].name

                loadImg(it.tracks[2].al.picUrl, ivCover3)
                tvSongInfo3.text = it.tracks[2].name
            }
        }
    }

    private fun loadImg(url: String, view: ImageView) {
        val options = RequestOptions().centerCrop().transform(RoundedCorners(20))
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .apply(options)
                .into(view)
    }
}