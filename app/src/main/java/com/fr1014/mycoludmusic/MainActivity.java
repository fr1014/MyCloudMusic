package com.fr1014.mycoludmusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.customview.PlayStatusBarView;
import com.fr1014.mycoludmusic.databinding.ActivityMainBinding;
import com.fr1014.mycoludmusic.ui.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BasePlayActivity<ActivityMainBinding, TopListViewModel> implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION_CODE = 100;

    private AppBarConfiguration mAppBarConfiguration;
    private PlayStatusBarView statusBar;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public TopListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(this, factory).get(TopListViewModel.class);
    }

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
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.playListDetailFragment) {
                    mViewBinding.appBarMain.toolbar.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.topListFragment) {
                    mViewBinding.appBarMain.toolbar.setBackgroundColor(getColor(R.color.white));
                    mViewBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                    mViewBinding.appBarMain.llContent.setVisibility(View.GONE);
                } else {
                    mViewBinding.appBarMain.toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
                    mViewBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                    mViewBinding.appBarMain.llContent.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewBinding.appBarMain.etKeywords.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(SearchActivity.class);
                }
                return true;
            }
        });
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
    public void initData() {
        requestMyPermissions();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (statusBar != null) {
            AudioPlayer.get().removeOnPlayEventListener(statusBar);
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
}