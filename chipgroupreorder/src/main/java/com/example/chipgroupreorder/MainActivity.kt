package com.example.chipgroupreorder

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chipgroupreorder.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var mChipShadow: ChipShadowBuilder
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val items = makeList()
        items.forEach {
            Chip(this).apply {
                text = "${it.first} Text"
                chipBackgroundColor = ColorStateList.valueOf(it.second)
                setChipBackgroundColorResource(it.second)
                binding.chipGroup.addView(this)
            }
        }
        attachViewDragListener()

        binding.chipGroup.setOnDragListener(chipDragListener)

    }

    private val chipDragListener = View.OnDragListener { chipGroup, dragEvent ->
        chipGroup as ChipGroup
        val dragView = dragEvent.localState as View
        val dragViewIndex = chipGroup.indexOfChild(dragView)

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                dragView.visibility = View.INVISIBLE
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                val shiftIndex = findViewToShift(chipGroup, dragEvent.x, dragEvent.y)
                mChipShadow.savePosition(dragEvent.x, dragEvent.y)
                if (shiftIndex != -1 && shiftIndex != dragViewIndex) {
                    shiftViews(chipGroup, dragViewIndex, shiftIndex)
                }
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                true
            }

            DragEvent.ACTION_DROP -> {
                chipGroup.layoutTransition.setAnimator(
                    LayoutTransition.APPEARING, mChipShadow.getReleaseAnimatorSet()
                )
                // This statement ensures that the dragView is drawn above all other chips after
                // drop assuming that all chips have zero elevation. The APPEARING translation
                // will settle the chip back to zero elevation.
                // Todo: Need a better way to ensure that dragged chips are drawn over other chips.
                dragView.elevation = 1f

                dragView.visibility = View.VISIBLE
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }

            else -> {
                false
            }
        }
    }

    private fun findViewToShift(chipGroup: ChipGroup, posX: Float, posY: Float): Int {
        val hitRect = Rect()
        for (i in 0 until chipGroup.childCount) {
            val view = chipGroup.getChildAt(i)
            view.getHitRect(hitRect)
            if (hitRect.contains(posX.toInt(), posY.toInt())) {
                return i
            }
        }
        return -1
    }

    private fun shiftViews(chipGroup: ChipGroup, dragViewIndex: Int, viewToShiftIndex: Int) {
        val dragView = chipGroup.getChildAt(dragViewIndex)
        // The view system holds onto the parent of a removed view if a transition is pending.
        // This sometimes results in an IllegatStateException
        //     "The specified child already has a parent. You must call removeView() on the child's
        //     parent first."
        // which may be due to some sort of race condition. Kill the transition before removing
        // the view to allow the parent to be set to null in the removed view then enable
        // transitions to add the view back.
        chipGroup.apply {
            val saveTransition = layoutTransition
            layoutTransition = null
            removeViewAt(dragViewIndex)
            layoutTransition = saveTransition
            addView(dragView, viewToShiftIndex)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachViewDragListener() {
        for (i in 0 until binding.chipGroup.childCount) {

            binding.chipGroup.getChildAt(i).setOnLongClickListener { view: View ->

                mChipShadow = ChipShadowBuilder(view)

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    @Suppress("DEPRECATION")
                    view.startDrag(null, mChipShadow, view, 0)
                } else {
                    view.startDragAndDrop(null, mChipShadow, view, 0)
                }
                true
            }
        }
    }

    private fun makeList(): MutableList<Pair<String, Int>> {
        val items: MutableList<Pair<String, Int>> = ArrayList()
        for (i in 0..10) {
            val color =
                when (i % 3) {
                    0 -> android.R.color.holo_blue_light
                    1 -> android.R.color.holo_red_light
                    else -> android.R.color.holo_green_light
                }
            // Just produced individually identifiable strings of varying length.
            items.add(Pair(i.toString().repeat(i % 5 + 1), color))
        }
        return items
    }
}