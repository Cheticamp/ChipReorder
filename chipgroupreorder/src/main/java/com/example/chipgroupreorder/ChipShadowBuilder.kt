package com.example.chipgroupreorder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class ChipShadowBuilder(view: View, maxElevation: Float) : View.DragShadowBuilder(view) {
    private val mShadow = view
    private val mMaxElevation = maxElevation
    private var mLastX = 0f
    private var mLastY = 0f
    private val mTouchPoint = Point()

    private val mReleaseAnimatorSet = AnimatorSet().apply {
        val animDuration =
            mShadow.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val moveHomeAnimatorX = ObjectAnimator.ofFloat(mShadow, "translationX", 0f).apply {
            duration = animDuration
        }
        val moveHomeAnimatorY = ObjectAnimator.ofFloat(mShadow, "translationY", 0f).apply {
            duration = animDuration
        }
        val elevationAnimator =
            ObjectAnimator.ofFloat(mShadow, "elevation", mShadow.elevation).apply {
                duration = animDuration
            }
        play(moveHomeAnimatorX).with(moveHomeAnimatorY).with(elevationAnimator)
    }

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width
        val height: Int = view.height
        size.set(width, height)
        mTouchPoint.x = width / 2
        mTouchPoint.y = height / 2
        touch.set(mTouchPoint.x, mTouchPoint.y)
    }

    override fun onDrawShadow(canvas: Canvas) {
        mShadow.draw(canvas)
    }

    fun savePosition(x: Float, y: Float) {
        mLastX = x
        mLastY = y
    }

    fun getReleaseAnimatorSet(): AnimatorSet {
        mShadow.apply {
            translationX = mLastX - x - mTouchPoint.x
            translationY = mLastY - y - mTouchPoint.y
        }
        return mReleaseAnimatorSet
    }

    fun getMaxElevation() = mMaxElevation
}