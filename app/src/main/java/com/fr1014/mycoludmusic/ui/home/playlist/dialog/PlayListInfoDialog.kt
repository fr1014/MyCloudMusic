package com.fr1014.mycoludmusic.ui.home.playlist.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.databinding.DialogPlaylistInfoBinding
import com.fr1014.mycoludmusic.utils.*
import com.fr1014.mycoludmusic.utils.glide.GlideApp

class PlayListInfoDialog(context: Context) : Dialog(context, R.style.ScreenDialog) {
    private var mViewBinding: DialogPlaylistInfoBinding? = null
    private var dialogWindow: Window? = null
    private var toast: Toast? = null

    init {
        initDialog()
    }

    private fun initDialog() {
        mViewBinding = DialogPlaylistInfoBinding.inflate(LayoutInflater.from(context))
        mViewBinding?.let {
            setContentView(it.root)
        }
        dialogWindow = window
        dialogWindow?.apply {
            StatusBarUtils.setImmersiveStatusBar(this, false)
            attributes.apply {
                width = ScreenUtils.getScreenWidth()
                height = ScreenUtils.getScreenHeight() + ScreenUtils.getStatusBarHeight() * 2
            }
            setGravity(Gravity.VERTICAL_GRAVITY_MASK)
        }
        setCanceledOnTouchOutside(false)
    }

    fun setData(playlistDetailEntity: PlayListDetailEntity) {
        var coverBitmap: Bitmap? = null
        mViewBinding?.apply {
            playlistDetailEntity.let { playlistDetailEntity ->
                GlideApp.with(ivCover)
                        .asBitmap()
                        .load(playlistDetailEntity.playlist.coverImgUrl)
                        .error(R.drawable.ic_placeholder)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val foregroundDrawable = BlurImageUtils.getForegroundDrawable(context, resource, 10)
                                dialogWindow?.setBackgroundDrawable(foregroundDrawable)
                                val roundedBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                                roundedBitmapDrawable.cornerRadius = 48f
                                ivCover.setImageDrawable(roundedBitmapDrawable)
                                coverBitmap = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                        })
                tvTitle.text = playlistDetailEntity.playlist.name
                tvDescription.text = playlistDetailEntity.playlist.description
                tvSave.setOnClickListener {
                    //防止快速点击
                    if (!CommonUtils.isFastClick()) {
                        playlistDetailEntity.playlist.apply {
                            toast = CommonUtils.toastShort("正在保存中...")
                            coverBitmap?.let {
                                val saveResult = FileUtils.saveBitmap(context, it, name + "_" + coverImgId)
                                if (saveResult) {
                                    CommonUtils.toastShort("保存成功")
                                } else {
                                    CommonUtils.toastShort("保存失败")
                                }
                            }
                        }
                    } else {
                        toast?.cancel()
                        toast = CommonUtils.toastLong("保存中，勿重复点击");
                    }
                }
            }
            val tagAdapter = TagAdapter(R.layout.item_playlist_tag)
            rvTag.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = tagAdapter
            }
            tagAdapter.setData(playlistDetailEntity.playlist.tags)
        }
    }

}

class TagAdapter(layoutResId: Int) : BaseAdapter<String, BaseViewHolder>(layoutResId) {

    override fun convert(holder: BaseViewHolder, data: String) {
        holder.setText(R.id.tv_item_tag, data)
    }

}