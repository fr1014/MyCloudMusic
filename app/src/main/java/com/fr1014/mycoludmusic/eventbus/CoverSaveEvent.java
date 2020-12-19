package com.fr1014.mycoludmusic.eventbus;

/**
 * Create by fanrui on 2020/12/18
 * Describe:
 */
public class CoverSaveEvent {
    public boolean success;

    public CoverSaveEvent(boolean success){
        this.success = success;
    }
}
