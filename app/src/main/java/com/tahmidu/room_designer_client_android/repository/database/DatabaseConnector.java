package com.tahmidu.room_designer_client_android.repository.database;

import android.content.Context;
import android.util.Log;

import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.repository.database.dao.UserDAO;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseConnector
{
    private final String TAG = "DATABASE CONNECTOR";

    private UserDAO userDAO;

    public DatabaseConnector(Context context)
    {
        ARRDDatabase.getInstance(context).userDAO();
    }

    public void insertUser(User user)
    {
        Completable.fromRunnable(() -> userDAO.insertUser(user))
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }
}
