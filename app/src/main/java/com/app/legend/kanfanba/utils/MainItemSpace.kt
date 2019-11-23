package com.app.legend.ruminasu.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.app.legend.kanfanba.R

class MainItemSpace: RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position:Int=parent.getChildAdapterPosition(view)

        val space:Int=view.resources.getDimensionPixelOffset(R.dimen.item_space)

        if (position%2==1) {

            outRect.left = space
        }

        if (position%2==0){

            outRect.left = space*2

        }

        outRect.top=space


    }
}