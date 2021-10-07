package com.bd.mystudy.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

// write : BrightDragon
// LifecycleObserver를 이용하여 네트워크 체크

class NetworkCheckMonitor(private val activity: AppCompatActivity): ConnectivityManager.NetworkCallback(), LifecycleObserver {

    private val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
    private var network: Network? = null

    init {
        network = connectivityManager.activeNetwork
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun connected(){
        network?.let {
            Log.d("##", "NetworkCheckMonitor >>> network 연결 O")
            check(activity.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
            connectivityManager.registerNetworkCallback(networkRequest, this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disconnected(){
        Log.d("##", "NetworkCheckMonitor >>> disable")
        connectivityManager.unregisterNetworkCallback(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d("##", "NetworkCheckMonitor >>> onAvailable ")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.d("##", "NetworkCheckMonitor >>> onLost ")
    }
}