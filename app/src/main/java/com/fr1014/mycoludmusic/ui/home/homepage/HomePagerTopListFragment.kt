package com.fr1014.mycoludmusic.ui.home

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentHomePageTopListBinding
import com.fr1014.mycoludmusic.ui.home.homepage.HomeViewModel
import com.fr1014.mymvvm.base.BaseFragment
import java.util.*

private const val ARG_PARAM1 = "param1"
private val NUM_PAGES = 4
private val ids = longArrayOf(3778678,991319590,3779629,2884035)
class HomePagerTopListFragment : BaseFragment<FragmentHomePageTopListBinding, HomeViewModel>() {
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
                HomePagerTopListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                    }
                }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentHomePageTopListBinding {
        return FragmentHomePageTopListBinding.inflate(layoutInflater);
    }

    override fun initViewModel(): HomeViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this,factory).get(HomeViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        mViewModel.getPlayListDetailEntity(ids[position!!])
    }

    override fun initViewObservable() {
        mViewModel.playListDetail.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mViewBinding.topListBlock.setData(it)
        })
    }
}

class HomeTopListPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val pageFragments: MutableMap<Int, Fragment> = HashMap()

    private fun manageFragments(fragment: Fragment, position: Int) {
        pageFragments[position] = fragment
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        val homePagerTopListFragment = HomePagerTopListFragment.newInstance(position)
        manageFragments(homePagerTopListFragment, position)
        return homePagerTopListFragment
    }
}