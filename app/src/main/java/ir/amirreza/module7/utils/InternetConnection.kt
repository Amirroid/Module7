package ir.amirreza.module7.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun checkInternet(context: Context, scope: CoroutineScope) = callbackFlow {
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val callback = object : NetworkCallback() {
        override fun onLost(network: Network) {
            scope.launch {
                send(false)
            }
            super.onLost(network)
        }

        override fun onUnavailable() {
            scope.launch {
                send(false)
            }
            super.onUnavailable()
        }

        override fun onAvailable(network: Network) {
            scope.launch {
                send(true)
            }
            super.onAvailable(network)
        }
    }
    connectivityManager.registerDefaultNetworkCallback(callback)
    this.awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}