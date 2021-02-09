package com.fr1014.mycoludmusic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class ResourceUtils {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getGrayDrawable(Context context, int resId) {
        Drawable drawable = context.getDrawable(resId);
        if (drawable != null){
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
        return drawable;
    }
}
