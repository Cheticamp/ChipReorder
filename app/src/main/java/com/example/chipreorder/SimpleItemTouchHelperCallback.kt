package com.example.chipreorder

/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Converted to Kotlin with other mods by Cheticamp
 */

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleItemTouchHelperCallback(
    private val mAdapter: ItemTouchHelperAdapter
) : ItemTouchHelper.Callback() {

    /**
     * Allow dragging only
     *
     * @param recyclerView RecyclerView
     * @param viewHolder   RecyclerView.ViewHolder
     * @return Flags that specify that dragging is ok
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(dragFlags, 0)
    }

    /**
     * Dismiss item when swiping is complete.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("onSwiped() not implemented")
    }

    /**
     * Inform the view holder that an action is being performed on it so it can set up
     * for movement effects.
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder == null) return
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder as ItemTouchHelperViewHolder
            // Let the view holder know that this item is being swiped
            viewHolder.onItemSelected()
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * Called when interaction and animation is complete.
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        // Tell the view holder it's time to restore the idle state
        viewHolder as ItemTouchHelperViewHolder
        viewHolder.onItemClear()
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }
}
