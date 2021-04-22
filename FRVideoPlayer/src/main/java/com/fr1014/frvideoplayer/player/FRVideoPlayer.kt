package com.fr1014.frvideoplayer.player

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fr1014.frvideoplayer.R
import com.fr1014.frvideoplayer.databinding.FrPlayerViewBinding
import com.fr1014.frvideoplayer.utils.PlayerUtils
import java.util.*

/**
 * Create by fanrui on 2021/4/16
 * Describe:视频播放
 *
 * 宿主Activity需要声明 android:configChanges="orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden"
 */
class FRVideoPlayer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SurfaceHolder.Callback, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, LifecycleObserver {

    private lateinit var viewBinding: FrPlayerViewBinding
    private lateinit var activity: Activity
    val mHandler = Handler(Looper.getMainLooper())

    // SurfaceView的创建比较耗时，要注意
    private var surfaceHolder: SurfaceHolder? = null
    private var mediaPlayer: MediaPlayer? = null

    //控件的位置信息
    private var mediaPlayerX = 0F
    private var mediaPlayerY = 0F
    private var playerViewW = 0
    private var playerViewH = 0

    //默认宽高比16:9
    private val defaultWidthProportion = 16
    private val defaultHeightProportion = 9

    //计时器
    private var timer_video: Timer? = null
    private var task_video: TimerTask? = null
    private var timer_controller: Timer? = null
    private var task_controller: TimerTask? = null

    private var videoPath = ""
    private var videoTitle = ""
    var isFirstPlay: Boolean = false

    //标记暂停和播放状态
    private var isPausing = false

    init {
        init()
    }

    private fun init() {
        activity = context as Activity
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        viewBinding = FrPlayerViewBinding.inflate(LayoutInflater.from(context), this, false)
        addView(viewBinding.root)

        initViews()

        //初始化SurfaceView
        initSurfaceView()

        //初始化位置信息
        viewTreeObserver.addOnGlobalLayoutListener {
            if (playerViewW == 0) {
                mediaPlayerX = x
                mediaPlayerY = y
                playerViewW = width
                playerViewH = height
            }
        }
    }

