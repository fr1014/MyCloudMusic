package com.fr1014.mycoludmusic.home.dialogfragment.playlist;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentPlayListDialogBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.currentmusic.CurrentMusicDialogFragment;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;
import com.fr1014.mycoludmusic.utils.ScreenUtil;

import java.util.List;


public class PlayListDialogFragment extends DialogFragment {

    private FragmentPlayListDialogBinding binding;
    private PlayListAdapter playListAdapter;
    private MusicService.MusicControl musicControl;
    private int oldPosition = -1;  //当前播放音乐的位置

    public PlayListDialogFragment() {
        // Required empty public constructor

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.PayListDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置content前设定
        dialog.setCanceledOnTouchOutside(true);  //外部点击取消

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 修改布局的大小
     */
    @Override
    public void onStart() {
        super.onStart();
        resizeDialogFragment();
    }

    private void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.height = (2 * ScreenUtil.getScreenHeight(MyApplication.getInstance()) / 3);//获取屏幕的宽度，定义自己的宽度
                lp.width = (ScreenUtil.getScreenWidth(MyApplication.getInstance()));
                lp.y = 0;
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                window.setLayout(lp.width, lp.height);
            }

        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            initHeader();
            initAdapter();
            binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
            binding.rvPlaylist.setAdapter(playListAdapter);
            binding.rvPlaylist.scrollToPosition(oldPosition);
            musicControl.registerOnStateChangeListener(onStateChangeListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicControl.unregisterOnStateChangeListener(onStateChangeListener);
        }
    };

    private MusicService.OnStateChangeListener onStateChangeListener = new MusicService.OnStateChangeListener() {

        @Override
        public void onPlay(Music item) {
            Log.d(TAG, "++++onPlay: " + item.toString());
            int position = musicControl.getPlayList().indexOf(item);
            if (oldPosition != position && item.getSongUrl() != null) {
                playListAdapter.setCurrentMusic(item);
                playListAdapter.notifyDataSetChanged();
                oldPosition = position;
            }
        }

        @Override
        public void onPause() {

        }
    };

    private void initAdapter() {
        playListAdapter = new PlayListAdapter();
        List<Music> playList = musicControl.getPlayList();
        if (playList != null) {
            playListAdapter.setData(playList);
            oldPosition = playList.indexOf(musicControl.getCurrentMusic());
        }
        playListAdapter.setCurrentMusic(musicControl.getCurrentMusic());

        playListAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_playlist:
                    if (oldPosition != position) {
                        Music item = (Music) adapter.getData(position);
                        musicControl.addPlayList(item);
                    } else {
                        //点击的为当前播放的歌曲
                        new CurrentMusicDialogFragment().show(getParentFragmentManager(), "current_music_dialog");
                        dismissDialog();
                    }
                    break;
                case R.id.iv_del:
                    break;
            }
        });
    }

    private void initHeader() {
        int count = musicControl.getPlayList().size();
        binding.header.tvCount.setText(String.format("(%d)", count));
    }

    private static final String TAG = "PlayListDialogFragment";

    private void dismissDialog() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().unbindService(serviceConnection);
        }
    }
}