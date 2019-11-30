package com.app.legend.kanfanba.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.fragment.OnNewestItemClick
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.bumptech.glide.Glide

class NewestAdapter :BaseAdapter<NewestAdapter.ViewHolder>(){


    private val videoList:MutableList<Video> = ArrayList()

    lateinit var onNewestItemClick: OnNewestItemClick


    public fun clean(){

        videoList.clear()
        notifyDataSetChanged()

    }

    /**
     *
     */
    public fun addVideos(list: MutableList<Video>){

        videoList.addAll(list)
        notifyItemRangeChanged(videoList.size,list.size)

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view=LayoutInflater.from(p0.context).inflate(R.layout.new_item,p0,false)

        val holder=ViewHolder(view)

        holder.view.setOnClickListener {

            val p=holder.adapterPosition

            val v=videoList[p]

            onNewestItemClick.clickItem(p,v)

        }

        return holder
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)

        val v=videoList[position]

        holder.new_tag.text=v.type
        holder.new_title.text=v.title
        holder.new_date.text=v.time
        Glide.with(holder.view).load(v.book).into(holder.book)

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        val view:View=itemView

        val book:ImageView=itemView.findViewById(R.id.new_book)

        val new_tag:TextView=itemView.findViewById(R.id.new_tag)
        val new_title:TextView=itemView.findViewById(R.id.new_title)
        val new_date:TextView=itemView.findViewById(R.id.new_date)


        init {
            reDraw()
        }

        /**
         * 重绘view的大小，适配不同屏幕
         * 宽度是屏幕二分之一再减去32dp
         *
         */
        private fun reDraw(){

            val layoutParams=itemView.layoutParams

            val sapce=itemView.resources.getDimensionPixelOffset(R.dimen.new_space)

            val width=itemView.resources.displayMetrics.widthPixels

            val w=((width-sapce)/2).toInt()

            val h=(w*1.333333).toInt()

            layoutParams.width=w
            layoutParams.height=h

            itemView.layoutParams=layoutParams

            val param=book.layoutParams//重绘整个view的高度

            val bh=(h*0.56).toInt()

            param.height=bh

            book.layoutParams=param//重绘imageview的高度

        }

    }

}