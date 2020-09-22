package com.fr1014.mycoludmusic.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间:2020/9/3
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class RxSchedulers {

    public static <T> ObservableTransformer<T,T> apply(){
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T,T> applyIO(){
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
