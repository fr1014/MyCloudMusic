package com.fr1014.mycoludmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.customview.PlayStatusBarView;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.databinding.ActivityMainBinding;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;
import com.fr1014.mycoludmusic.utils.CommonUtil;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BasePlayActivity<ActivityMainBinding> implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private TopListViewModel viewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private MusicService.MusicControl musicControl;
    private SharedPreferences spMode;
    private PlayStatusBarView statusBar;
    private Observer<Music> musicObserver;

    @Override
    protected void initView() {
        setSupportActionBar(mViewBinding.appBarMain.toolbar);

        statusBar = new PlayStatusBarView(this,getSupportFragmentManager());
        mViewBinding.appBarMain.contentMain.llPlaystatus.addView(statusBar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(mViewBinding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mViewBinding.navView, navController);

//        initClickListener();
    }

    @Override
    protected void initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(this, factory).get(TopListViewModel.class);
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        initService();
        initSettings();

        viewModel.getCheckSongResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCanPlay) {
                if (!isCanPlay) {
                    musicControl.playNext();
                }
            }
        });

        musicObserver = music -> musicControl.addPlayList(music);
        viewModel.getSongUrl().observeForever(musicObserver);
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
                startActivity(SearchActivity.class);
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

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            statusBar.setMusicControl(musicControl);
            //注册监听器
            musicControl.registerOnStateChangeListener(onStateChangeListener);

            viewModel.getMusicLocal().observe(MainActivity.this, new Observer<List<MusicEntity>>() {
                @Override
                public void onChanged(List<MusicEntity> musicEntities) {
                    if (!CommonUtil.isEmptyList(musicEntities)) {
                        List<Music> musicList = new ArrayList<>();
                        for (MusicEntity musicEntity : musicEntities) {
                            musicList.add(new Music(musicEntity.getId(),musicEntity.getArtist(),musicEntity.getTitle(),"",musicEntity.getImgUrl(), musicEntity.getMusicRid()));
                        }
                        Collections.reverse(musicList);
                        musicControl.addPlayList(musicList);
                        statusBar.setVisibility(View.VISIBLE);
                    } else {
                        statusBar.setVisibility(View.GONE);
                    }

                }
            });
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
                statusBar.setMusic(item);
                statusBar.setPlayStatus(View.GONE);
                statusBar.setStopStatus(View.VISIBLE);

                viewModel.getItemLocal(item.getTitle(),item.getArtist()).observe(MainActivity.this, new Observer<MusicEntity>() {
                    @Override
                    public void onChanged(MusicEntity musicEntity) {
                        if (musicEntity == null) {
                            viewModel.saveMusicLocal(item);
                        }
                    }
                });
            } else {
                if (!TextUtils.isEmpty(item.getMUSICRID())){  //酷我的歌
                    viewModel.getSongUrl(item);
                }else if (item.getId() != 0){           //网易的歌
                    viewModel.checkSong(item);
                }
            }
        }

        @Override
        public void onPause() {
            statusBar.setPlayStatus(View.VISIBLE);
            statusBar.setStopStatus(View.GONE);
        }

    };

    @Override
    public void onClick(View v) {
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
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        viewModel.getSongUrl().removeObserver(musicObserver);
    }
}