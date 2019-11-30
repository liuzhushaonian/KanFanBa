package com.app.legend.kanfanba.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(var code:Int,var info:String):Parcelable {
}