    private fun initViews() {

        viewBinding.apply {
            ivStatus.setOnClickListener(this@FRVideoPlayer)
            root.setOnClickListener(this@FRVideoPlayer)
            back.setOnClickListener(this@FRVideoPlayer)
            progressBar.setOnSeekBarChangeListener(this@FRVideoPlayer)
            root.isClickable = false
            tvTitle.text = videoTitle
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                activity.finish()
            }
            R.id.root -> {
                if (viewBinding.topVideo.visibility == View.GONE) {
                    showControllerMenu()
                } else {
                    destroyControllerTask(true)
                }
            }
            R.id.iv_status -> {
                if (isPausing) {
                    startVideo()
                } else {
                    pauseVideo()
                }
            }
        }
    }

    private fun initSurfaceView() {
        // 得到SurfaceView容器，播放的内容就是显示在这个容器里面
        surfaceHolder = viewBinding.playerSurfaceView.holder
        // 使用SurfaceHolder设置屏幕高亮，注意：所有的View都可以设置 设置屏幕高亮
//        surfaceHolder.setKeepScreenOn(true)
        //surfaceCreated、surfaceChanged、surfaceDestroyed
        surfaceHolder?.addCallback(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val screenWidth: Int = PlayerUtils.getScreenWidth(activity)

        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            //计算视频的大小16：9
            playerViewW = screenWidth
            playerViewH = screenWidth * defaultHeightProportion / defaultWidthProportion
            //竖屏通知
//            if (onScreenOrientationListener != null) {
//                onScreenOrientationListener.orientation_portrait()
//            }
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PlayerUtils.checkNavigationBarShow(activity)) {
                PlayerUtils.hideBottomUIMenu(activity)
            }
            playerViewW = screenWidth - PlayerUtils.getStatusBarHeight(activity)
            playerViewH = PlayerUtils.getScreenHeight(activity)
            //横屏通知
//            if (onScreenOrientationListener != null) {
//                onScreenOrientationListener.orientation_landscape()
//            }
        }
        fitVideoSize()
    }

    /*
     * SurfaceView相关接口实现
     */

    //onResume时调用
    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()

            mediaPlayer?.let { mediaPlayer ->
                mediaPlayer.setAudioAttributes(
                        AudioAttributes.Builder()                                       // 设置音效相关属性
                                .setUsage(AudioAttributes.USAGE_MEDIA)                  // 设置音效使用场景
                                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)     // 设置音效的类型
                                .build()
                )
                mediaPlayer.apply {
                    setOnCompletionListener(this@FRVideoPlayer)     // 播放完成监听
                    setOnPreparedListener(this@FRVideoPlayer)       // 异步准备的一个监听函数，准备好了就调用里面的方法
                    setOnErrorListener(this@FRVideoPlayer)          // 播放错误
                    setOnBufferingUpdateListener(this@FRVideoPlayer) // 缓冲监听
                }
            }

            //第一次初始化需不需要主动播放
            if (isFirstPlay) {
                playVideo(videoPath, videoTitle);
            } else {
                isFirstPlay = true;
            }
        }

        mediaPlayer?.setDisplay(surfaceHolder)                                   //添加到容器中
        if (isPausing) {
            isPausing = false
            mediaPlayer?.start()
        }
    }

    //surfaceView尺寸改变时调用
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    //onPause时调用
    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    /*
     * MediaPlayer 相关接口实现
     */

    override fun onCompletion(mp: MediaPlayer) {
        mp.seekTo(0)
        mp.start()
    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.start()
        viewBinding.progressBar.max = mp.duration
        fitVideoSize()
        viewBinding.root.isClickable = true
        showControllerMenu()  //展示控制界面（进度条，状态按钮。。。）
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {

        return true
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {

    }

    /*
     *  SeekBar相关接口实现
     */

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(seekBar.progress)
            }
        }
    }

    // =========================================================================

    private fun fitVideoSize() {

        mediaPlayer?.let { mediaPlayer ->

            if (playerViewW == 0) {
                playerViewW = width
                playerViewH = height
            }
            //适配视频的高度
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight
            val parentWidth = playerViewW
            val parentHeight = playerViewH
            //判断视频宽高和父布局的宽高
            val surfaceViewW: Int
            val surfaceViewH: Int
            if (videoWidth.toFloat() / videoHeight.toFloat() > parentWidth.toFloat() / parentHeight.toFloat()) {
                surfaceViewW = parentWidth
                surfaceViewH = videoHeight * surfaceViewW / videoWidth
            } else {
                surfaceViewH = parentHeight
                surfaceViewW = videoWidth * surfaceViewH / videoHeight
            }
            //显示的分辨率,不设置为视频默认
            surfaceHolder?.setFixedSize(surfaceViewW, surfaceViewH)

            //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充
            val params = viewBinding.playerSurfaceView.layoutParams as RelativeLayout.LayoutParams
            params.height = surfaceViewH
            params.width = surfaceViewW
            params.addRule(RelativeLayout.CENTER_IN_PARENT, viewBinding.rlSurface.id)
            viewBinding.playerSurfaceView.layoutParams = params
        }
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(videoPath)
            mediaPlayer.prepareAsync()
            //视频缩放模式
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        }
    }

    //------------------------------------------------------------
    // ## 计时器相关方法 ##
    //------------------------------------------------------------
    private fun initTimeTask() {
        initControllerTask()
        initVideoTask()
    }

    private fun initVideoTask() {
        timer_video = Timer()
        task_video = object : TimerTask() {
            override fun run() {
                mHandler.post {
                    if (mediaPlayer == null) return@post
                    mediaPlayer?.let { mediaPlayer ->
                        viewBinding.apply {
                            tvStart.text = PlayerUtils.converLongTimeToStr(mediaPlayer.currentPosition)
                            tvEnd.text = PlayerUtils.converLongTimeToStr(mediaPlayer.duration)
                            //进度条
                            progressBar.progress = mediaPlayer.currentPosition
                        }
                    }
                }
            }
        }
        timer_video?.schedule(task_video, 0, 1000)
    }

    private fun destroyVideoTask() {
        timer_video?.apply {
            cancel()
            timer_video = null
        }
        task_video?.apply {
            cancel()
            task_video = null
        }
    }

    private fun initControllerTask() {
        timer_controller = Timer()
        task_controller = object : TimerTask() {
            override fun run() {
                destroyControllerTask(false)
            }
        }
        timer_controller?.schedule(task_controller, 5000)
    }

    private fun destroyControllerTask(isMainThread: Boolean) {
        if (isMainThread) {
            dismissControllerMenu()
        } else {
            mHandler.post {
                dismissControllerMenu()
            }
        }

        timer_controller?.apply {
            cancel()
            timer_controller = null
        }
        task_controller?.apply {
            cancel()
            task_controller = null
        }

        destroyVideoTask()
    }

    private fun dismissControllerMenu() {
        viewBinding.apply {
            ivStatus.visibility = View.GONE
            topVideo.visibility = View.GONE
            bottomVideo.visibility = View.GONE
        }
    }

    private fun showControllerMenu() {
        initTimeTask()
        viewBinding.apply {
            ivStatus.visibility = View.VISIBLE
            ivStatus.setImageDrawable(context.getDrawable(if (isPausing) R.drawable.ic_stop else R.drawable.ic_play))
            topVideo.visibility = View.VISIBLE
            bottomVideo.visibility = View.VISIBLE
        }
    }

    //------------------------------------------------------------
    //  ## 对外提供的方法 ##
    //------------------------------------------------------------

    /**
     * 设置视频信息(首次播放调用)
     *
     * @param url   视频地址
     * @param title 视频标题
     */
    fun setDataSource(url: String, title: String) {
        //赋值
        videoPath = url
        videoTitle = title
//        setVideoThumbnail()
    }

    /**
     * 播放视频(第二次调用)
     */
    fun playVideo(url: String, title: String) {
        playVideo(url, title, 0)
    }

    /**
     *  播放视频（支持上次播放位置）
     *
     * 自己记录上一次播放的位置，然后传递position
     * @param videoPath 视频地址
     * @param videoTitle 视频标题
     * @param position  视频跳转的位置（毫秒）
     */
    fun playVideo(url: String, title: String, position: Int) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "播放地址错误", Toast.LENGTH_SHORT).show()
        }
        //赋值
        videoPath = url;
        videoTitle = title;
//        video_position = position;
//        isPrepare = false;
//        isPlaying = true;
        //重置MediaPlayer
        initViews()
        resetMediaPlayer()
    }

    private fun startVideo() {
        mediaPlayer?.let { mediaPlayer ->
            viewBinding.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_play))
            isPausing = false
            mediaPlayer.start()
        }
    }

    private fun pauseVideo() {
        mediaPlayer?.let { mediaPlayer ->
            viewBinding.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_stop))
            isPausing = true
            mediaPlayer.pause()
        }
    }

    //--------------------------------------------
    // Activity/Fragment中调用 getLifecycle().addObserver(frVideoPlayer);
    //--------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        pauseVideo()
    }

    //释放资源
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mediaPlayer?.let { mediaPlayer ->
            mediaPlayer.stop()
            mediaPlayer.release()// 释放资源
        }
        mediaPlayer = null
        surfaceHolder = null
//        video_position = 0;
//        unRegisterBatteryReceiver();
//        unregisterNetReceiver();
//        removeAllListener();
//        destroyTimeTask();
//        myHandler.removeCallbacksAndMessages(null);
    }
}