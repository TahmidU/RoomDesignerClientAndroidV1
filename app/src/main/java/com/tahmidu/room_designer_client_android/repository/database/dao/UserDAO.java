package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Model;
import com.tahmidu.room_designer_client_android.model.User;

import java.util.List;

@Dao
public interface UserDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Update
    void updateModels(List<User> users);

    @Delete
    void deleteModel(User user);
}
