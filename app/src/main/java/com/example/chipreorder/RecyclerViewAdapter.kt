package com.example.chipreorder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import java.util.*

internal class RecyclerViewAdapter(private var mItems: MutableList<Pair<String, Int>>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ItemViewHolder
        val itemPair = mItems!![position]
        vh.mItemChip.apply {
            text = "${itemPair.first} Text"
            chipBackgroundColor = ColorStateList.valueOf(itemPair.second)
            setChipBackgroundColorResource(itemPair.second)
        }
    }

    override fun getItemCount(): Int = mItems?.run { size } ?: 0

    override fun getItemViewType(position: Int): Int = TYPE_ITEM

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mItems as MutableList<*>, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mItems as MutableList<*>, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    companion object {
        @Suppress("unused")
        private val TAG = this::class.java.simpleName
        private const val TYPE_ITEM = 1
    }
}

class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item),
    ItemTouchHelperViewHolder {
    val mItemChip: Chip = item.findViewById(R.id.chip)
    private var mChipSavedColor: ColorStateList? = null

    override fun onItemSelected() {
        mChipSavedColor = mItemChip.chipBackgroundColor
        mItemChip.setChipBackgroundColorResource(android.R.color.white)
        mItemChip.chipStrokeWidth = 5f
        mItemChip.setChipStrokeColorResource(android.R.color.black)
    }

    override fun onItemClear() {
        mChipSavedColor?.apply {
            mItemChip.chipBackgroundColor = this
        }
        mItemChip.chipStrokeWidth = 0f
        mItemChip.setChipStrokeColorResource(android.R.color.transparent)

    }
}