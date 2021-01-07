package com.fr1014.mycoludmusic.customview.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class GridSpaceItemDecoration(private val space: Int, private val spanCount: Int, private val isSquare: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanSizeLookup = (parent.layoutManager as GridLayoutManager).spanSizeLookup
        val size = parent.adapter?.itemCount ?: 0
        val p = parent.getChildLayoutPosition(view)
        val lineCount = if (spanSizeLookup != null) {
            var line = 0
            var tmp = 0
            repeat(size) {
                val spanSize = spanSizeLookup.getSpanSize(it)
                tmp += spanSize
                line += tmp / spanCount
                tmp %= spanCount
            }
            line
        } else {
            (size + spanCount / 2) / spanCount
        }
        val lineIndex = if (spanSizeLookup != null) {
            var index = 0
            var tmp = 0
            repeat(p) {
                val spanSize = spanSizeLookup.getSpanSize(it)
                tmp += spanSize
                index += tmp / spanCount
                tmp %= spanCount
            }
            index
        } else {
            p / spanCount
        }

        if (lineIndex == 0) {
            outRect.top = space
            outRect.bottom = space / 2
        } else if (lineIndex == lineCount - 1) {
            outRect.top = space / 2
            outRect.bottom = space
        } else {
            outRect.top = space / 2
            outRect.bottom = space / 2
        }


        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

        if (isSquare) {
            outRect.left = space / 2
            outRect.right = space / 2
        } else {
            if (spanIndex == 0) {
                outRect.left = space
                outRect.right = space / 2
            } else if (spanIndex == spanCount - 1) {
                outRect.left = space / 2
                outRect.right = space
            } else {
                outRect.left = space / 2
                outRect.right = space / 2
            }
        }
    }
}