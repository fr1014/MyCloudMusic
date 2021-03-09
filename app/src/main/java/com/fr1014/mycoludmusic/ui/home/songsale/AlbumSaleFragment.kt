package com.fr1014.mycoludmusic.ui.home.songsale

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentSaleContainerBinding
import com.fr1014.mymvvm.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

const val TYPE_FRAGMENT = "type_fragment"
const val TYPE_ALBUM = 0  //数字专辑榜
const val TYPE_SINGLE = 1 //数字单曲榜

class AlbumSaleFragment : BaseFragment<FragmentSaleContainerBinding, SongSaleViewModel>() {

    val tabs = arrayOf("日榜", "周榜", "2021年榜", "总榜")
    var mType: Int = 0

    companion object {
        fun getInstance(type: Int): AlbumSaleFragment {
            val bundle = Bundle()
            bundle.putInt(TYPE_FRAGMENT, type)
            val albumSaleFragment = AlbumSaleFragment()
            albumSaleFragment.arguments = bundle
            return albumSaleFragment
        }
    }

    override fun initParam() {
        arguments?.apply {
            mType = getInt(TYPE_FRAGMENT)
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentSaleContainerBinding {
        return FragmentSaleContainerBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SongSaleViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SongSaleViewModel::class.java)
    }

    override fun initView() {
        mViewBinding.apply {
            pagerRank.apply {
                offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT  //禁用预加载
                adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                    override fun getItemCount() = tabs.size

                    override fun createFragment(position: Int): Fragment = DetailSaleFragment.getInstance(mType, position)
                }
            }
            TabLayoutMediator(tabLayout, pagerRank) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }

}