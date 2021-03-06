package com.fr1014.mycoludmusic.ui.home.homepage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.dataconvter.CommonPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.Block;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.Creative;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.databinding.FragmentHomeBinding;
import com.fr1014.mycoludmusic.utils.CollectionUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    @Override
    protected FragmentHomeBinding getViewBinding(ViewGroup container) {
        return FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected HomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(getActivity(), factory).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        initPagerTopList();
        initListener();
    }

    private void initPagerTopList() {
        HomeTopListPagerAdapter pagerAdapter = new HomeTopListPagerAdapter(getActivity());
        //设置滚动方向
        mViewBinding.pagerTopList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //一屏多页
        View recyclerView = mViewBinding.pagerTopList.getChildAt(0);
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setPadding(0, 0, ScreenUtils.dp2px(40), 0);
            ((RecyclerView) recyclerView).setClipToPadding(false);
        }
        mViewBinding.pagerTopList.setAdapter(pagerAdapter);
    }

    @Override
    public void initData() {
//        mViewModel.getWYRecommendList(30);
//        mViewModel.getWYNetizensPlayList("new","全部",10,0);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getHomeBlockLiveData().observe(this, new Observer<HomeBlock>() {
            @Override
            public void onChanged(HomeBlock homeBlock) {
                List<Block> blocks = homeBlock.getData().component2();
                for (Block block : blocks) {
                    List<Creative> results = block.component5();
                    List<CommonPlaylist> commonPlaylists = new ArrayList<>();
                    if (CollectionUtils.isEmptyList(results)) return;
                    for (int index = 0; index < results.size() - 1; index++) {
                        Creative creative = results.get(index);
                        if (creative != null) {
                            try {
                                commonPlaylists.add(new CommonPlaylist(Long.parseLong(creative.getCreativeId()), creative.getUiElement().getMainTitle().getTitle(), creative.getUiElement().getImage().getImageUrl()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (TextUtils.equals(block.getBlockCode(), "HOMEPAGE_BLOCK_PLAYLIST_RCMD")) { //推荐歌单
                        mViewBinding.blockRecommend.setVisibility(View.VISIBLE);
                        mViewBinding.blockRecommend.showLoadingView(false);
                        mViewBinding.blockRecommend.setTitle(block.getUiElement().getSubTitle().getTitle());
//                        mViewBinding.blockRecommend.setTvButton(block.getUiElement().getButton().getText());
                        mViewBinding.blockRecommend.bindData(commonPlaylists);
                    } else if (TextUtils.equals(block.getBlockCode(), "HOMEPAGE_BLOCK_OFFICIAL_PLAYLIST")) { //专属场景歌单
                        mViewBinding.blockNetizensPlaylist.setVisibility(View.VISIBLE);
                        mViewBinding.blockNetizensPlaylist.showLoadingView(false);
                        mViewBinding.blockNetizensPlaylist.setTitle(block.getUiElement().getSubTitle().getTitle());
//                        mViewBinding.blockNetizensPlaylist.setTitle(block.getUiElement().getButton().getText());
                        mViewBinding.blockNetizensPlaylist.bindData(commonPlaylists);
                    } else if (TextUtils.equals(block.getBlockCode(), "HOMEPAGE_BLOCK_MGC_PLAYLIST")) { //音乐雷达
                        mViewBinding.blockMusicRadar.setVisibility(View.VISIBLE);
                        mViewBinding.blockMusicRadar.showLoadingView(false);
                        mViewBinding.blockMusicRadar.setTitle(block.getUiElement().getSubTitle().getTitle());
                        mViewBinding.blockMusicRadar.bindData(commonPlaylists);
                    }
//                    else if (TextUtils.equals(block.getBlockCode(),"HOMEPAGE_VOICELIST_RCMD")){ //播客合集
//                        mViewBinding.blockVoiceList.setTitle(block.getUiElement().getSubTitle().getTitle());
//                        mViewBinding.blockVoiceList.bindData(commonPlaylists);
//                    }else if(TextUtils.equals(block.getBlockCode(),"HOMEPAGE_BLOCK_VIDEO_PLAYLIST")){ //视频合集
//                        mViewBinding.blockVideoList.setTitle(block.getUiElement().getSubTitle().getTitle());
//                        mViewBinding.blockVideoList.bindData(commonPlaylists);
//                    }
                }
            }
        });
//        mViewModel.getRecommendListLiveData().observe(this, new Observer<List<CommonPlaylist>>() {
//            @Override
//            public void onChanged(List<CommonPlaylist> commonPlaylists) {
//                mViewBinding.blockRecommend.bindData(commonPlaylists);
//            }
//        });
//        mViewModel.getNetizensPlaylistLiveData().observe(this, new Observer<List<CommonPlaylist>>() {
//            @Override
//            public void onChanged(List<CommonPlaylist> commonPlaylists) {
//                mViewBinding.blockNetizensPlaylist.bindData(commonPlaylists);
//            }
//        });
    }

    private void initListener() {
        if (getView() != null) {
            Navigation.findNavController(getView()).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    if (destination.getId() == R.id.nav_home || destination.getId() == R.id.topListFragment || destination.getId() == R.id.userInfoFragment) {
                        if (getActivity() != null) {
                            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), true);
                        }
                    } else {
                        if (getActivity() != null) {
                            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
                        }
                    }
                }
            });
        }

        mViewBinding.tvBtMore.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_topListFragment);
        });
    }

}