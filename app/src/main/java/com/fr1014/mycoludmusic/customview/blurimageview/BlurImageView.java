package com.fr1014.mycoludmusic.customview.blurimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.fr1014.mycoludmusic.utils.BlurImageUtils;

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
        setImageDrawable(BlurImageUtils.getForegroundDrawable(getContext(),bitmap,5));
    }

    public void setBlurImageUrl(String url){
        setScaleType(ScaleType.CENTER_CROP);
        if (TextUtils.isEmpty(url)){
            setImageDrawable(BlurImageUtils.getForegroundDrawable(getContext(),BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder),5));
            return;
        }
        Glide.with(getContext())
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BlurImageView.this.setImageDrawable(BlurImageUtils.getForegroundDrawable(getContext(),resource,5));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

}
