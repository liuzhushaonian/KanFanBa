package com.app.legend.kanfanba.m3u8

import android.util.Log
import com.app.legend.kanfanba.m3u8.bean.M3u8
import com.app.legend.kanfanba.utils.SSLUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


/**
 * m3u8解析与下载
 * 先解析m3u8链接，然后获取信息，解析信息并封装，用于下载
 */
class M3u8Downloader {

    //临时存放地点
    private var temp=""

    //最终保存文件的位置
    private var savePath=""


    private var m3u8List:MutableList<M3u8> =ArrayList();


    public fun download(url: String){

        getM3u8Infos(url)

    }


    private fun getM3u8Infos(url:String){


        var r=""

        val httpClient= OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .sslSocketFactory(SSLUtil.createSSLSocketFactory()!!,SSLUtil.MyTrustManager())
            .hostnameVerifier(SSLUtil.getHostnameVerifier()!!)
            .build()


        val request= Request.Builder()
            .get()
            .addHeader("referer","https://akcp.kanfanba.com/")
            .url(url)
            .build()

        val call=httpClient.newCall(request)

        val response=call.execute()

        r=response.body!!.string()

        parseM3u8Info(r)


    }


    private fun parseM3u8Info(info:String){

        if (!info.startsWith("#EXTM3U")){
            return
        }

        val lines=info.split(System.getProperty("line.separator")!!)

        for (l in lines){

            Log.d("info---->>", l)

        }

    }



}