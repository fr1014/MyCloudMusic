package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.customview.blurimageview.FastBlurUtil;

/**
 * Create by fanrui on 2021/1/29
 * Describe:
 */
public class BlurImageUtils {

    public static Drawable getForegroundDrawable(Context context,Bitmap bitmap) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (ScreenUtils.getScreenWidth()
                * 1.0 / ScreenUtils.getScreenHeight() * 1.0);

        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 6, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(context.getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }
}
