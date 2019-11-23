package com.app.legend.ruminasu.activityes


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity




abstract class BaseActivity : AppCompatActivity(){


    protected var sharedPreferences:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences("kanfanba", Context.MODE_PRIVATE)

    }

}