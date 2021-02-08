package com.fr1014.mycoludmusic.ui.home.playlist.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.databinding.DialogPlaylistInfoBinding
import com.fr1014.mycoludmusic.utils.BlurImageUtils
import com.fr1014.mycoludmusic.utils.PaletteBgUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils

class PlayListInfoDialog(context: Context) : Dialog(context, R.style.ScreenDialog) {
    private var mViewBinding: DialogPlaylistInfoBinding? = null
    private var dialogWindow : Window? = null

    init {
        initDialog()
    }

    private fun initDialog() {
        mViewBinding = DialogPlaylistInfoBinding.inflate(LayoutInflater.from(context))
        mViewBinding?.let {
            setContentView(it.root)
        }
        dialogWindow = window
        val params = dialogWindow?.attributes
        params?.apply {
            width = ScreenUtils.getScreenWidth()
            height = ScreenUtils.getScreenHeight()
        }
        dialogWindow?.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(false)
    }

    fun setData(playlistDetailEntity: PlayListDetailEntity) {
        mViewBinding?.apply {
            playlistDetailEntity.let {
                Glide.with(ivCover)
                        .asBitmap()
                        .load(it.playlist.coverImgUrl)
                        .error(R.drawable.ic_placeholder)
                        .apply(RequestOptions().centerCrop().transform(RoundedCorners(64)))
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val foregroundDrawable = BlurImageUtils.getForegroundDrawable(context, resource, 10)
                                dialogWindow?.setBackgroundDrawable(foregroundDrawable)
                                ivCover.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                        })
                tvTitle.text = it.playlist.name
                tvDescription.text = it.playlist.description
                val tagAdapter = TagAdapter(R.layout.item_playlist_tag)
                rvTag.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = tagAdapter
                }
                tagAdapter.setData(playlistDetailEntity.playlist.tags)
            }
        }

    }
}

class TagAdapter(layoutResId: Int) : BaseAdapter<String, BaseViewHolder>(layoutResId) {

    override fun convert(holder: BaseViewHolder, data: String) {
        holder.setText(R.id.tv_item_tag, data)
    }

}