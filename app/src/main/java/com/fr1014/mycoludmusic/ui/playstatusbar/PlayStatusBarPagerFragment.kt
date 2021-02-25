package com.fr1014.mycoludmusic.ui.playstatusbar

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.mycoludmusic.base.BasePlayActivity
import com.fr1014.mycoludmusic.databinding.FragmentPagerPlaystatusbarBinding
import com.fr1014.mycoludmusic.listener.LoadResultListener
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicFragment
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.CoverLoadUtils
import com.fr1014.mycoludmusic.utils.FileUtils
import com.fr1014.mycoludmusic.utils.MusicUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import com.fr1014.mymvvm.base.BaseFragment
import java.util.*
import kotlin.collections.ArrayList

private const val POSITION_MUSIC = "param"

class PlayStatusBarPagerFragment : BaseFragment<FragmentPagerPlaystatusbarBinding, CommonViewModel>(), LoadResultListener {
    var music: Music? = null
    private var musicDialogFragment: CurrentPlayMusicFragment? = null
    private var isLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            music = it.getParcelable<Music>(POSITION_MUSIC)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLoaded){
            if (music != null) {
                val coverLocal = FileUtils.getCoverLocal(music)
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
            if (musicDialogFragment == null) {
                musicDialogFragment = CurrentPlayMusicFragment()
            }
            if (!musicDialogFragment!!.isAdded) {
                //当前播放的音乐的详情页
                (context as BasePlayActivity<*, *>).showPlayingFragment()
            }
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

    private fun setBitmap(bitmap: Bitmap){
        isLoaded = true
        mViewBinding.ivCoverImg.setImageBitmap(bitmap)
    }

    fun loadImgUrl(music: Music) {
        if (isLoaded) return
        if (this.music?.title.equals(music.title) && this.music?.artist.equals(music.artist)) {
            GlideApp.with(this@PlayStatusBarPagerFragment)
                    .load(music.imgUrl + "?param=90y90")
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            isLoaded = true
                            mViewBinding.ivCoverImg.setImageDrawable(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun coverLoading() {

    }

    override fun coverLoadSuccess(music: Music, coverLocal: Bitmap) {
        if (isLoaded) return
        this.music?.let{
            if (MusicUtils.isSameMusic(it,music)) {
                setBitmap(coverLocal)
            }
        }
    }

    override fun coverLoadFail() {

    }

    override fun onDestroyView() {
        Log.d("hello", "onDestroyView: ")
        CoverLoadUtils.get().removeLoadListener(this)
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
        if (music.id == 0L){
            return music.musicrid.getRid()
        }
        return music.id
    }

    override fun containsItem(itemId: Long): Boolean {
        return musicList.any {
            it.id == itemId || if (TextUtils.isEmpty(it.musicrid)) true else it.musicrid.getRid() == itemId
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
        CoverLoadUtils.get().registerLoadListener(playStatusBarPagerFragment)
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

fun String.getRid() = this.substring(6, length).toLong()