package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Item;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ItemDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertItem(Item item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertItems(List<Item> items);

    @Update
    Completable updateItems(List<Item> items);

    @Delete
    Completable deleteItem(Item item);

    @Query("SELECT * FROM Item")
    Flowable<Item> getAllItem();

    @Query("SELECT * FROM Item WHERE itemId = :itemId")
    Flowable<Item> getItemById(Long itemId);
}
