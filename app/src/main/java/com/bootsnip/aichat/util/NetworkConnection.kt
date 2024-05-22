package com.bootsnip.aichat.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.InetAddress

class NetworkConnection(context: Context) {

    private val applicationContext = context.applicationContext

    fun isNetworkAvailable(): Boolean {
        val result: Boolean
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

        return result
    }

    fun isInternetAvailable(): Boolean {
        return try {
            if (isNetworkAvailable()) {
                val ipAddress: InetAddress = InetAddress.getByName("google.com")
                !ipAddress.equals("")
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}