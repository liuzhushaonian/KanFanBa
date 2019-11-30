package com.app.legend.kanfanba.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.sunchen.netbus.NetStatusBus
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class KanApp: Application() {



    override fun onCreate() {
        super.onCreate()

        context=applicationContext
        NetStatusBus.getInstance().init(this);


        trustAllHosts()

    }


    companion object{

        lateinit var context: Context

    }

    private fun trustAllHosts(){

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }


                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }


                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }
            })

        val sc: SSLContext = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection
            .setDefaultSSLSocketFactory(sc.getSocketFactory())

    }






}