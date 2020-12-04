package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fr1014.mycoludmusic.R;

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
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.bg_play)
                .error(R.drawable.bg_play)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this);
    }
}
