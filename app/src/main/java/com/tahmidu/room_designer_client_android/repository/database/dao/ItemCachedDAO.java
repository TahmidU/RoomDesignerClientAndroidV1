package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.cached_model.ItemCached;

import java.util.List;

@Dao
public interface ItemCachedDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItem(ItemCached itemCached);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(List<ItemCached> itemCaches);

    @Update
    public void updateItems(List<ItemCached> itemCaches);

    @Delete
    public void deleteItem(ItemCached itemCached);
}
