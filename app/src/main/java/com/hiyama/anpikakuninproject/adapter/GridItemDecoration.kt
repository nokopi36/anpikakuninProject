package com.hiyama.anpikakuninproject.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridItemDecoration: ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = 5
        outRect.bottom = 5
        if(5 < parent.getChildAdapterPosition(view)) {
            // ここ適当
            val space = (parent.width - 32 ) / 5 - 60
            outRect.left = 0
            outRect.right = 0
        }else {
            outRect.left = 0
            outRect.right = 0
        }
        //super.getItemOffsets(outRect, view, parent, state)
    }
}