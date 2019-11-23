package com.app.legend.kanfanba.utils

import android.util.Log
import androidx.annotation.WorkerThread
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 网络访问工具，获取相关信息并返回
 */
class NetWorkUtil {

    companion object{

        @WorkerThread
        public fun getHtml(url:String,formBody: FormBody):String?{

            Log.d("url---->>","$url")

            var result:String?=""

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build()


            val request=Request.Builder()
                .post(formBody)
                .url(url)
                .build()


            val call=httpClient.newCall(request)

            val response=call.execute()


            result = response.body?.string()

            return result
        }


        public fun getIndex():String{

            var r:String=""

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build()


            val request=Request.Builder()
                .get()
                .url("https://akcp.kanfanba.com/")
                .build()

            val call=httpClient.newCall(request)

            val response=call.execute()

            r=response.body!!.string()

            return r

        }

    }



}