package com.fr1014.mycoludmusic;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.customview.PlayStatusBarView;
import com.fr1014.mycoludmusic.databinding.ActivityMainBinding;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.listener.MusicInfoListener;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BasePlayActivity<ActivityMainBinding> implements View.OnClickListener, MusicInfoListener {
    private static final String TAG = "MainActivity";

    private TopListViewModel viewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private PlayStatusBarView statusBar;
//    private Observer<Music> musicObserver;

    @Override
    protected void initView() {
        setSupportActionBar(mViewBinding.appBarMain.toolbar);

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

    //首次绑定Service时该方法被调用
    @Override
    protected void onServiceBound() {
        statusBar = new PlayStatusBarView(this, getSupportFragmentManager());
        AudioPlayer.get().addOnPlayEventListener(statusBar);
        mViewBinding.appBarMain.contentMain.flPlaystatus.addView(statusBar);
        AudioPlayer.get().addOnPlayEventListener(statusBar);
    }


    @Override
    protected void initData() {
        viewModel.getCheckSongResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCanPlay) {
                if (!isCanPlay) {
                    AudioPlayer.get().playNext();
                }
            }
        });

        viewModel.getKWSongUrl().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                if (!TextUtils.isEmpty(music.getSongUrl())) {
                    AudioPlayer.get().addAndPlay(music);
                } else {
                    AudioPlayer.get().playNext();
                }
            }
        });
//        musicObserver = new Observer<Music>() {
//            @Override
//            public void onChanged(Music music) {
//                if (!TextUtils.isEmpty(music.getSongUrl())){
//                    AudioPlayer.get().addAndPlay(music);
//                }else{
//                    AudioPlayer.get().playNext();
//                }
//            }
//        };
//        viewModel.getSongUrl().observeForever(musicObserver);
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

    @Override
    public void songUrlIsEmpty(Music music) {
//        if (!TextUtils.isEmpty(music.getMUSICRID())) {  //酷我的歌
//            viewModel.getSongUrl(music);
//        } else if (music.getId() != 0) {           //网易的歌
//            viewModel.checkSong(music);
//        }
    }

    @Override
    public void onClick(View v) {
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (AudioPlayer.get().isPlaying()){
//            statusBar.setMusic(AudioPlayer.get().getPlayMusic());
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        viewModel.getSongUrl().removeObserver(musicObserver);
        if (statusBar != null) {
            AudioPlayer.get().removeOnPlayEventListener(statusBar);
        }
    }

}