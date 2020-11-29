package com.fr1014.mycoludmusic.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = getViewBinding();
        setContentView(mViewBinding.getRoot());
        initView();
        initViewModel();
        initData();
    }

    protected abstract void initView();

    protected abstract void initViewModel();

    protected abstract VB getViewBinding();

    protected abstract void initData();

    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseAllRequests();
    }
}
