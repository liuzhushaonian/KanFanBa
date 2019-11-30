package com.app.legend.kanfanba.main.adapter

import com.app.legend.kanfanba.bean.Video

interface OnVideoListItemClick {

    fun clickItem(video: Video,list: MutableList<Video>)
}