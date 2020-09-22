package com.fr1014.mymvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel {
    protected M model;

    public BaseViewModel(@NonNull Application application) {
        this(application,null);
    }

    public BaseViewModel(@NonNull Application application,M model){
        super(application);
        this.model = model;
    }
}
