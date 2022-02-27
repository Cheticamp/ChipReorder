package com.example.chipreorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RecyclerViewAdapter(makeList())
        findViewById<RecyclerView>(R.id.recyclerView)?.apply {
            setAdapter(adapter)
            layoutManager = FlexboxLayoutManager(this@MainActivity)
            val touchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(adapter))
            touchHelper.attachToRecyclerView(this)
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