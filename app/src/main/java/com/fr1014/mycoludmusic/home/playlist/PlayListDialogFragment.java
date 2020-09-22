package com.fr1014.mycoludmusic.home.playlist;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fr1014.musicmanager.Music;
import com.fr1014.musicmanager.MusicService;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentPlayListDialogBinding;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.utils.ScreenUtil;

import java.util.List;


public class PlayListDialogFragment extends DialogFragment {

    private FragmentPlayListDialogBinding binding;
    private PlayListAdapter playListAdapter;
    private TopListViewModel viewModel;
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
//        dialog.setContentView(R.layout.fragment_play_list_dialog);
        dialog.setCanceledOnTouchOutside(true);  //外部点击取消

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(getActivity(), factory).get(TopListViewModel.class);
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
        public void onPlayProgressChange(long played, long duration) {

        }

        @Override
        public void onPlay(Music item) {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onNotify(Music item, boolean canPlay) {

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
                        playListAdapter.setCurrentMusic(item);
//                        if (oldPosition != -1) {
//                            playListAdapter.notifyItemChanged(oldPosition);
//                        }
//                        playListAdapter.notifyItemChanged(position);
                        playListAdapter.notifyDataSetChanged();
                        oldPosition = position;
                    } else {
                        Toast.makeText(getContext(), "点击的为当前播放的音乐!!!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().unbindService(serviceConnection);
        }
    }
}