package com.fr1014.mycoludmusic.rx;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class MyDisposableObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
