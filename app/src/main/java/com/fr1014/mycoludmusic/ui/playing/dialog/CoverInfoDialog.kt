package com.fr1014.mycoludmusic.ui.playing.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.databinding.DialogCoverInfoBinding
import com.fr1014.mycoludmusic.musicmanager.player.*
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.FileUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils
import com.fr1014.mycoludmusic.utils.StatusBarUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CoverInfoDialog(context: Context) : Dialog(context, R.style.ScreenDialog) {
    val from = "COVER_INFO_DIALOG"
    private var mViewBinding: DialogCoverInfoBinding? = null
    private var dialogWindow: Window? = null
    private var toast: Toast? = null
    private var coverBitmap: Bitmap? = null
    private var music: Music? = null

    init {
        initDialog()
    }

    private fun initDialog() {
        EventBus.getDefault().register(this)
        mViewBinding = DialogCoverInfoBinding.inflate(LayoutInflater.from(context))
        mViewBinding?.apply {
            setContentView(root)
            ivCover.layoutParams.height = ScreenUtils.getScreenWidth()
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
        initListener()
    }

    private fun initListener() {
        mViewBinding?.tvSave?.setOnClickListener {
            //防止快速点击
            if (!CommonUtils.isFastClick()) {
                toast?.cancel()
                if (music == null) {
                    toast = CommonUtils.toastShort("图片正在加载中，稍后再试")
                    return@setOnClickListener
                }
                toast = CommonUtils.toastShort("正在保存中...")
                coverBitmap?.let {
                    val saveResult = FileUtils.saveBitmap(context, it, music?.title + "_" + music?.album)
                    if (saveResult) {
                        CommonUtils.toastShort("保存成功")
                    } else {
                        CommonUtils.toastShort("保存失败")
                    }
                }
            } else {
                toast?.cancel()
                toast = CommonUtils.toastLong("保存中，勿重复点击");
            }
        }
    }

    fun setData(music: Music) {
        this.music = music
        music.loadRemoteCover(from)
//        mViewBinding?.apply {
//            GlideApp.with(context)
//                    .asBitmap()
//                    .load("${music.imgUrl}?param=700y700")
//                    .into(object : CustomTarget<Bitmap>(){
//                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                            ivCover.background = null
//                            ivCover.layoutParams.height = ScreenUtils.getScreenHeight() + ScreenUtils.getStatusBarHeight() * 2
//                            ivCover.setImageBitmap(resource)
//                            coverBitmap = resource
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            CommonUtils.toastShort("图片加载失败，请保证网络状态良好的情况下再重试")
//                        }
//
//                    })
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMusicCoverEvent(event: MusicCoverEvent) {
        if (event.from == from) {
            when (event.type) {
                CoverStatusType.Loading, CoverStatusType.Fail -> {
                    CommonUtils.toastShort("图片加载失败，请保证网络状态良好的情况下再重试")
                }
                CoverStatusType.Success -> {
                    mViewBinding?.apply {
                        ivCover.background = null
                        ivCover.layoutParams.height = ScreenUtils.getScreenHeight() + ScreenUtils.getStatusBarHeight() * 2
                        ivCover.setImageBitmap(event.coverLocal)
                        coverBitmap = event.coverLocal
                    }
                }
            }
        }
    }

    override fun dismiss() {
        EventBus.getDefault().unregister(this)
        super.dismiss()
    }
}