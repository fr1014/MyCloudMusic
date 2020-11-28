package com.fr1014.mycoludmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.ActivityMainBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.currentmusic.CurrentMusicDialogFragment;
import com.fr1014.mycoludmusic.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private TopListViewModel viewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private MusicService.MusicControl musicControl;
    private ActivityMainBinding binding;
    private SharedPreferences spMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(this, factory).get(TopListViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initSettings();

        initService();

        initClickListener();

        viewModel.getCheckSongResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCanPlay) {
                if (!isCanPlay) {
                    musicControl.playNext();
                }
            }
        });
    }

    private void initClickListener() {
        binding.appBarMain.contentMain.clBottomBar.setOnClickListener(this);
        binding.appBarMain.contentMain.ivStateStop.setOnClickListener(this);
        binding.appBarMain.contentMain.ivStatePlay.setOnClickListener(this);
        binding.appBarMain.contentMain.ivMusicMenu.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchSong();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;

            //注册监听器
            musicControl.registerOnStateChangeListener(onStateChangeListener);

            Music item = musicControl.getCurrentMusic();

            //首次绑定服务时若无音乐播放底部的设置音乐状态栏不可见
            if (item == null) {
                binding.appBarMain.contentMain.clBottomBar.setVisibility(View.GONE);
            }

            //设置播放模式，默认为循环播放
            int mode = spMode.getInt("play_mode", MusicService.TYPE_ORDER);
            musicControl.setPlayMode(mode);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接时，注销监听器
            musicControl.unregisterOnStateChangeListener(onStateChangeListener);
        }
    };

    //监听MusicService的变化
    private final MusicService.OnStateChangeListener onStateChangeListener = new MusicService.OnStateChangeListener() {
        @Override
        public void onPlay(Music item) {
            Log.d(TAG, "----onPlay: " + item.toString());
            //播放音乐时，若底部的音乐状态栏不可见，则设置为可见
            if (!TextUtils.isEmpty(item.getSongUrl())) {
                int visibility = binding.appBarMain.contentMain.clBottomBar.getVisibility();
                if (visibility == 8) {
                    binding.appBarMain.contentMain.clBottomBar.setVisibility(View.VISIBLE);
                }

                Glide.with(MainActivity.this)
                        .load(item.getImgUrl())
                        .placeholder(R.drawable.film)
                        .into(binding.appBarMain.contentMain.ivCoverImg);
                binding.appBarMain.contentMain.tvName.setText(item.getTitle());

                binding.appBarMain.contentMain.ivStatePlay.setVisibility(View.GONE);
                binding.appBarMain.contentMain.ivStateStop.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "++++onPlay: " + "main");
                viewModel.checkSong(item);
            }
        }

        @Override
        public void onPause() {
            binding.appBarMain.contentMain.ivStatePlay.setVisibility(View.VISIBLE);
            binding.appBarMain.contentMain.ivStateStop.setVisibility(View.GONE);
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state_play:
            case R.id.iv_state_stop:
                musicControl.playOrPause();
                break;
            case R.id.iv_music_menu:
                //弹出当前播放列表
                new PlayListDialogFragment().show(getSupportFragmentManager(), "playlist_dialog");
                break;
            case R.id.cl_bottom_bar:
                //当前播放的音乐的详情页
                new CurrentMusicDialogFragment().show(getSupportFragmentManager(), "current_music_dialog");
                break;
        }
    }

    private void searchSong() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void initSettings() {
        spMode = getSharedPreferences("settings", MODE_PRIVATE);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = spMode.edit();

        int mode = musicControl.getPlayMode();
        editor.putInt("play_mode", mode);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseAllRequests();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }
}