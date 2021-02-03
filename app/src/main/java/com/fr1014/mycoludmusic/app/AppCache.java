package com.fr1014.mycoludmusic.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class AppCache {
    private Context mContext;
//    private final List<SheetInfo> mSheetList = new ArrayList<>();
    private final List<Activity> mActivityStack = new ArrayList<>();
//    private final LongSparseArray<DownloadMusicInfo> mDownloadList = new LongSparseArray<>();
//    private AMapLocalWeatherLive mAMapLocalWeatherLive;

    private AppCache() {
    }

    private static class SingletonHolder {
        private static final AppCache instance = new AppCache();
    }

    public static AppCache get() {
        return SingletonHolder.instance;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
//        ToastUtils.init(mContext);
        Preferences.init(mContext);
        ScreenUtils.init(mContext);
//        CrashReport.initCrashReport(mContext, "11c03b3b54", false);
        String isDebug = application.getResources().getString(R.string.is_debug);
        Bugly.init(mContext, "11c03b3b54", TextUtils.equals(isDebug, "true"));
        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());
    }

    public Context getContext() {
        return mContext;
    }

//    public List<SheetInfo> getSheetList() {
//        return mSheetList;
//    }

    public void clearStack() {
        List<Activity> activityStack = mActivityStack;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

//    public LongSparseArray<DownloadMusicInfo> getDownloadList() {
//        return mDownloadList;
//    }
//
//    public AMapLocalWeatherLive getAMapLocalWeatherLive() {
//        return mAMapLocalWeatherLive;
//    }

//    public void setAMapLocalWeatherLive(AMapLocalWeatherLive aMapLocalWeatherLive) {
//        mAMapLocalWeatherLive = aMapLocalWeatherLive;
//    }

    private class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d("hello", "onCreate: " + activity.getClass().getSimpleName());
            mActivityStack.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d("hello", "onDestroy: " + activity.getClass().getSimpleName());
            mActivityStack.remove(activity);
        }
    }
}
