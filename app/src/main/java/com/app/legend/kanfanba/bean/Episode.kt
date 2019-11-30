package com.app.legend.kanfanba.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(var title:String,var time:String,var count:Int,var introduction:String,var type:String,var book:String,var url:String,var bigBook:String) : Parcelable {
}