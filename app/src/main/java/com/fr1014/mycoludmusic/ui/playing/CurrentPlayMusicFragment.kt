package com.fr1014.mycoludmusic.ui.playing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.ViewModelProvider
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.customview.PlayControlBarView.OnPlayControlBarClick
import com.fr1014.mycoludmusic.databinding.FragmentCurrentMusicBinding
import com.fr1014.mycoludmusic.musicmanager.lrcview.LrcView
import com.fr1014.mycoludmusic.musicmanager.lrcview.LrcView.OnPlayClickListener
import com.fr1014.mycoludmusic.musicmanager.player.*
import com.fr1014.mycoludmusic.ui.home.playlistdialog.PlayDialogFragment
import com.fr1014.mycoludmusic.utils.*
import com.fr1014.mycoludmusic.utils.reboundlayout.OnBounceDistanceChangeListener
import com.fr1014.mymvvm.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class CurrentPlayMusicFragment : BaseFragment<FragmentCurrentMusicBinding, CurrentPlayMusicViewModel>(), View.OnClickListener, OnPlayClickListener, OnBounceDistanceChangeListener {
    private val COVER_FROM = "CurrentPlayMusicFragment"
    private lateinit var player: MediaPlayer
    private lateinit var onBounceDistanceChangeListener: OnBounceDistanceChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReBoundActivity) {
            onBounceDistanceChangeListener = context
        }
    }

    override fun getViewBinding(container: ViewGroup): FragmentCurrentMusicBinding {
        return FragmentCurrentMusicBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mViewBinding.reboundLayout.onBounceDistanceChangeListener = this
        return view
    }

    override fun initViewModel(): CurrentPlayMusicViewModel {
        return ViewModelProvider(this, AppViewModelFactory.getInstance(MyApplication.getInstance())).get(CurrentPlayMusicViewModel::class.java)
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        initSystemBar()
        viewLifecycleOwner.lifecycle.addObserver(mViewBinding.albumCoverView)
        mViewBinding.userControlBar.initUserControlBar(mViewModel, this)
        initListener()
        initCoverLrc()
        if (MyAudioPlay.get().isPlaying()) {
            mViewBinding.playControlBar.setStateImage(R.drawable.selector_stop_state)
        } else {
            mViewBinding.playControlBar.setStateImage(R.drawable.selector_play_state)
        }
        mViewBinding.playControlBar.initPlayMode()
        player = MyAudioPlay.get().mediaPlayer
        val music: Music = MyAudioPlay.get().getCurrentMusic() ?: return
        onChange(music)
        if (MyAudioPlay.get().isPlaying()) {
            mViewBinding.albumCoverView.startAnimator()
        }
    }

    private fun initListener() {
        mViewBinding.apply {
            icBack.setOnClickListener(this@CurrentPlayMusicFragment)
            flPlay.setOnClickListener(this@CurrentPlayMusicFragment)
            albumCoverView.mViewBinding.civSongImg.setOnClickListener(this@CurrentPlayMusicFragment)

            playControlBar.setPlayControlBarClick(object : OnPlayControlBarClick {
                override fun pre(pre: Music) {
                    changeMusicPlay(pre)
                }

                override fun next(next: Music) {
                    changeMusicPlay(next)
                }

                override fun openMenu() {
                    PlayDialogFragment().show(parentFragmentManager, "playlist_dialog")
                }
            })

            sbProgress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    //拖动进度条
                    mViewBinding.tvNowTime.text = CommonUtils.formatTime(progress.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    if (MyAudioPlay.get().isPlaying() || MyAudioPlay.get().isPausing()) {
                        val progress = seekBar.progress
                        MyAudioPlay.get().seekTo(progress)
                    } else {
                        seekBar.progress = 0;
                    }
                }
            })
        }
    }

    /**
     * 修改状态栏字颜色为白色
     * 沉浸式状态栏
     */
    private fun initSystemBar() {
        StatusBarUtils.setImmersiveStatusBar(activity?.window, false)
        val top = ScreenUtils.getStatusBarHeight()
        mViewBinding.llContent.setPadding(0, top, 0, 0)
    }

    override fun initViewObservable() {
        mViewModel.getSongLrcPath().observe(viewLifecycleOwner, { lrcPath ->
            mViewBinding.lrcView.setLabel("该歌曲暂无歌词")
            if (lrcPath[0] != "") {
                if (lrcPath[1] != "") {
                    mViewBinding.lrcView.loadLrc(File(lrcPath[0]), File(lrcPath[1]))
                } else {
                    mViewBinding.lrcView.loadLrc(File(lrcPath[0]))
                }
            } else {
                mViewBinding!!.lrcView.loadLrc("[00:00.000]该歌曲暂无歌词")
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_back -> {
                mViewBinding.albumCoverView.endAnimator()
                activity?.onBackPressed()
            }
            R.id.fl_play, R.id.civ_songImg -> if (mViewBinding.albumCoverView.visibility == View.VISIBLE) {
                mViewBinding.albumCoverView.visibility = View.GONE
                setTipsVisibility(View.GONE)
                mViewBinding.albumCoverView.pauseAnimator()
                mViewBinding.llLrc.visibility = View.VISIBLE
                getSongLrc(MyAudioPlay.get().getCurrentMusic())
            }
        }
    }

    private fun initViewData(music: Music) {
        initSeekBarData(music)
        setBitmap(FileUtils.getCoverLocal(COVER_FROM, music));
        setTipsVisibility(View.VISIBLE)
        resetSeekBarData()
        setMusicInfo(music)
    }

    private fun setTipsVisibility(visibility: Int) {
        val sourceType = MyAudioPlay.get().getCurrentMusic()?.sourceType
        if (sourceType != MusicSource.KW_MUSIC.sourceType) {
            mViewBinding.tvTips.visibility = View.GONE
            return
        } else {
            val tips = "该歌曲源并非来自\"网易\"\n匹配的歌曲源可能不正确"
            mViewBinding.tvTips.text = tips
        }
        mViewBinding.tvTips.visibility = visibility
    }

    private fun setMusicInfo(music: Music) {
        mViewBinding.tvTitle.text = music.title
        mViewBinding.tvArtist.text = music.artist
    }

    private fun initSeekBarData(music: Music) {
        var duration = music.duration
        if (duration == 0L && !MyAudioPlay.get().isIdle()) {
            duration = player.duration.toLong()
        }
        mViewBinding.sbProgress.max = Math.abs(duration).toInt()
        //        sbProgress.setSecondaryProgress(0);
        mViewBinding!!.tvDuration.text = CommonUtils.formatTime(duration)
    }

    private fun resetSeekBarData() {
        mViewBinding.tvNowTime.setText(R.string.start_seekbar)
        mViewBinding.sbProgress.secondaryProgress = 0
    }

    //音乐旋转图、歌词
    private fun initCoverLrc() {
        mViewBinding.lrcView.setDraggable(true, this)
        mViewBinding.lrcView.setOnTapListener { view, x, y ->
            mViewBinding.albumCoverView.visibility = View.VISIBLE
            setTipsVisibility(View.VISIBLE)
            if (MyAudioPlay.get().isPlaying()) {
                mViewBinding.albumCoverView.resumeAnimator()
            }
            mViewBinding.llLrc.visibility = View.GONE
        }
    }

    private fun setBitmap(resource: Bitmap?) {
        if (resource != null) {
            mViewBinding.albumCoverView.songImgSetBitmap(resource)
            mViewBinding.biBackground.setBitmap(resource)
        } else {
            mViewBinding.biBackground.setBackgroundDrawable(ResourceUtils.getGrayDrawable(context, R.drawable.palying_default_bg))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayerEvent(event: PlayerEvent) {
        when (event.type) {
            PlayerType.OnPlayerStart -> {
                mViewBinding.playControlBar.setStateImage(R.drawable.selector_stop_state)
            }
            PlayerType.OnPlayerPause -> {
                mViewBinding.playControlBar.setStateImage(R.drawable.selector_play_state)
            }
            PlayerType.OnChange -> {
                event.music?.let { music ->
                    onChange(music)
                }
            }
            PlayerType.OnPublish -> onPublish(event.progress)
            PlayerType.OnBufferingUpdate -> if (event.percent > 0) {
                mViewBinding.sbProgress.secondaryProgress = mViewBinding.sbProgress.max * 100 / event.percent
            }
            else -> {}
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMusicCoverEvent(event: MusicCoverEvent) {
        if (event.from == COVER_FROM_COMMON || event.from == COVER_FROM) {
            when (event.type) {
                CoverStatusType.Loading, CoverStatusType.Fail -> {
                    mViewBinding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(resources, R.drawable.film))
                }
                CoverStatusType.Success -> {
                    MyAudioPlay.get().getCurrentMusic()?.let {
                        if (it.isSameMusic(event.music)) {
                            setBitmap(event.coverLocal)
                        }
                    }
                }
            }
        }
    }

    private fun onChange(music: Music) {
        initViewData(music)
        changeMusicPlay(music)
        mViewBinding.userControlBar.musicChange(music)
    }

    private fun changeMusicPlay(music: Music) {
        if (mViewBinding.llLrc.visibility == View.VISIBLE) {
            getSongLrc(music) //切换歌时，请求歌词
        }
        mViewBinding.albumCoverView.endAnimator()
    }

    private fun onPublish(progress: Int) {
        mViewBinding.tvNowTime.text = CommonUtils.formatTime(player.currentPosition.toLong())
        mViewBinding.sbProgress.progress = progress
        if (isVisible && mViewBinding!!.llLrc.visibility == View.VISIBLE && mViewBinding!!.lrcView.hasLrc()) {
            mViewBinding!!.lrcView.updateTime(progress.toLong())
        }
    }

    private fun getSongLrc(music: Music?) {
        mViewBinding.apply {
            music?.let { music ->
                val mTag = music.title + music.artist
                val tag = lrcView.tag
                if (tag == null || tag != mTag) {
                    lrcView.tag = mTag
                    lrcView.loadLrc(File(""))
                    lrcView.setLabel("正在搜索歌词")
                    mViewModel.getSongLrc(music);
                }
            }
        }
    }

    //点击歌词播放歌词所在时间点
    override fun onPlayClick(view: LrcView, time: Long): Boolean {
        if (MyAudioPlay.get().isPlaying() || MyAudioPlay.get().isPausing()) {
            MyAudioPlay.get().seekTo(time.toInt())
            if (MyAudioPlay.get().isPausing()) {
                MyAudioPlay.get().startPlayer()
            }
            return true;
        }
        return false
    }

    override fun onDestroy() {
//        if (getActivity() != null) {
//            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), true);
//        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onDistanceChange(distance: Int, direction: Int) {
        onBounceDistanceChangeListener.onDistanceChange(distance, direction)
    }

    override fun onFingerUp(distance: Int, direction: Int) {
        onBounceDistanceChangeListener.onFingerUp(distance, direction)
    }
}