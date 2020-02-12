package com.tahmidu.room_designer_client_android.network;

import com.tahmidu.room_designer_client_android.repository.SignUpRepo;

public class NetworkState
{
    private static NetworkState instance;
    private static Status status;

    public static NetworkState getInstance() {
        if(instance == null)
        {
            status = Status.DONE;
            return instance = new NetworkState();
        }

        return instance;
    }

    public static void setStatus(Status status) {
        NetworkState.status = status;
    }

    public static Status getStatus()
    {
        return status;
    }
}
