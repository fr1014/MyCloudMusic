package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean;

import java.util.List;

/**
 * Create by fanrui on 2020/12/29
 * Describe:
 */
public class WYSearchDetail {
    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public static class ResultBean {

        private int songCount;
        private List<SongsBean> songs;

        public int getSongCount() {
            return songCount;
        }

        public List<SongsBean> getSongs() {
            return songs;
        }
    }
}
