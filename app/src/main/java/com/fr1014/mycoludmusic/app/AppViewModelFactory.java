package com.fr1014.mycoludmusic.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fr1014.mycoludmusic.data.DataRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 *
 * 工厂方法创建viewModel对象
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile AppViewModelFactory instance = null;
    private final Application mApplication;
    private final DataRepository mDataRepository;

    private AppViewModelFactory(Application application, DataRepository repository) {
        this.mApplication = application;
        this.mDataRepository = repository;
    }

    public static AppViewModelFactory getInstance(Application application) {
        if (instance == null) {
            synchronized (AppViewModelFactory.class) {
                instance = new AppViewModelFactory(application, MyApplication.provideRepository());
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            String classname = modelClass.getName();
            Class<?> c = Class.forName(classname);
            Constructor<?> constructor = c.getConstructor(Application.class, DataRepository.class);
            ViewModel viewModel = (ViewModel) constructor.newInstance(mApplication, mDataRepository);
            return (T) viewModel;
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.create(modelClass);
    }
}
