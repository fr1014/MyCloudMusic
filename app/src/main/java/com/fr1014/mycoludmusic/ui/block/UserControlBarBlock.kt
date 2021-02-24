package com.fr1014.mycoludmusic.ui.block

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.databinding.CustomUserControlbarBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicViewModel
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.CommonUtils

class UserControlBarBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private lateinit var mViewBinding: CustomUserControlbarBinding
    private var mViewModel: CurrentPlayMusicViewModel? = null
    private var mLifecycleOwner: LifecycleOwner? = null
    private var musicLikes: MutableList<MusicLike>
    private var userLikePid: Long? = null

    init {
        initView()
        musicLikes = ArrayList()
    }

    private fun initView() {
        mViewBinding = CustomUserControlbarBinding.inflate(LayoutInflater.from(context), this, false)
        addView(mViewBinding.root)
        initListener()
    }

    private fun initViewModelObserver() {
        mLifecycleOwner?.let { owner ->
            val userId = Preferences.getUserProfile()?.userId.toString()
            userLikePid = Preferences.getUserLikePid(userId)
            if (userLikePid == 0L) {
                mViewModel?.getWYUserPlayList()
            }

            mViewModel?.getLikeList()?.observe(owner, Observer {
                musicLikes.clear()
                musicLikes.addAll(it)
                val playMusic = AudioPlayer.get().currentMusic
                initLikeIcon(playMusic)
            })

            mViewModel?.playlistWYLive?.observe(owner, Observer {
                if (!CollectionUtils.isEmptyList(it)) {
                    userLikePid = it[0].id
                    Preferences.saveUserLikePid(userId, userLikePid!!)
                }
            })
        }
    }

    private fun initListener() {
        mViewBinding.apply {
            ivLike.setOnClickListener(this@UserControlBarBlock)
            ivDownload.setOnClickListener(this@UserControlBarBlock)
            ivComment.setOnClickListener(this@UserControlBarBlock)
            ivMv.setOnClickListener(this@UserControlBarBlock)
            ivMore.setOnClickListener(this@UserControlBarBlock)
        }
    }

    fun initUserControlBar(mViewModel: CurrentPlayMusicViewModel, mLifecycleOwner: LifecycleOwner) {
        this.mViewModel = mViewModel
        this.mLifecycleOwner = mLifecycleOwner
        initViewModelObserver()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_like -> {
                startLikeAnimator()
                val playMusic = AudioPlayer.get().currentMusic
                if (userLikePid == 0L) {
                    CommonUtils.toastShort("尚未登录")
                    return
                }
                if (playMusic.id != 0L) {
                    val isLikeMusic = playMusic.isLikeMusic(musicLikes)
                    mViewModel?.getLikeSong(!isLikeMusic, userLikePid!!, playMusic.id.toString(), System.currentTimeMillis().toString())
                } else {
                    CommonUtils.toastLong("该歌曲源不是网易，暂时无法收藏")
                }
            }
            else ->{
                CommonUtils.toastShort(context.resources.getString(R.string.dev))
            }
        }
    }

    fun initLikeIcon(music: Music) {
        if (music.id == 0L) {
            mViewBinding.ivLike.setImageDrawable(context.getDrawable(R.drawable.selector_like))
            return
        }
        val isLikeMusic = music.isLikeMusic(musicLikes)
        mViewBinding.ivLike.apply {
            if (isLikeMusic) {
                setImageDrawable(context.getDrawable(R.drawable.ic_like_fill))
            } else {
                setImageDrawable(context.getDrawable(R.drawable.selector_like))
            }
        }
    }

    private fun startLikeAnimator(){
        val ivLikeX = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleX", 1f, 1.4f)
        val ivLikeY = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleY", 1f, 1.4f)
        val ivLikeX2 = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleX", 1.4f, 1f)
        val ivLikeY2 = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleY", 1.4f, 1f)
        AnimatorSet().apply {
            playTogether(ivLikeX,ivLikeY)
            duration = 200
            start()
        }.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                AnimatorSet().apply {
                    playTogether(ivLikeX2,ivLikeY2)
                    duration = 200
                    start()
                }
            }
        })
    }
}

fun Music.isLikeMusic(musicLikes: MutableList<MusicLike>): Boolean {
    for (musicLike in musicLikes) {
        if (musicLike.id == id) {
            return true
        }
    }
    return false
}