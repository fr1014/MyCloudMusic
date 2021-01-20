package com.fr1014.mycoludmusic.ui.home.playlist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.ui.home.playlist.paging2.PlayListDetailAdapter;
import com.fr1014.mycoludmusic.ui.search.paging2.NetworkStatus;
import com.fr1014.mycoludmusic.utils.ScreenUtil;
import com.fr1014.mymvvm.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

//歌单详情页面
public class PlayListDetailFragment extends BaseFragment<FragmentPlaylistDetailBinding, PlayListViewModel>{
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COVER = "COVER";
    private long id = 0L;
    private String name;
    private String cover;
    private PlayListDetailAdapter adapter;

    public static Bundle createBundle(long id,String name,String coverImg){
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(
                TransitionInflater.from(getContext())
                        .inflateTransition(android.R.transition.move));
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
        ViewCompat.setTransitionName(mViewBinding.ivCover, name);
        mViewBinding.toolbar.setPadding(0, ScreenUtil.getStatusHeight(MyApplication.getInstance()), 0, 0);
        mViewBinding.name.setText(name);

        postponeEnterTransition();
        Glide.with(this)
                .load(cover)
                .error(R.drawable.bg_play)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mViewBinding.ivCover);

        initAdapter();

        mViewBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter(mViewModel);
        mViewBinding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        mViewBinding.rvPlaylistDetail.setAdapter(adapter);

        mViewBinding.playAll.getRoot().setOnClickListener(v -> {
            List<Music> datas = new ArrayList<>(adapter.getCurrentList());
            if (datas.size() >= 1) {
                // TODO: 2021/1/20 传入id播放全部歌曲
                AudioPlayer.get().addAndPlay(datas);
            }
        });
    }

    @Override
    public void initViewObservable() {

        mViewModel.getPlayListDetail(id).observe(getViewLifecycleOwner(), new Observer<Long[]>() {
            @Override
            public void onChanged(Long[] ids) {
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

                        }
                    }
                });
            }
        });
    }

    private void initHeaderView(int length) {
        mViewBinding.playAll.tvCount.setText(length+"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}