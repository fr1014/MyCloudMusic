package com.fr1014.mycoludmusic.ui.home.playlist

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDetailAdapter
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDetailAdapter.OnPlayAllClickListener
import com.fr1014.mycoludmusic.ui.mv.MVActivity
import com.fr1014.mycoludmusic.ui.paging.FooterAdapter
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.PaletteBgUtils.Companion.paletteDownBg
import com.fr1014.mycoludmusic.utils.PaletteBgUtils.Companion.paletteTopBg
import com.fr1014.mycoludmusic.utils.ScreenUtils
import com.fr1014.mycoludmusic.utils.StatusBarUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import com.fr1014.mymvvm.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//歌单详情页面
class PlayListDetailFragment : BaseFragment<FragmentPlaylistDetailBinding, PlayListViewModel>() {
    private var id = 0L
    private var pName: String? = null
    private var cover: String? = null
    private lateinit var pAdapter: PlayListDetailAdapter
    private var headerHeight = 0
    private var isShowPlayListName = false
    private var showDialogInfo = true

    override fun initParam() {
        arguments?.apply {
            id = getLong(KEY_ID)
            pName = getString(KEY_NAME)
            cover = getString(KEY_COVER)
            showDialogInfo = getBoolean(KEY_SHOW_DIALOG_INFO, true)
        }
    }

    override fun getViewBinding(container: ViewGroup): FragmentPlaylistDetailBinding {
        return FragmentPlaylistDetailBinding.inflate(layoutInflater, container, false)
    }

