package com.fr1014.mymvvm.base;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public interface IModel {

    /**
     * ViewModel销毁时清除Model，与 Model共消亡。Model层同样不能持有长生命周期对象
     */
    void onCleared();
}
