package com.fr1014.mycoludmusic.ui.block

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.BaseConfig
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.enum.CommentType
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.databinding.CustomUserControlbarBinding
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.musicmanager.player.Music
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay
import com.fr1014.mycoludmusic.musicmanager.player.isLikeMusic
import com.fr1014.mycoludmusic.ui.home.comment.CommentActivity
import com.fr1014.mycoludmusic.ui.login.LoginActivity
import com.fr1014.mycoludmusic.ui.mv.MVActivity
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
    private var userLikePid = 0L
    private var playMusic: Music? = null

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

            mViewModel?.let { viewModel ->

                viewModel.getLikeList().observe(owner, Observer {
                    musicLikes.clear()
                    musicLikes.addAll(it)
                    playMusic?.let { playMusic ->
                        initLikeIcon(playMusic)
                    }
                })

                viewModel.playlistWYInfo.observe(owner, Observer {
                    if (!CollectionUtils.isEmptyList(it)) {
                        userLikePid = it[0].id
                        Preferences.saveUserLikePid(userId, userLikePid)
                    }
                })

                viewModel.mvMusic.observe(owner, {
                    MVActivity.startMVActivity(context, it.mvUrl, it.title, true)
                    MyAudioPlay.get().pausePlayer()
                })
            }
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
                if (BaseConfig.isLogin) {
                    startLikeAnimator()
                    if (userLikePid == 0L) {
                        CommonUtils.toastShort("尚未登录")
                        return
                    }
                    playMusic?.let { playMusic ->
//                        if (!TextUtils.isEmpty(playMusic.id)) {
//                            val isLikeMusic = playMusic.isLikeMusic(musicLikes)
//                            mViewModel?.getLikeSong(!isLikeMusic, userLikePid, playMusic.id, System.currentTimeMillis().toString())
//                        } else {
//                            CommonUtils.toastLong("该歌曲源不是网易，暂时无法收藏")
//                        }
                    }
                } else {
                    if (context is MainActivity) {
                        CommonUtils.toastShort(context.getString(R.string.tips_login))
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
                }
            }
            R.id.iv_comment -> {
                playMusic?.let { playMusic ->
                    try {
                        val id = playMusic.id.toLong()
                        CommentActivity.getInstance(context, CommentType.SONG.type, id, 20, 0L)
                    } catch (e: Exception) {
                        CommonUtils.toastShort("该歌曲非来自网易云，暂未支持评论功能！")
                    }
                }
            }
            R.id.iv_mv -> {
                playMusic?.let { playMusic ->
                    when {
                        TextUtils.isEmpty(playMusic.mvId) || playMusic.mvId == "0" -> {
                            CommonUtils.toastShort("该歌曲暂时无MV")
                        }
                        !TextUtils.isEmpty(playMusic.mvId) -> {
                            mViewModel?.getWYMVInfo(playMusic)
                        }
                        else -> {
                            CommonUtils.toastShort("该歌曲非来自网易云,暂时不支持播放MV")
                        }
                    }

                }
            }
            else -> {
                CommonUtils.toastShort(context.resources.getString(R.string.dev))
            }
        }
    }

    fun musicChange(music: Music) {
        playMusic = music
        initMvIcon(music)
        initLikeIcon(music)
    }

    private fun initMvIcon(music: Music) {
        //音乐非网易 或 歌曲本身就无mv
        if (TextUtils.isEmpty(music.mvId) || music.mvId == "0") {
            mViewBinding.ivSlash.visibility = View.VISIBLE
        }else{
            mViewBinding.ivSlash.visibility = View.GONE
        }
    }

    private fun initLikeIcon(music: Music) {
        val isLikeMusic = music.isLikeMusic(musicLikes)
        mViewBinding.ivLike.apply {
            if (isLikeMusic) {
                setImageDrawable(context.getDrawable(R.drawable.ic_like_fill))
            } else {
                setImageDrawable(context.getDrawable(R.drawable.selector_like))
            }
        }
    }

    private fun startLikeAnimator() {
        val ivLikeX = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleX", 1f, 1.4f)
        val ivLikeY = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleY", 1f, 1.4f)
        val ivLikeX2 = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleX", 1.4f, 1f)
        val ivLikeY2 = ObjectAnimator.ofFloat(mViewBinding.ivLike, "scaleY", 1.4f, 1f)
        AnimatorSet().apply {
            playTogether(ivLikeX, ivLikeY)
            duration = 200
            start()
        }.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                AnimatorSet().apply {
                    playTogether(ivLikeX2, ivLikeY2)
                    duration = 200
                    start()
                }
            }
        })
    }
}