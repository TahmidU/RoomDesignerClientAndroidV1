package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Model;
import com.tahmidu.room_designer_client_android.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface UserDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUsers(List<User> users);

    @Update
    Completable updateUsers(List<User> users);

    @Delete
    Completable deleteUser(User user);

    @Query("SELECT * FROM User")
    Flowable<User> getAllUsers();

    @Query("SELECT * FROM User WHERE userId = :userId")
    Flowable<User> getUserById(Long userId);

}
