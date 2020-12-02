package com.fr1014.mycoludmusic.home.toplist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;

import java.util.List;


//排行榜详情页面
public class PlayListDetailFragment extends Fragment implements OnPlayerEventListener {
    public static final String KEY_ID = "ID";
    private long id = 0L;
    private TopListViewModel viewModel;
    private PlayListDetailAdapter adapter;
    private FragmentPlaylistDetailBinding binding;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViewModel();
        // Inflate the layout for this fragment
        binding = FragmentPlaylistDetailBinding.inflate(getLayoutInflater(), container, false);
        binding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        initAdapter();
        binding.rvPlaylistDetail.setAdapter(adapter);
        return binding.getRoot();
    }

    private void initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(getActivity(), factory).get(TopListViewModel.class);
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter();
        View header = getLayoutInflater().inflate(R.layout.item_playlist_detail_header, binding.getRoot(), false);
        adapter.setHeaderView(header);
        header.setOnClickListener(v -> {
            List<Music> datas = adapter.getDatas();
            if (datas.size() >= 1) {
                AudioPlayer.get().addAndPlay(datas);
                viewModel.checkSong(datas.get(0));
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getData(position);
            if (TextUtils.isEmpty(music.getSongUrl())) {
                viewModel.checkSong(music);
            } else {
                AudioPlayer.get().addAndPlay(music);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AudioPlayer.get().addOnPlayEventListener(this);

        viewModel.getPlayListDetail(id).observe(PlayListDetailFragment.this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> musics) {
                adapter.setData(musics);
            }
        });
    }


    @Override
    public void onChange(Music music) {

    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayer.get().removeOnPlayEventListener(this);
    }
}