    override fun initViewModel(): PlayListViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(PlayListViewModel::class.java)
    }

    override fun initView() {
        mViewBinding.apply {
            toolbar.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0)
            name.text = "歌单"
            playAll.llPlaylist.visibility = View.INVISIBLE
        }

        initAdapter()

        GlideApp.with(this)
                .asBitmap()
                .load(cover)
                .error(R.drawable.ic_placeholder)
                .addListener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap?>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any, target: Target<Bitmap?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        paletteTopBg(mViewBinding.ivTitle, resource)
                        mViewModel.getCoverBitmap().postValue(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

        initListener()
    }

    private fun initListener() {
//        mViewBinding.ivBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { v -> Navigation.findNavController(v).popBackStack() }
            toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_search) {
                    CommonUtils.toastShort(getString(R.string.dev))
                }
                true
            }
            playAll.llPlaylist.setOnClickListener {
                MyAudioPlay.get().initPlayList(pAdapter.snapshot().items).play()
            }

            rvPlaylistDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val scrollOffset = scrollY()
                    if (scrollOffset > ScreenUtils.dp2px(64f)) {
                        if (!isShowPlayListName) {
                            name.isFocusable = true
                            name.text = pName
                            isShowPlayListName = true
                        }
                    } else {
                        name.isFocusable = false
                        name.text = "歌单"
                        isShowPlayListName = false
                    }
                    val bitmap = mViewModel!!.getCoverBitmap().value
                    if (bitmap != null) {
                        if (scrollOffset > ScreenUtils.dp2px(89f)) {
                            paletteDownBg(ivTitle, bitmap)
                        } else {
                            paletteTopBg(ivTitle, bitmap)
                        }
                    }
                    playAll.llPlaylist.visibility = if (scrollOffset > ScreenUtils.dp2px(204f)) View.VISIBLE else View.GONE
                }
            })
        }
    }

    // 获取header
    // 获取第一个可见item的高度
    private fun scrollY(): Int {
        val layoutManager = mViewBinding.rvPlaylistDetail.layoutManager as LinearLayoutManager
        // 获取第一个可见item的位置
        val position = layoutManager.findFirstVisibleItemPosition()
        if (position == 0) {
            // 获取header
            val headerView = layoutManager.findViewByPosition(0)
            // 获取第一个可见item的高度
            headerHeight = headerView!!.height
        }

        // 获取第一个可见item
        val firstVisiableChildView = layoutManager.findViewByPosition(position)
        // 获取第一个可见item的高度
        val itemHeight = firstVisiableChildView!!.height
        // 获取第一个可见item的位置
        return if (position == 0) {
            position * itemHeight - firstVisiableChildView.top
        } else {
            position * itemHeight - firstVisiableChildView.top + headerHeight
        }
    }

    private fun initAdapter() {

        pAdapter = PlayListDetailAdapter(mViewModel, viewLifecycleOwner, showDialogInfo, requireActivity().supportFragmentManager)

        mViewBinding.apply {
            rvPlaylistDetail.layoutManager = LinearLayoutManager(context)
            rvPlaylistDetail.adapter = pAdapter.withLoadStateFooter(FooterAdapter { pAdapter.retry() })

            pAdapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        includeLoadingView.llLoading.visibility = View.INVISIBLE
                    }
                    is LoadState.Loading -> {
                        includeLoadingView.llLoading.visibility = View.VISIBLE
                    }
                    is LoadState.Error -> {
                        val state = it.refresh as LoadState.Error
                        includeLoadingView.llLoading.visibility = View.INVISIBLE
                        Toast.makeText(context, "Load Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        pAdapter.setOnPlayAllClick(object : OnPlayAllClickListener {
            override fun clickPlayAll() {
                MyAudioPlay.get().initPlayList(pAdapter.snapshot().items).play()
            }
        })
    }

    override fun initViewObservable() {

        mViewModel.apply {

            playListDetailInfo.observe(viewLifecycleOwner, { playListDetailEntity -> pAdapter.setHeadInfo(playListDetailEntity) })

            getPlayListDetail(id).observe(viewLifecycleOwner, { ids: Array<Long> ->
                initHeaderView(ids.size)

                viewLifecycleOwner.lifecycleScope.launch {
                    getPlayList(ids).collectLatest {
                        pAdapter.submitData(it)
                    }
                }
                getWYUserPlayList()//获取用户收藏的歌单
            })

            playlistWYInfo.observe(viewLifecycleOwner, { playlists ->
                var type = 1
                for (playlist in playlists) {
                    if (playlist.id == id) {
                        type = 2
                    }
                }
                collectPlayListType = type
                pAdapter.setHeadInfo(type)
            })
            getCollectPlayList().observe(viewLifecycleOwner, { (code, point, msg) -> pAdapter.setHeadInfo(collectPlayListType) })

            mvMusic.observe(viewLifecycleOwner, {
                context?.let { context -> MVActivity.startMVActivity(context, it.mvUrl, it.title, true) }
                MyAudioPlay.get().pausePlayer()
            })
        }
    }

    private fun initHeaderView(length: Int) {
        pAdapter.setPlayListCount(length)
        mViewBinding!!.playAll.tvCount.text = length.toString()
    }

    companion object {
        const val KEY_ID = "ID"
        const val KEY_NAME = "NAME"
        const val KEY_COVER = "COVER"
        const val KEY_SHOW_DIALOG_INFO = "SHOW_DIALOG_INFO"

        fun createBundle(id: Long, name: String?, coverImg: String?): Bundle {
            val bundle = Bundle()
            bundle.putLong(KEY_ID, id)
            bundle.putString(KEY_NAME, name)
            bundle.putString(KEY_COVER, coverImg)
            return bundle
        }

        fun createBundle(id: Long, name: String?, coverImg: String?, showDialogInfo: Boolean): Bundle {
            val bundle = Bundle()
            bundle.putLong(KEY_ID, id)
            bundle.putString(KEY_NAME, name)
            bundle.putString(KEY_COVER, coverImg)
            bundle.putBoolean(KEY_SHOW_DIALOG_INFO, showDialogInfo)
            return bundle
        }
    }

    override fun onResume() {
        super.onResume()
        StatusBarUtils.setImmersiveStatusBar(activity?.window, false)
    }
}