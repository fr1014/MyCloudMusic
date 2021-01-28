package com.fr1014.mycoludmusic.customview.behavior

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.fr1014.mycoludmusic.R
import com.google.android.material.appbar.AppBarLayout


/**
 * 头部下拉放大Behavior
 */
class AppbarZoomBehavior(context: Context?, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {
    private lateinit var mImageView: ImageView
    //记录AppbarLayout原始高度
    private var mAppbarHeight = 0
    //记录ImageView原始高度
    private var mImageViewHeight = 0
    //放大最大高度
    private val MAX_ZOOM_HEIGHT = 500f
    //手指在Y轴滑动的总距离
    private var mTotalDy = 0f
    //图片缩放比例
    private var mScaleValue = 0f
    //Appbar的变化高度
    private var mLastBottom = 0
    //是否做动画标志
    private var isAnimate = false


    override fun onLayoutChild(
        parent: CoordinatorLayout,
        abl: AppBarLayout,
        layoutDirection: Int
    ): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        init(abl)
        return handled
    }

    /**
     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     *
     * @param abl
     */
    private fun init(abl: AppBarLayout) {
        abl.clipChildren = false
        mAppbarHeight = abl.height
        mImageView = abl.findViewById<View>(R.id.iv_title) as ImageView
        mImageViewHeight = mImageView.height
    }

    /**
     * 是否处理嵌套滑动
     *
     * @param parent
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @param type
     * @return
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        isAnimate = true
        return true
    }

    /**
     * 在这里做具体的滑动处理
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (child.bottom >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
            zoomHeaderImageView(child, dy)
        } else {
            if (child.bottom > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                consumed[1] = dy
                zoomHeaderImageView(child, dy)
            } else {
                if (valueAnimator == null || !valueAnimator!!.isRunning) {
                    super.onNestedPreScroll(
                        coordinatorLayout,
                        child,
                        target,
                        dx,
                        dy,
                        consumed,
                        type
                    )
                }
            }
        }
    }


    /**
     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
     *
     * @param abl
     * @param dy
     */
    private fun zoomHeaderImageView(abl: AppBarLayout, dy: Int) {
        mTotalDy += -dy.toFloat()
        mTotalDy = mTotalDy.coerceAtMost(MAX_ZOOM_HEIGHT)
        mScaleValue = 1f.coerceAtLeast(1f + mTotalDy / MAX_ZOOM_HEIGHT)
        mImageView.apply {
            scaleX = mScaleValue
            scaleY = mScaleValue
        }
        mLastBottom = mAppbarHeight + (mImageViewHeight / 2 * (mScaleValue - 1)).toInt()
        abl.bottom = mLastBottom
    }


    /**
     * 处理惯性滑动的情况
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (velocityY > 100) {
            isAnimate = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }


    /**
     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
     *
     * @param coordinatorLayout
     * @param abl
     * @param target
     * @param type
     */
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
        recovery(abl)
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    private var valueAnimator: ValueAnimator? = null

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     *
     * @param abl
     */
    private fun recovery(abl: AppBarLayout) {
        if (mTotalDy > 0) {
            mTotalDy = 0f
            if (isAnimate) {
                valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220)
                valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    mImageView.apply {
                        scaleX = value
                        scaleY = value
                    }
                    abl.bottom =
                        (mLastBottom - (mLastBottom - mAppbarHeight) * animation.animatedFraction).toInt()
                })
                valueAnimator?.start()
            } else {
                mImageView.apply {
                    scaleX = 1f
                    scaleY = 1f
                }
                abl.bottom = mAppbarHeight
            }
        }
    }
}