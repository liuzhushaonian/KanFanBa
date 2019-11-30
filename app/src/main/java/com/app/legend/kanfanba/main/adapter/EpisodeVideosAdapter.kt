package com.app.legend.kanfanba.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.play_list_item.view.*

class EpisodeVideosAdapter:BaseAdapter<EpisodeVideosAdapter.ViewHolder>() {

    var videoList:MutableList<Video> =ArrayList()

    var onListItemClick:OnVideoListItemClick?=null


    public fun initList(list: MutableList<Video>){

        videoList=list

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view=LayoutInflater.from(p0.context).inflate(R.layout.play_list_item,p0,false)

        val holder=ViewHolder(view)

        holder.itemView.setOnClickListener {

            if (onListItemClick!=null){

                val p=holder.adapterPosition

                val v=videoList[p]

                onListItemClick?.clickItem(v,videoList)


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
        holder.itemView.v_title.text=v.title
        Glide.with(holder.itemView).load(v.book).into(holder.itemView.item_book)

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){




    }

}