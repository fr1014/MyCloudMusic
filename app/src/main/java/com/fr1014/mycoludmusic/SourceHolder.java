package com.fr1014.mycoludmusic;
import com.fr1014.mycoludmusic.musicmanager.Preferences;

/**
 * Create by fanrui on 2020/12/29
 * Describe:
 */
public class SourceHolder {

    private SourceHolder(){

    }

    public static SourceHolder get(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static final SourceHolder instance = new SourceHolder();
    }

    public void setSource(String source){
        Preferences.saveMusicSource(source);
    }

    public String getSource(){
        return Preferences.getMusicSource();
    }
}
