package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Model;

import java.util.List;

@Dao
public interface ModelCachedDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertModel(Model model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertModels(List<Model> model);

    @Update
    public void updateModels(List<Model> model);

    @Delete
    public void deleteModel(Model model);
}
