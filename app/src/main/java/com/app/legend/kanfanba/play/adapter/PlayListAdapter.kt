package com.app.legend.kanfanba.play.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.play_list_item.view.*

class PlayListAdapter: BaseAdapter<PlayListAdapter.ViewHolder>() {


    private var playList:MutableList<Video> =ArrayList()

    var itemClick: OnPlayListItemClick?=null

    public fun initList(list: MutableList<Video>){

        this.playList=list

        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view=LayoutInflater.from(p0.context).inflate(R.layout.play_list_item,p0,false)

        val holder=ViewHolder(view)

        holder.itemView.setOnClickListener {

            if (itemClick!=null){

                val p=holder.adapterPosition

                val v=playList[p]

                itemClick?.clickItem(p,v)

            }

        }

        return holder
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)

        val v=playList[position]

        val t="${position+1} ${v.title}"

        holder.title.text=t
        Glide.with(holder.itemView).load(v.book).into(holder.itemView.item_book)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title:TextView=itemView.findViewById(R.id.v_title)

    }

}