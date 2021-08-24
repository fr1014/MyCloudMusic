package com.fr1014.mycoludmusic.musicmanager.player;

/**
 * 创建时间:2020/9/29
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public enum PlayModeEnum {
    LOOP(0),  //循环
    SHUFFLE(1), //随机
    SINGLE(2); //单曲

    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SHUFFLE;
            case 2:
                return SINGLE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}