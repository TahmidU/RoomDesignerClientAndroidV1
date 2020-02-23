package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.cached_model.ItemCached;
import com.tahmidu.room_designer_client_android.model.cached_model.ModelCached;

import java.util.List;

@Dao
public interface ModelCachedDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertModel(ModelCached modelCached);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertModels(List<ModelCached> modelCaches);

    @Update
    public void updateModels(List<ModelCached> modelCaches);

    @Delete
    public void deleteModel(ModelCached modelCached);
}
