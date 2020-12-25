package com.fr1014.mycoludmusic.customview.blurimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.utils.DisplayUtil;

public class BlurImageView extends AppCompatImageView {

    public BlurImageView(@NonNull Context context) {
        super(context);
        initView();
    }

    public BlurImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BlurImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    public void setBitmap(Bitmap bitmap){
        setScaleType(ScaleType.CENTER_CROP);
        setImageDrawable(getForegroundDrawable(bitmap));
    }

    public void setBlurImageUrl(String url){
        setScaleType(ScaleType.CENTER_CROP);
        if (TextUtils.isEmpty(url)){
            setImageDrawable(getForegroundDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.bg_play)));
            return;
        }
        Glide.with(getContext())
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bg_play)
                .error(R.drawable.bg_play)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BlurImageView.this.setImageDrawable(getForegroundDrawable(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    private Drawable getForegroundDrawable(Bitmap bitmap) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtil.getScreenWidth()
                * 1.0 / DisplayUtil.getScreenHeight() * 1.0);

        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 5, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

}
