package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Model;

import java.util.List;

@Dao
public interface ModelDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Model model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModels(List<Model> models);

    @Update
    void updateModels(List<Model> models);

    @Delete
    void deleteModel(Model model);
}
