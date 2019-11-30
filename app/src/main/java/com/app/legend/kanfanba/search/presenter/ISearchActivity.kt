package com.app.legend.kanfanba.search.presenter

import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video

interface ISearchActivity {

    fun setEpisodeList(list: MutableList<Episode>)

    fun setVideoList(list: MutableList<Video>)
    fun onError(msg:String)

}