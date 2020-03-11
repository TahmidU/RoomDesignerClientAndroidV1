package com.tahmidu.room_designer_client_android.repository.database;

import android.content.Context;
import android.util.Log;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.repository.database.dao.ImageDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.ItemDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.ModelDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.UserDAO;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseConnector
{
    private final String TAG = "DATABASE CONNECTOR";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private ImageDAO imageDAO;
    private ModelDAO modelDAO;

    public DatabaseConnector(Context context)
    {
        userDAO = ARRDDatabase.getInstance(context).userDAO();
        itemDAO = ARRDDatabase.getInstance(context).itemDAO();
        imageDAO = ARRDDatabase.getInstance(context).imageDAO();
        modelDAO = ARRDDatabase.getInstance(context).modelDAO();
    }

    public void insertUser(User user)
    {
       Log.d(TAG, "Insert User: " + user.toString());
       compositeDisposable.add(userDAO.insertUser(user)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    public void insertItem(Item item)
    {
/*        Log.d(TAG, "Insert Item: " + item.toString());
        compositeDisposable.add(userDAO.insertItem(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe());*/
    }

    public void deleteUser(User user)
    {
        Log.d(TAG, "Delete User: " + user.toString());
        compositeDisposable.add(userDAO.deleteUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe());
    }
}
