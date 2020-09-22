package com.fr1014.mymvvm.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 创建时间:2020/9/6
 * 作者:fr
 * 邮箱:1546352238@qq.com
 *
 * 去除 LiveData 的粘性事件
 */
public class BusLiveData<T> extends MutableLiveData<T> {
    private static final int START_VERSION = -1;
    private int mVersion = START_VERSION;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, new ObserverWrapper<T>((Observer<T>) observer, this));
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        mVersion++;
        super.postValue(value);
    }

    private static class ObserverWrapper<T> implements Observer<T> {
        private int mLastVersion;
        private Observer<T> observer;
        private BusLiveData<T> liveData;

        public ObserverWrapper(Observer<T> observer, BusLiveData<T> liveData) {
            this.observer = observer;
            this.liveData = liveData;
            this.mLastVersion = liveData.mVersion;
        }

        @Override
        public void onChanged(T t) {
            if (mLastVersion >= liveData.mVersion) {
                return;
            }
            mLastVersion = liveData.mVersion;
            observer.onChanged(t);
        }
    }
}

