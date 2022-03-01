package com.example.chipgroupreorder

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class ChipShadowBuilder(view: View) : View.DragShadowBuilder(view) {
    private val mShadow = view

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width
        val height: Int = view.height
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
        mShadow.draw(canvas)
    }
}