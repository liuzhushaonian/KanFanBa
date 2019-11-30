package com.app.legend.kanfanba.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter:BaseAdapter<MovieAdapter.ViewHolder>() {


    private val videoList:MutableList<Video> =ArrayList()


    var onMovieItemClick:OnMovieItemClick?=null

    public fun addList(list: MutableList<Video>){

        videoList.addAll(list)

        notifyItemRangeChanged(videoList.size,list.size)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view=LayoutInflater.from(p0.context).inflate(R.layout.movie_item,p0,false)

        val holder=ViewHolder(view)

        holder.itemView.setOnClickListener {

            if (onMovieItemClick!=null){

                val p=holder.adapterPosition
                val v=videoList[p]
                onMovieItemClick?.itemClick(v)
            }
        }

        return holder
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)

        val v=videoList[position]

        holder.itemView.movie_title.text=v.title
        Glide.with(holder.itemView).load(v.book).into(holder.itemView.movie_book)

    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        init {
            reDraw()
        }


        private fun reDraw(){

            val p=itemView.layoutParams

            val w=itemView.resources.displayMetrics.widthPixels

            val width=((w-itemView.resources.getDimensionPixelSize(R.dimen.movie_space))/3).toInt()

            val h=(width*1.56).toInt()

            p.width=width

            p.height=h

            itemView.layoutParams=p

            val ih=(h*0.8).toInt()

            val ip=itemView.movie_book.layoutParams
            ip.height=ih

            itemView.movie_book.layoutParams=ip

        }
    }

    public fun clean(){

        videoList.clear()

        notifyDataSetChanged()

    }
}