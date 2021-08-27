package com.fr1014.mycoludmusic.ui.playing;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.utils.reboundlayout.OnBounceDistanceChangeListener;
import com.fr1014.mycoludmusic.utils.reboundlayout.ReBoundLayout;

import static com.fr1014.mycoludmusic.utils.reboundlayout.ReBoundLayoutKt.*;

public class ReBoundActivity extends AppCompatActivity implements OnBounceDistanceChangeListener {

    ReBoundLayout reBoundLayout;
    private int mResetDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fragment_slide_enter, 0);
        setContentView(R.layout.activity_rebound);
        reBoundLayout = findViewById(R.id.rebound_layout);
        mResetDistance = (int) (ScreenUtils.getScreenHeight() / 5);
        reBoundLayout.setNeedReset(false);
        reBoundLayout.setResetDistance(mResetDistance);
        reBoundLayout.setOnBounceDistanceChangeListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CurrentPlayMusicFragment()).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fragment_slide_exit);
    }

    @Override
    public void onDistanceChange(int distance, int direction) {
//        switch (direction) {
//            case DIRECTION_LEFT:
//                break;
//            case DIRECTION_RIGHT:
//                break;
//            case DIRECTION_UP:
//                break;
//            case DIRECTION_DOWN:
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onFingerUp(int distance, int direction) {
        if (direction == DIRECTION_DOWN) {
            if (distance >= mResetDistance) {
                finish();
            }
        }
    }
}
