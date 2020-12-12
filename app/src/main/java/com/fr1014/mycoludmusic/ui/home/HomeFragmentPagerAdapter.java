package com.fr1014.mycoludmusic.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fr1014.mycoludmusic.ui.gallery.GalleryFragment;

import java.util.List;

/**
 * 创建时间:2020/9/8
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private int numPages = 3;

    public HomeFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior,List<Fragment> fragments){
        this(fm, behavior);
        this.fragments = fragments;
    }

    public HomeFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        return fragments.get(position);
        return new GalleryFragment();
    }

    @Override
    public int getCount() {
//        return fragments.size();
        return 3;
    }
}
