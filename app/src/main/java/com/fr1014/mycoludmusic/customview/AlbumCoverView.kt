package com.fr1014.mycoludmusic.customview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fr1014.mycoludmusic.databinding.AlubmCoverviewBinding
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay
import com.fr1014.mycoludmusic.musicmanager.player.PlayerEvent
import com.fr1014.mycoludmusic.musicmanager.player.PlayerType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Create by fanrui on 2020/12/11
 * Describe:
 */
class AlbumCoverView : ConstraintLayout, LifecycleObserver {
    private lateinit var rotationAnimator: ObjectAnimator
    private lateinit var mViewBinding: AlubmCoverviewBinding

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        mViewBinding = AlubmCoverviewBinding.inflate(LayoutInflater.from(context), this)
        initAnimator()
        mViewBinding.apply {
            ivArm.pivotX = 0f
            ivArm.pivotY = 0f
            if (MyAudioPlay.get().isPlaying()) {
                startAnimator()
                ObjectAnimator.ofFloat(ivArm, "rotation", 0f).start()
            } else {
                ObjectAnimator.ofFloat(ivArm, "rotation", 0f, -20f).start()
            }
        }
    }

    private fun initAnimator() {
        rotationAnimator = ObjectAnimator.ofFloat(mViewBinding.flCover, "rotation", 0f, 360f) //旋转的角度可有多个
        rotationAnimator.apply {
            duration = 25000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART //匀速
            interpolator = LinearInterpolator() //让旋转动画一直转，不停顿的重点
        }
    }

    fun startAnimator() {
        rotationAnimator.start()
    }

    fun pauseAnimator() {
        if (rotationAnimator.isRunning) {
            rotationAnimator.pause()
        }
    }

    fun resumeAnimator() {
        if (rotationAnimator.isPaused) {
            rotationAnimator.resume()
        }
    }

    private fun resumeOrStartAnimator() {
        if (rotationAnimator.isPaused) {
            rotationAnimator.resume() //继续（在暂停的位置继续动画）
        } else {
            startAnimator()
        }
    }

    fun endAnimator() {
        if (rotationAnimator.isRunning || rotationAnimator.isPaused) {
            rotationAnimator.end() //结束（回到原始位置）
        }
    }

    fun songImgSetBitmap(resource: Bitmap?) {
        mViewBinding.civSongImg.setImageBitmap(resource)
    }

    private fun startArmAnimator() {
        val ra = ObjectAnimator.ofFloat(mViewBinding.ivArm, "rotation", -20f, 0f)
        ra.duration = 800
        ra.start()
    }

    private fun endArmAnimator() {
        val ra = ObjectAnimator.ofFloat(mViewBinding.ivArm, "rotation", 0f, -20f)
        ra.duration = 800
        ra.start()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayerEvent(event: PlayerEvent) {
        when (event.type) {
            PlayerType.OnPlayerStart -> {
                resumeOrStartAnimator()
                startArmAnimator()
            }
            PlayerType.OnPlayerPause -> {
                pauseAnimator()
                endArmAnimator()
            }
            PlayerType.OnPlayerComplete -> {
                endAnimator()
            }
            else -> {}
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        EventBus.getDefault().register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (MyAudioPlay.get().isPlaying()) {
            resumeAnimator()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        pauseAnimator()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        EventBus.getDefault().unregister(this)
        endAnimator()
    }
}