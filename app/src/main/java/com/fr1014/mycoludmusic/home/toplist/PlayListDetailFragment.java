package com.fr1014.mycoludmusic.home.toplist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fr1014.musicmanager.Music;
import com.fr1014.musicmanager.MusicService;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentPlaylistDetailBinding;

import java.util.List;

public class PlayListDetailFragment extends Fragment {
    public static final String KEY_ID = "ID";
    private long id = 0L;
    private TopListViewModel viewModel;
    private PlayListDetailAdapter adapter;
    private MusicService.MusicControl musicControl;
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
            Log.d(TAG, "------onCreate: " + id);
        }
    }

    private static final String TAG = "PlayListDetailFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistDetailBinding.inflate(getLayoutInflater(), container, false);

        initViewModel();

        binding.rvPlaylistDetail.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        initAdapter();
        binding.rvPlaylistDetail.setAdapter(adapter);

        viewModel.getSongUrl().observe(getViewLifecycleOwner(), new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                musicControl.addPlayList(music);
            }
        });

        viewModel.getPlayListDetail(id).observe(getViewLifecycleOwner(), new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> musics) {
                adapter.setData(musics);
            }
        });
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
            musicControl.addPlayList(datas);
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getData(position);
            viewModel.getSongUrlEntity(music);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        //绑定成功时
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;

        }

        //断开连接时
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}