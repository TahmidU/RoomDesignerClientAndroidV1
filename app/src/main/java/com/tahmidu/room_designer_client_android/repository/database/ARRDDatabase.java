package com.tahmidu.room_designer_client_android.repository.database;

import android.content.Context;
import android.view.Display;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tahmidu.room_designer_client_android.model.Image;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.Model;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.repository.database.dao.ImageDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.ItemDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.ModelDAO;
import com.tahmidu.room_designer_client_android.repository.database.dao.UserDAO;

@Database(entities = {Item.class, Image.class, Model.class, User.class}, version = 1, exportSchema = false)
public abstract class ARRDDatabase extends RoomDatabase
{
    public abstract ImageDAO imageDAO();
    public abstract ItemDAO itemDAO();
    public abstract ModelDAO modelDAO();
    public abstract UserDAO userDAO();

    private static ARRDDatabase INSTANCE;

    public static ARRDDatabase getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context,
                    ARRDDatabase.class, "aard_db").build();
        }
        return INSTANCE;
    }
}
