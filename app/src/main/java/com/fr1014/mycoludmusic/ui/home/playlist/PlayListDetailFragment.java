package com.fr1014.mycoludmusic.ui.home.playlist;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDetailAdapter;
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus;
import com.fr1014.mycoludmusic.utils.PaletteBgUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mymvvm.base.BaseFragment;

//歌单详情页面
public class PlayListDetailFragment extends BaseFragment<FragmentPlaylistDetailBinding, PlayListViewModel> {
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COVER = "COVER";
    private long id = 0L;
    private String name;
    private String cover;
    private PlayListDetailAdapter adapter;

    public static Bundle createBundle(long id, String name, String coverImg) {
        Bundle bundle = new Bundle();
        bundle.putLong(PlayListDetailFragment.KEY_ID, id);
        bundle.putString(PlayListDetailFragment.KEY_NAME, name);
        bundle.putString(PlayListDetailFragment.KEY_COVER, coverImg);
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
        mViewBinding.toolbar.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        mViewBinding.name.setText(name);
        mViewBinding.playAll.llPlaylist.setVisibility(View.GONE);

        Glide.with(this)
                .asBitmap()
                .load(cover + "?param=300y300")
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
        mViewBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        mViewBinding.rvPlaylistDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = recyclerView.computeVerticalScrollOffset();
                Log.d("hello", "onScrolled: "+offset);
                if (offset > ScreenUtils.dp2px(198f)){
                    mViewBinding.playAll.llPlaylist.setVisibility(View.VISIBLE);
                }else {
                    mViewBinding.playAll.llPlaylist.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter(mViewModel, getViewLifecycleOwner());
        mViewBinding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        mViewBinding.rvPlaylistDetail.setAdapter(adapter);

        adapter.setOnPlayAllClick(new PlayListDetailAdapter.OnPlayAllClickListener() {
            @Override
            public void clickPlayAll() {
                AudioPlayer.get().addAndPlay(adapter.getCurrentList());
            }
        });
    }

    @Override
    public void initViewObservable() {

        mViewModel.getPlayListDetail(id).observe(getViewLifecycleOwner(), new Observer<Long[]>() {
            @Override
            public void onChanged(Long[] ids) {
                initAdapter();
                initHeaderView(ids.length);
                mViewModel.getPlayList(ids).observe(getViewLifecycleOwner(), new Observer<PagedList<Music>>() {
                    @Override
                    public void onChanged(PagedList<Music> musics) {
                        adapter.submitList(musics);
                    }
                });

                mViewModel.networkStatus.observe(getViewLifecycleOwner(), new Observer<NetworkStatus>() {
                    @Override
                    public void onChanged(NetworkStatus networkStatus) {
                        adapter.updateNetworkStatus(networkStatus);
                        if (networkStatus == NetworkStatus.COMPLETED) {
                            if (mViewModel.getNeedScrollToTop()) {
                                mViewBinding.rvPlaylistDetail.scrollToPosition(0);
                                mViewModel.setNeedScrollToTop(false);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initHeaderView(int length) {
        adapter.setPlayListCount(length);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}