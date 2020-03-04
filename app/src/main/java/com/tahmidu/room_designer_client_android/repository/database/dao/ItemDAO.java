package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Item;

import java.util.List;

@Dao
public interface ItemDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItem(Item item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(List<Item> items);

    @Update
    public void updateItems(List<Item> items);

    @Delete
    public void deleteItem(Item item);
}
