package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.utils.glide.DataCacheKey;

import de.hdodenhof.circleimageview.CircleImageView;

public class RemoteCircleImageView extends CircleImageView {

    public RemoteCircleImageView(@NonNull Context context) {
        super(context);
    }

    public RemoteCircleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RemoteCircleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url){
        Bitmap cacheBitmap = DataCacheKey.getCacheBitmap(url);
        if (cacheBitmap == null){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.film)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.bg_play)
                    .format(DecodeFormat.PREFER_ARGB_8888);

            Glide.with(getContext())
                    .load(url)
                    .apply(options)
                    .into(this);
        }else{
            setImageBitmap(cacheBitmap);
        }
    }
}
