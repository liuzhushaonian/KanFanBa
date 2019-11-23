package com.app.legend.kanfanba.main.fragment

import com.app.legend.kanfanba.bean.Video

interface OnNewestItemClick {

    fun clickItem(position: Int,video: Video)

}