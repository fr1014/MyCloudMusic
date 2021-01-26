package com.fr1014.mycoludmusic.ui.block

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Track
import com.fr1014.mycoludmusic.databinding.BlockTopListBinding
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListDetailFragment
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class TopListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding: BlockTopListBinding;
    private var playListDetailEntity: PlayListDetailEntity? = null

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockTopListBinding.inflate(LayoutInflater.from(context), this, false)
        addView(mViewBinding.root)
    }

    @SuppressLint("SetTextI18n")
    fun setData(playListDetailEntity: PlayListDetailEntity?) {
        mViewBinding.apply {
            playListDetailEntity?.playlist?.let {
                tvTopName.apply {
                    text = "${it.name} >"
                    setOnClickListener { view ->
                        Navigation.findNavController(view).navigate(R.id.playListDetailFragment,
                                PlayListDetailFragment.createBundle(it.id, it.name, it.coverImgUrl)
                        )
                    }

                    it.tracks[0].apply {
                        loadImg(al.picUrl, ivCover1)
                        tvSongInfo1.text = "1  $name"
                        tvSinger1.text = " - ${getSinger(this)}"
                    }

                    it.tracks[1].apply {
                        loadImg(al.picUrl, ivCover2)
                        tvSongInfo2.text = "2  $name"
                        tvSinger2.text = " - ${getSinger(this)}"
                    }

                    it.tracks[2].apply {
                        loadImg(al.picUrl, ivCover3)
                        tvSongInfo3.text = "3  $name"
                        tvSinger3.text = " - ${getSinger(this)}"
                    }
                }
            }
        }
    }

    private fun getSinger(track: Track): String {
        val sb = StringBuilder()
        for (ar in track.ar) {
            sb.append(ar.name).append('/')
        }
        return sb.substring(0, sb.length - 1)
    }

    private fun loadImg(url: String, view: ImageView) {
        val options = RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().transform(RoundedCorners(20))
        GlideApp.with(context)
                .load("$url?param=200y200")
                .apply(options)
                .into(view)
    }
}