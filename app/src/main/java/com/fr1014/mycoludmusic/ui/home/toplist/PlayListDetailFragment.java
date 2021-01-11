package com.fr1014.mycoludmusic.ui.home.toplist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionInflater;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

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
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.utils.ScreenUtil;
import com.fr1014.mymvvm.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


//歌单详情页面
public class PlayListDetailFragment extends BaseFragment<FragmentPlaylistDetailBinding, TopListViewModel>{
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

//    public static PlayListDetailFragment newInstance(long id) {
//        PlayListDetailFragment fragment = new PlayListDetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putLong(KEY_ID, id);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

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
    protected TopListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(getActivity(), factory).get(TopListViewModel.class);
    }

    @Override
    protected void initView() {
        ViewCompat.setTransitionName(mViewBinding.ivCover, name);
        mViewBinding.toolbar.setPadding(0, ScreenUtil.getStatusHeight(MyApplication.getInstance()), 0, 0);
        mViewBinding.name.setText(name);
        mViewBinding.llLoading.lavLoading.setAnimation(R.raw.loading_music);

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
        mViewBinding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        initAdapter();
        mViewBinding.rvPlaylistDetail.setAdapter(adapter);

        mViewBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter();
        adapter.setDisplayMarginView(true);
        View header = getLayoutInflater().inflate(R.layout.item_playlist_detail_header, mViewBinding.getRoot(), false);
        adapter.setHeaderView(header);

        header.setOnClickListener(v -> {
            List<Music> datas = new ArrayList<>(adapter.getDatas());
            if (datas.size() >= 1) {
//                AudioPlayer.get().addAndPlay(datas);
//                viewModel.checkSong(datas.get(0));
                mViewModel.getSongListUrl(datas).observe(getViewLifecycleOwner(), new Observer<List<Music>>() {
                    @Override
                    public void onChanged(List<Music> musicList) {
                        AudioPlayer.get().addAndPlay(musicList);
                    }
                });
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getData(position);
            if (TextUtils.isEmpty(music.getSongUrl())) {
                mViewModel.checkSong(music);
            } else {
                AudioPlayer.get().addAndPlay(music);
            }
        });
    }

    @Override
    public void initViewObservable() {
        mViewModel.getCheckSongResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCanPlay) {
                if (!isCanPlay) {
                    AudioPlayer.get().playNext();
                }
            }
        });

        mViewModel.getSongUrl().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                if (!TextUtils.isEmpty(music.getSongUrl())) {
                    AudioPlayer.get().addAndPlay(music);
                } else {
                    AudioPlayer.get().playNext();
                }
            }
        });

        mViewModel.getStartPlayListDetail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isStart) {
                if (isStart){
                    adapter.getHeaderView().setVisibility(View.INVISIBLE);
                    mViewBinding.llLoading.llLoading.setVisibility(View.VISIBLE);
                    mViewBinding.llLoading.lavLoading.playAnimation();
                    mViewBinding.llLoading.lavLoading.loop(true);
                }
            }
        });

        mViewModel.getPlayListDetail(id).observe(PlayListDetailFragment.this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> musics) {
                adapter.getHeaderView().setVisibility(View.VISIBLE);
                mViewBinding.llLoading.lavLoading.cancelAnimation();
                mViewBinding.llLoading.llLoading.setVisibility(View.GONE);
                adapter.setData(musics);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}