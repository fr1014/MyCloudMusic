package com.fr1014.mycoludmusic.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fr1014.mycoludmusic.musicmanager.PlayService;

import java.lang.ref.WeakReference;

public abstract class BasePlayActivity<VB extends ViewBinding> extends BaseActivity<VB> {

    protected PlayService playService;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        serviceConnection = new PlayServiceConnect(this);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    private static class PlayServiceConnect implements ServiceConnection {
        private final WeakReference<Context> reference;

        public PlayServiceConnect(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BasePlayActivity activity = (BasePlayActivity) reference.get();
            activity.playService = ((PlayService.PlayBinder) service).getService();
            activity.onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    protected void onServiceBound() {
    }

    @Override
    protected void onDestroy() {
        if (serviceConnection != null){
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }
}
