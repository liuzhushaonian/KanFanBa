package com.app.legend.kanfanba.utils

import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class SSLUtil {

    companion object{

        public fun createSSLSocketFactory(): SSLSocketFactory? {
            var ssfFactory: SSLSocketFactory? = null
            try {
                val mMyTrustManager = MyTrustManager()
                val sc: SSLContext = SSLContext.getInstance("TLS")
                sc.init(null, arrayOf<TrustManager>(mMyTrustManager), SecureRandom())
                ssfFactory = sc.getSocketFactory()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
            return ssfFactory
        }

        fun getHostnameVerifier(): HostnameVerifier? {
            return object : HostnameVerifier {

                override fun verify(s: String?, sslSession: SSLSession?): Boolean {
                    return true
                }
            }
        }

    }




    public class MyTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }


}