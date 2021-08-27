package com.fr1014.mycoludmusic.ui.playstatusbar

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.fr1014.mycoludmusic.databinding.FragmentPagerPlaystatusbarBinding
import com.fr1014.mycoludmusic.musicmanager.player.*
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicFragment
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.FileUtils
import com.fr1014.mycoludmusic.ui.playing.ReBoundActivity
import com.fr1014.mymvvm.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

private const val POSITION_MUSIC = "param"

class PlayStatusBarPagerFragment : BaseFragment<FragmentPagerPlaystatusbarBinding, CommonViewModel>() {
    private val COVER_FROM = "PlayStatusBarPagerFragment";
    private var music: Music? = null
    private var musicDialogFragment: CurrentPlayMusicFragment? = null
    private var isLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        arguments?.let {
            music = it.getParcelable<Music>(POSITION_MUSIC)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLoaded) {
            if (music != null) {
                val coverLocal = FileUtils.getCoverLocal(COVER_FROM, music)
                if (coverLocal != null) {
                    setBitmap(coverLocal)
                }
            }
        }
    }

    companion object {
        fun newInstance(music: Music?) = PlayStatusBarPagerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POSITION_MUSIC, music)
            }
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentPagerPlaystatusbarBinding {
        return FragmentPagerPlaystatusbarBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mViewBinding.clPlaylist.setOnClickListener {
            //当前播放的音乐的详情页
//            (context as BasePlayActivity<*, *>).showPlayingFragment()
            startActivity(ReBoundActivity::class.java)
//            (context as BasePlayActivity<*, *>).overridePendingTransition(R.anim.fragment_slide_enter, 0)
        }
        setData(music)
    }

    fun setData(music: Music?) {
        mViewBinding.apply {
            music?.let {
                if (tvName.text == it.title && tvAuthor.text == it.artist) return
                tvName.text = it.title
                tvAuthor.text = it.artist
            }
        }
    }

    private fun setBitmap(bitmap: Bitmap) {
        isLoaded = true
        mViewBinding.ivCoverImg.setImageBitmap(bitmap)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMusicCoverEvent(event: MusicCoverEvent) {
        if (event.from == COVER_FROM_COMMON || event.from == COVER_FROM) {
            when (event.type) {
                CoverStatusType.Loading -> {

                }
                CoverStatusType.Success -> {
                    val musicLoaded = event.music
                    musicLoaded?.let {
                        if (it.isSameMusic(this.music)) {
                            event.coverLocal?.let { bitmap -> setBitmap(bitmap) }
                        }
                    }
                }
                CoverStatusType.Fail -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}

class PlayStatusBarPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private var musicList: ArrayList<Music> = ArrayList()

    private val createIds: HashSet<Long> = HashSet() //得用hashset防重
    private val pageFragments: MutableMap<Int, Fragment> = HashMap()

    fun getPagerFragment(position: Int): PlayStatusBarPagerFragment? {
        return if (pageFragments.isEmpty() || pageFragments[position] == null) null else pageFragments[position] as PlayStatusBarPagerFragment
    }

    private fun manageFragments(fragment: Fragment, position: Int) {
        pageFragments[position] = fragment
    }

    override fun getItemId(position: Int): Long {
        val music = musicList[position]
        if (music.sourceType == MusicSource.KW_MUSIC.sourceType) {
            return music.getKWMusicId().toLong()
        }
        return music.id.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return musicList.any {
            if (it.sourceType == MusicSource.KW_MUSIC.sourceType) {
                it.getKWMusicId() === itemId.toString()
            } else {
                it.id === itemId.toString()
            }
        }
    }

    fun setMusicList(musics: List<Music>) {
        if (CollectionUtils.isEmptyList(musics)) return
        val callback = PagerDiffUtil(musicList, musics)
        val diff = DiffUtil.calculateDiff(callback)
        musicList.clear()
        musicList.addAll(musics)
        diff.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val fragment = getPagerFragment(position)
            // safe check ,but fragment should not be null
            if (fragment != null) {
                fragment.setData(musicList[position])
            } else {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = musicList.size

    override fun createFragment(position: Int): Fragment {
        val playStatusBarPagerFragment = PlayStatusBarPagerFragment.newInstance(musicList[position])
        createIds.add(position.toLong())
        manageFragments(playStatusBarPagerFragment, position)
        return playStatusBarPagerFragment
    }
}

class PagerDiffUtil(private val oldList: List<Music>, private val newList: List<Music>) : DiffUtil.Callback() {

    enum class PayloadKey {
        VALUE
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title && oldList[oldItemPosition].artist == newList[newItemPosition].artist
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        return listOf(PayloadKey.VALUE)
    }
}