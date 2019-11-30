package com.app.legend.kanfanba.main.presenter

import com.app.legend.kanfanba.bean.Video

interface IMovieFragment {

    fun setData(list: MutableList<Video>)

    fun onError(msg:String)

}