package com.fr1014.mycoludmusic;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.customview.PlayStatusBarView;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearChDefaultBean;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchDefault;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Profile;
import com.fr1014.mycoludmusic.databinding.ActivityMainBinding;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.musicmanager.QuitTimer;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.ui.SwitchDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.ui.search.SearchActivity;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Deque;

public class MainActivity extends BasePlayActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private AppBarConfiguration mAppBarConfiguration;
    private PlayStatusBarView statusBar;
    private OnPlayerEventListener playEventListener;
    private SearchDefault mSearchDefault = null;
    private ObjectAnimator animator1;
    private ObjectAnimator animator2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        Profile profile = getUserProfile();
        if (profile != null) {
            mViewModel.getLikeIdList(profile.getUserId());
        }
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public MainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    protected void initView() {
        setSupportActionBar(mViewBinding.appBarMain.toolbar);
        mViewBinding.appBarMain.toolbar.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(mViewBinding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(mViewBinding.navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (getSupportActionBar() == null) return;
                switch (destination.getId()) {
                    case R.id.topListFragment:
                        mViewBinding.appBarMain.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                        if (!getSupportActionBar().isShowing()) {
                            getSupportActionBar().show();
                        }
                        mViewBinding.appBarMain.llContent.setVisibility(View.GONE);
                        break;
                    case R.id.playListDetailFragment:
                    case R.id.userInfoFragment:
                    case R.id.dayRecommendFragment:
                    case R.id.songSaleFragment:
                    case R.id.commentFragment:
                        getSupportActionBar().hide();
                        break;
                    default:
                        mViewBinding.appBarMain.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (!getSupportActionBar().isShowing()) {
                            getSupportActionBar().show();
                        }
                        mViewBinding.appBarMain.llContent.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        initToolbar();
        initCustomNavView();
        initClickListener();
    }

    private void initCustomNavView() {
        mViewBinding.navView.setViewModel(mViewModel, this);
        initNavHeaderViewData(getUserProfile());
    }

    private void initNavHeaderViewData(Profile profile) {
        mViewBinding.navView.initNavHeaderViewData(profile);
    }

    private void initToolbar() {
        source = SourceHolder.get().getSource();
        mViewBinding.appBarMain.tvSearch.setText("当前搜索源：" + source);
    }

    private void initClickListener() {
        mViewBinding.appBarMain.ivSwitch.setOnClickListener(this);
        mViewBinding.appBarMain.tvSearch.setOnClickListener(this);
    }

    private Profile getUserProfile() {
        return Preferences.getUserProfile();
    }

    //首次绑定Service时该方法被调用
    @Override
    protected void onServiceBound() {
        QuitTimer.get().setOnTimerListener(mViewBinding.navView);
        if (statusBar == null) {
            statusBar = new PlayStatusBarView(MainActivity.this, getSupportFragmentManager());
            getLifecycle().addObserver(statusBar);
            playEventListener = statusBar.getOnPlayEventListener();
            statusBar.addMusicListChangeListener();
            if (playEventListener != null) {
                AudioPlayer.get().addOnPlayEventListener(playEventListener);
            }
            mViewBinding.appBarMain.contentMain.flPlaystatus.addView(statusBar);

            AudioPlayer.get().initMusicList();
        }
    }

    @Override
    public void initData() {
        mViewModel.searchDefault(String.valueOf(System.currentTimeMillis()));
        requestMyPermissions();
    }

    @Override
    public void initViewObservable() {
        mViewModel.getSearchDefault().observe(this, new Observer<SearchDefault>() {
            @Override
            public void onChanged(SearchDefault searchDefault) {
                mSearchDefault = searchDefault;
                startSearchAnimator(mViewBinding.appBarMain.tvSearch, searchDefault);
            }
        });
    }

    private void startSearchAnimator(TextView view, SearchDefault searchDefault) {
        animator1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f);
        animator1.setDuration(2500);
        animator2 = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f);
        animator2.setDuration(2500);
        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setText(searchDefault.getData().getShowKeyword());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator1.start();
            }
        });
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setText("当前搜索源：" + source);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator2.start();
            }
        });
        animator1.start();
    }

    private void startAnimator() {
        if (animator1 != null && animator2 != null) {
            animator1.start();
        }
    }

    private void stopAnimator() {
        if (animator1 != null && animator2 != null) {
            animator1.end();
            animator2.end();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimator();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimator();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_switch:
                new SwitchDialogFragment().show(getSupportFragmentManager(), "switch_dialog");
                break;
            case R.id.tv_search:
                if (mSearchDefault != null) {
                    SearChDefaultBean data = mSearchDefault.getData();
                    SearchActivity.Companion.startSearchActivity(this, data.getShowKeyword(), data.getRealkeyword(), data.getSearchType());
                } else {
                    startActivity(SearchActivity.class);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        AudioPlayer.get().saveMusicsInfo();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playEventListener != null) {
            AudioPlayer.get().removeOnPlayEventListener(playEventListener);
        }
        if (statusBar != null) {
            getLifecycle().removeObserver(statusBar);
        }
    }

    private void requestMyPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
//            for (int i = 0; i < permissions.length; i++) {
//                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
//            }
        }
    }

    public void back() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Deque<NavBackStackEntry> backStack = navController.getBackStack();
        if (backStack.size() == 2) {
            StatusBarUtils.setImmersiveStatusBar(getWindow(), true);
        }
    }
}