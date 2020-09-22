package com.fr1014.mymvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private volatile static ViewModelFactory instance;
    private final Application mApplication;


    private ViewModelFactory(Application application){
        this.mApplication = application;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (instance == null){
            synchronized (ViewModelFactory.class){
                if (instance == null){
                    instance = new ViewModelFactory(application);
                }
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BaseViewModel.class)){
            return (T) new BaseViewModel(mApplication);
        }
        try {
            String className = modelClass.getName();
            Class<?> classViewModel = Class.forName(className);
            Constructor<?> constructor = classViewModel.getConstructor(Application.class);
            ViewModel viewModel = (ViewModel) constructor.newInstance(mApplication);
            return (T) viewModel;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.create(modelClass);
    }
}
