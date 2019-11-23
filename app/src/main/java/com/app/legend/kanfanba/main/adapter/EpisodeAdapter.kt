package com.app.legend.kanfanba.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.main.fragment.OnEpisodeItemClick
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_item.view.*

class EpisodeAdapter:BaseAdapter<EpisodeAdapter.ViewHolder>() {


    private var episodeList: MutableList<Episode> =ArrayList()

    var onEpisodeItemClick: OnEpisodeItemClick?=null

    public fun addList(list: MutableList<Episode>){

        episodeList.addAll(list)

        notifyItemRangeChanged(episodeList.size,list.size)

    }

    /**
     * 清空
     */
    public fun clean(){

        episodeList.clear()

        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view=LayoutInflater.from(p0.context).inflate(R.layout.episode_item,p0,false)

        val holder=ViewHolder(view)

        holder.itemView.setOnClickListener {

            if (onEpisodeItemClick!=null){

                val p=holder.adapterPosition

                val e=episodeList[p]

                onEpisodeItemClick?.click(e)

            }

        }

        return holder
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)

        val episode=episodeList[position]

        holder.itemView.ep_title.text=episode.title
        holder.itemView.ep_date.text=episode.time
        Glide.with(holder.itemView).load(episode.book).into(holder.itemView.ep_book)

    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){


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

            val w=(width/2)-sapce

            val h=(w*1.333333).toInt()

            layoutParams.width=w
            layoutParams.height=h

            itemView.layoutParams=layoutParams

            val param=itemView.ep_book.layoutParams//重绘整个view的高度

            val bh=(h*0.56).toInt()

            param.height=bh

            itemView.ep_book.layoutParams=param//重绘imageview的高度

        }

    }
}