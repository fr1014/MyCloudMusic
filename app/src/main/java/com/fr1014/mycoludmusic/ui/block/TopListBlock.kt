package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.databinding.BlockTopListBinding

class TopListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding : BlockTopListBinding;

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockTopListBinding.inflate(LayoutInflater.from(context),this,false)
        addView(mViewBinding.root)
    }

    fun setData(playListDetailEntity: PlayListDetailEntity?){
        val options = RequestOptions().centerCrop().transform(RoundedCorners(20))
        mViewBinding.apply {
            playListDetailEntity?.playlist?.let {
                tvTopName.text = "${it.name} >"
                Glide.with(context)
                        .load(it.tracks[0].al.picUrl)
                        .apply(options)
                        .into(ivCover1)
                tvSongInfo1.text = it.tracks[0].name

                Glide.with(context)
                        .load(it.tracks[1].al.picUrl)
                        .apply(options)
                        .into(ivCover2)
                tvSongInfo2.text = it.tracks[1].name

                Glide.with(context)
                        .load(it.tracks[2].al.picUrl)
                        .apply(options)
                        .into(ivCover3)
                tvSongInfo3.text = it.tracks[2].name
            }
        }
    }

}