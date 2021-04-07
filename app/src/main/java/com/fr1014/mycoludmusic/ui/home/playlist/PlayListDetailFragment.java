package com.fr1014.mycoludmusic.ui.home.playlist;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.CollectPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist;
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDetailAdapter;
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus;
import com.fr1014.mycoludmusic.utils.CommonUtils;
import com.fr1014.mycoludmusic.utils.PaletteBgUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseFragment;

import java.util.List;

//歌单详情页面
public class PlayListDetailFragment extends BaseFragment<FragmentPlaylistDetailBinding, PlayListViewModel> {
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COVER = "COVER";
    public static final String KEY_SHOW_DIALOG_INFO = "SHOW_DIALOG_INFO";
    private long id = 0L;
    private String name;
    private String cover;
    private PlayListDetailAdapter adapter;
    private int headerHeight = 0;
    private boolean isShowPlayListName = false;
    private boolean showDialogInfo = true;

    public static Bundle createBundle(long id, String name, String coverImg) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, id);
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_COVER, coverImg);
        return bundle;
    }

    public static Bundle createBundle(long id, String name, String coverImg,boolean showDialogInfo) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, id);
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_COVER, coverImg);
        bundle.putBoolean(KEY_SHOW_DIALOG_INFO,showDialogInfo);
        return bundle;
    }

    public PlayListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void initParam() {
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_ID);
            name = getArguments().getString(KEY_NAME);
            cover = getArguments().getString(KEY_COVER);
            showDialogInfo = getArguments().getBoolean(KEY_SHOW_DIALOG_INFO,true);
        }
    }

    @Override
    protected FragmentPlaylistDetailBinding getViewBinding(ViewGroup container) {
        return FragmentPlaylistDetailBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected PlayListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(this, factory).get(PlayListViewModel.class);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
        mViewBinding.toolbar.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        mViewBinding.name.setText("歌单");
        mViewBinding.playAll.llPlaylist.setVisibility(View.INVISIBLE);

        Glide.with(this)
                .asBitmap()
                .load(cover)
                .error(R.drawable.ic_placeholder)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        PaletteBgUtils.Companion.paletteTopBg(mViewBinding.ivTitle, resource);
                        mViewModel.getCoverBitmap().postValue(resource);
                    }
                });

        initListener();
    }

    private void initListener() {
//        mViewBinding.ivBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        mViewBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search){
                    CommonUtils.toastShort(getString(R.string.dev));
                }
                return true;
            }
        });

        mViewBinding.playAll.llPlaylist.setOnClickListener(v -> AudioPlayer.get().addAndPlay(adapter.getCurrentList()));

        mViewBinding.rvPlaylistDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int scrollOffset = getScrollY();
                if (scrollOffset > ScreenUtils.dp2px(64f)) {
                    if (!isShowPlayListName) {
                        mViewBinding.name.setFocusable(true);
                        mViewBinding.name.setText(name);
                        isShowPlayListName = true;
                    }
                } else {
                    mViewBinding.name.setFocusable(false);
                    mViewBinding.name.setText("歌单");
                    isShowPlayListName = false;
                }
                Bitmap bitmap = mViewModel.getCoverBitmap().getValue();
                if (bitmap != null) {
                    if (scrollOffset > ScreenUtils.dp2px(89f)) {
                        PaletteBgUtils.Companion.paletteDownBg(mViewBinding.ivTitle, bitmap);
                    } else {
                        PaletteBgUtils.Companion.paletteTopBg(mViewBinding.ivTitle, bitmap);
                    }
                }
                mViewBinding.playAll.llPlaylist.setVisibility(scrollOffset > ScreenUtils.dp2px(204f) ? View.VISIBLE : View.GONE);
            }
        });
    }

    private int getScrollY() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mViewBinding.rvPlaylistDetail.getLayoutManager();
        // 获取第一个可见item的位置
        int position = layoutManager.findFirstVisibleItemPosition();

        if (position == 0) {
            // 获取header
            View headerView = layoutManager.findViewByPosition(0);
            // 获取第一个可见item的高度
            headerHeight = headerView.getHeight();
        }

        // 获取第一个可见item
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        // 获取第一个可见item的高度
        int itemHeight = firstVisiableChildView.getHeight();
        // 获取第一个可见item的位置
        int iResult;
        if (position == 0) {
            iResult = (position) * itemHeight - firstVisiableChildView.getTop();
        } else {
            iResult = (position) * itemHeight - firstVisiableChildView.getTop() + headerHeight;
        }

        return iResult;
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter(mViewModel, getViewLifecycleOwner(),showDialogInfo);
        mViewBinding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        mViewBinding.rvPlaylistDetail.setAdapter(adapter);

        adapter.setOnPlayAllClick(() -> AudioPlayer.get().addAndPlay(adapter.getCurrentList()));
    }

    @Override
    public void initViewObservable() {
        mViewBinding.loadingView.llLoading.setVisibility(View.VISIBLE);

        mViewModel.getPlayListDetail(id).observe(getViewLifecycleOwner(), ids -> {
            initAdapter();
            initHeaderView(ids.length);
            mViewModel.getPlayList(ids).observe(getViewLifecycleOwner(), musics -> adapter.submitList(musics));
            mViewModel.networkStatus.observe(getViewLifecycleOwner(), networkStatus -> {
                adapter.updateNetworkStatus(networkStatus);
                if (networkStatus == NetworkStatus.COMPLETED) {
                    if (mViewModel.getNeedScrollToTop()) {
                        mViewBinding.rvPlaylistDetail.scrollToPosition(0);
                        mViewModel.setNeedScrollToTop(false);
                        mViewBinding.rvPlaylistDetail.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getView() != null) {
                                    mViewBinding.loadingView.llLoading.setVisibility(View.GONE);
                                    mViewBinding.rvPlaylistDetail.setVisibility(View.VISIBLE);
                                    mViewBinding.rvPlaylistDetail.getChildAt(0).setVisibility(View.VISIBLE);
                                }
                            }
                        }, 800);
                    }
                    mViewModel.getWYUserPlayList(); //获取用户收藏的歌单
                }
            });
        });

        mViewModel.getPlayListDetailInfo().observe(getViewLifecycleOwner(), new Observer<PlayListDetailEntity>() {
            @Override
            public void onChanged(PlayListDetailEntity playListDetailEntity) {
                adapter.setHeadInfo(playListDetailEntity);
            }
        });

        mViewModel.getPlaylistWYLive().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                int type = 1;
                for (Playlist playlist : playlists){
                    if (playlist.getId() == id){
                        type = 2;
                    }
                }
                mViewModel.setCollectPlayListType(type);
                adapter.setHeadInfo(type);
            }
        });

        mViewModel.getCollectPlayList().observe(getViewLifecycleOwner(), new Observer<CollectPlaylist>() {
            @Override
            public void onChanged(CollectPlaylist collectPlaylist) {
                adapter.setHeadInfo(mViewModel.getCollectPlayListType());
            }
        });
    }

    private void initHeaderView(int length) {
        adapter.setPlayListCount(length);
        mViewBinding.playAll.tvCount.setText(length + "");
    }

}