package com.tahmidu.room_designer_client_android.network;

import android.util.Log;

/**
 * Monitor the state of the network.
 */
public class NetworkState
{
    private final String TAG = "NETWORK_STATE";

    private static NetworkState instance;
    private static NetworkStatus networkStatus;

    public static NetworkState getInstance() {
        if(instance == null)
        {
            networkStatus = NetworkStatus.DONE;
            return instance = new NetworkState();
        }

        return instance;
    }

    public void setStatus(NetworkStatus networkStatus) {
        Log.d(TAG, "Now set to: " + networkStatus.toString());
        NetworkState.networkStatus = networkStatus;
    }

    public NetworkStatus getStatus()
    {
        return networkStatus;
    }
}
