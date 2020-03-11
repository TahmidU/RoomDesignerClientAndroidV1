package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Model;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ModelDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertModel(Model model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertModels(List<Model> models);

    @Update
    Completable updateModels(List<Model> models);

    @Delete
    Completable deleteModel(Model model);

    @Query("SELECT * FROM Model")
    Flowable<Model> getAllModel();

    @Query("SELECT * FROM Model WHERE modelId = :modelId")
    Flowable<Model> getModelById(Long modelId);
}
