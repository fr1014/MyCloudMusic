package com.fr1014.mycoludmusic.utils.reboundlayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.fr1014.mycoludmusic.R
import kotlin.math.abs

const val DIRECTION_LEFT = 1
const val DIRECTION_RIGHT = 2
const val DIRECTION_UP = 3
const val DIRECTION_DOWN = 4

interface OnBounceDistanceChangeListener {
    fun onDistanceChange(distance: Int, direction: Int)
    fun onFingerUp(distance: Int, direction: Int)
}

class ReBoundLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mTouchSlop = 0
    var mDownX = 0f
    var mDownY = 0f
    var isIntercept = false
    var innerView: View? = null
    var resistance = 1f
    var orientation = LinearLayout.HORIZONTAL
    var mDuration = 0L
    var mInterpolator = AccelerateDecelerateInterpolator()
    var isNeedReset = false
    var resetDistance = Int.MAX_VALUE
    var onBounceDistanceChangeListener: OnBounceDistanceChangeListener? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ReBoundLayout)
        orientation = array.getInt(R.styleable.ReBoundLayout_reBoundOrientation, LinearLayout.HORIZONTAL)
        resistance = array.getFloat(R.styleable.ReBoundLayout_resistance, 1f)
        mDuration = array.getInt(R.styleable.ReBoundLayout_reBoundOrientation, 300).toLong()
        array.recycle()
        if (resistance < 1f) {
            resistance = 1f
        }
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        innerView?.let {
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    it.clearAnimation()
                    mDownX = ev.x
                    mDownY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val difX = ev.x - mDownX
                    val difY = ev.y - mDownY
                    Log.d("hello", "onInterceptTouchEvent: $orientation")
                    Log.d("hello", "difX: $difX")
                    Log.d("hello", "difY: $difY")
                    if (orientation == LinearLayout.HORIZONTAL) {
                        if (abs(difX) > mTouchSlop && abs(difX) > abs(difY)) {
                            var parent = parent
                            while (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true)
                                parent = parent.parent
                                isIntercept = true
                            }
                            if (!it.canScrollHorizontally(-1) && difX > 0) {
                                //右拉到边界
                                return true
                            }
                            if (!it.canScrollHorizontally(1) && difX < 0) {
                                //左拉到边界
                                return true
                            }
                        }
                    } else {
                        if (abs(difY) > mTouchSlop && abs(difY) > abs(difX)) {
                            var parent = parent
                            while (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true)
                                parent = parent.parent
                                isIntercept = true
                            }
                            if (!it.canScrollVertically(-1) && difY > 0) {
                                Log.d("hello", "onInterceptTouchEvent: 下拉至边界")
                                //下拉到边界
                                return true
                            }
                            if (!it.canScrollVertically(1) && difY > 0) {
                                //上啦到边界
                                return true
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    if (isIntercept) {
                        var parent = parent
                        while (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(false)
                            parent = parent.parent
                        }
                    }
                    isIntercept = false
                    mDownY = 0f
                    mDownX = 0f
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        innerView?.let {
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    Log.d("hello", "onTouchEvent: $orientation")
                    if (orientation == LinearLayout.HORIZONTAL) {
                        val difX = event.x - mDownX / resistance
                        var isRebound = false
                        if (!it.canScrollHorizontally(-1) && difX > 0) {
                            //右啦到边界
                            isRebound = true
                        } else if (!it.canScrollHorizontally(1) && difX < 0) {
                            //左拉到边界
                            isRebound = true
                        }
                        if (isRebound) {
                            it.translationX = difX
                            onBounceDistanceChangeListener?.onDistanceChange(Math.abs(difX).toInt(),
                                    if (difX > 0) DIRECTION_RIGHT else DIRECTION_LEFT)
                            return true
                        }
                    } else {
                        val difY = event.y - mDownY
                        Log.d("hello", "onTouchEvent dify: $difY")
                        Log.d("hello", "event.y: " + event.y)
                        Log.d("hello", "mDownY: $mDownY")
                        if (difY < 0) return true
                        var isRebound = false
                        if (!it.canScrollVertically(-1) && difY > 0) {
                            //下拉到边界
                            isRebound = true
                            Log.d("hello", "onTouchEvent: 下拉到边界")
                        } else if (!it.canScrollVertically(1) && difY < 0) {
                            isRebound = true
                            Log.d("hello", "onTouchEvent: 上拉到边界")
                        }
                        if (isRebound) {
                            it.translationY = difY
                            onBounceDistanceChangeListener?.onDistanceChange(abs(difY).toInt(),
                                    if (difY > 0) DIRECTION_DOWN else DIRECTION_UP)
                            return true
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    if (orientation == LinearLayout.HORIZONTAL) {
                        val difX = it.translationX
                        if (difX != 0f) {
                            if (abs(difX) < resetDistance || isNeedReset) {
                                it.animate().translationX(0f).setDuration(mDuration).interpolator = mInterpolator
                            }
                            onBounceDistanceChangeListener?.onFingerUp(abs(difX).toInt(),
                                    if (difX > 0) DIRECTION_RIGHT else DIRECTION_LEFT)
                        }
                    } else {
                        val difY = it.translationY
                        if (difY != 0f) {
                            if (abs(difY) < resetDistance || isNeedReset) {
                                it.animate().translationY(0f).setDuration(mDuration).interpolator = mInterpolator
                            }
                            onBounceDistanceChangeListener?.onFingerUp(abs(difY).toInt(),
                                    if (difY > 0) DIRECTION_DOWN else DIRECTION_UP)
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            innerView = getChildAt(0)
        } else {
            throw IllegalArgumentException("it must have innerView")
        }
    }
}