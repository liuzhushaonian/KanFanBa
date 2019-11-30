package com.app.legend.kanfanba.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R

class MovieSpace: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State) {


        val position:Int=parent.getChildAdapterPosition(view)

        val space:Int=view.resources.getDimensionPixelOffset(R.dimen.item_space)

//        if (position%3==1) {

            outRect.left = space
//        }

//        if (position%3==2){
//
//            outRect.left = space*2
//
//        }

        outRect.top=space



    }
}