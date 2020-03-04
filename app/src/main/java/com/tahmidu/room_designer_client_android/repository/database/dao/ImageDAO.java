package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Image;

import java.util.List;

@Dao
public interface ImageDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(List<Image> images);

    @Update
    void updateImages(List<Image> images);

    @Delete
    void deleteImage(Image image);
}
