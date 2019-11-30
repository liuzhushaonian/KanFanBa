package com.app.legend.kanfanba.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Video(var title:String,var type:String,var introduction:String,var time:String,var book:String,var url:String,var post_id:Int,var bigBook:String,var isPlay:Int):Parcelable {
}