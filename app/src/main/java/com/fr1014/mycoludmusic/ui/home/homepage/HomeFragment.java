package com.fr1014.mycoludmusic.ui.home.homepage;

import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.dataconvter.CommonPlaylist;
import com.fr1014.mycoludmusic.databinding.FragmentHomeBinding;
import com.fr1014.mycoludmusic.ui.home.HomeTopListPagerAdapter;
import com.fr1014.mycoludmusic.ui.login.LoginActivity;
import com.fr1014.mycoludmusic.utils.ScreenUtil;
import com.fr1014.mymvvm.base.BaseFragment;

import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel> {

    @Override
    protected FragmentHomeBinding getViewBinding(ViewGroup container) {
        return FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected HomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(getActivity(),factory).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        initRecommendPlayList();
        initPagerTopList();
        initNetizensPlaylist();
        initListener();
    }

    private void initPagerTopList() {
        HomeTopListPagerAdapter pagerAdapter = new HomeTopListPagerAdapter(getActivity());
        //设置滚动方向
        mViewBinding.pagerTopList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //一屏多页
        View recyclerView = mViewBinding.pagerTopList.getChildAt(0);
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setPadding(0, 0, ScreenUtil.dp2px(getContext(),40), 0);
            ((RecyclerView) recyclerView).setClipToPadding(false);
        }
        mViewBinding.pagerTopList.setAdapter(pagerAdapter);
    }

    private void initNetizensPlaylist() {
        mViewBinding.blockNetizensPlaylist.setTitle("网友精选碟");
    }

    private void initRecommendPlayList() {
        mViewBinding.blockRecommend.setTitle("推荐歌单");
    }

    @Override
    public void initData() {
        mViewModel.getWYRecommendList(30);
        mViewModel.getWYNetizensPlayList("new","全部",10,0);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getRecommendListLiveData().observe(this, new Observer<List<CommonPlaylist>>() {
            @Override
            public void onChanged(List<CommonPlaylist> commonPlaylists) {
                mViewBinding.blockRecommend.bindData(commonPlaylists);
            }
        });
        mViewModel.getNetizensPlaylistLiveData().observe(this, new Observer<List<CommonPlaylist>>() {
            @Override
            public void onChanged(List<CommonPlaylist> commonPlaylists) {
                mViewBinding.blockNetizensPlaylist.bindData(commonPlaylists);
            }
        });
    }

    private void initListener() {
        mViewBinding.btToplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TopListActivity.class);
//                startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.topListFragment);
            }
        });

        mViewBinding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
//                Profile userProfile = Preferences.getUserProfile();
//                if (userProfile != null){
//                    Log.d("hello", "onClick: "+userProfile.toString());
//                }
            }
        });

        mViewBinding.btLikeList.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.userInfoFragment);
        });
    }

}