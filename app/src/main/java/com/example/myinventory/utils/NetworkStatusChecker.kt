package com.example.myinventory.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.snackbar.Snackbar

class NetworkStatusChecker(private val context: Context, private val view: View) {

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var snackBar: Snackbar? = null
    var isInternetAvailable: Boolean = true

    fun register() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo?.isConnectedOrConnecting == true) {
            // Internet connection is available
            isInternetAvailable = true
            snackBar?.dismiss()
        } else {
            // Internet connection is not available
            isInternetAvailable = false
            showSnackbar()
        }

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Internet connection is available\
                isInternetAvailable = true
                snackBar?.dismiss()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                isInternetAvailable = false
                showSnackbar()
            }

            override fun onLost(network: Network) {
                isInternetAvailable = false
                showSnackbar()
            }

            override fun onUnavailable() {
                isInternetAvailable = false
                showSnackbar()
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback!!)
    }

    fun unregister() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback!!)
    }

    private fun showSnackbar() {
        Handler(Looper.getMainLooper()).post {
            snackBar = Snackbar.make(
                view,
                "No internet connection",
                Snackbar.LENGTH_INDEFINITE
            )
            snackBar?.show()
        }
    }
}
