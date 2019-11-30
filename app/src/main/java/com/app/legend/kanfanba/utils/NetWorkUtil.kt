package com.app.legend.kanfanba.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.legend.kanfanba.bean.Result
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
        public fun getHtml(url:String,formBody: FormBody):Result{

//            Log.d("url---->>","$url")

            var r=""

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .sslSocketFactory(SSLUtil.createSSLSocketFactory()!!,SSLUtil.MyTrustManager())
                .hostnameVerifier(SSLUtil.getHostnameVerifier()!!)
                .build()


            val request=Request.Builder()
                .post(formBody)
                .url(url)
                .build()


            val call=httpClient.newCall(request)

            val response=call.execute()


            val code=response.code

            val result=Result(code,r)

            if (code==200){

                r=response.body!!.string()
                result.info=r

            }

            return result
        }


        public fun getIndex():Result{

            var r:String=""

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .sslSocketFactory(SSLUtil.createSSLSocketFactory()!!,SSLUtil.MyTrustManager())
                .hostnameVerifier(SSLUtil.getHostnameVerifier()!!)
                .build()


            val request=Request.Builder()
                .get()
                .url("https://akcp.kanfanba.com/")
                .build()

            val call=httpClient.newCall(request)

            val response=call.execute()

            val code=response.code

            val result=Result(code,r)

            if (code==200){

                r=response.body!!.string()
                result.info=r

            }

            return result

        }

        fun getPager(url: String):Result{

            var r:String=""

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .sslSocketFactory(SSLUtil.createSSLSocketFactory()!!,SSLUtil.MyTrustManager())
                .hostnameVerifier(SSLUtil.getHostnameVerifier()!!)
                .build()


            val request=Request.Builder()
                .get()
                .url(url)
                .build()

            val call=httpClient.newCall(request)

            val response=call.execute()

            val code=response.code

            val result=Result(code,r)

            if (code==200){

                r=response.body!!.string()
                result.info=r

            }

            return result

        }


        public fun getSearchHtml(url: String):Result{

            val httpClient=OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .sslSocketFactory(SSLUtil.createSSLSocketFactory()!!,SSLUtil.MyTrustManager())
                .hostnameVerifier(SSLUtil.getHostnameVerifier()!!)
                .build()


            val request=Request.Builder()
                .get()
                .url(url)
                .build()


            var r=""


            val call=httpClient.newCall(request)

            val response=call.execute()

            val code=response.code

            val result=Result(code,r)

//            Log.d("rrr--->>>",url)

            if (code==200){

                r=response.body!!.string()
                result.info=r

            }

            return result

        }


    }



}