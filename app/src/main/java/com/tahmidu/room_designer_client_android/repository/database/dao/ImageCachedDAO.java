package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.Image;

import java.util.List;

@Dao
public interface ImageCachedDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertImage(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertImages(List<Image> images);

    @Update
    public void updateImages(List<Image> images);

    @Delete
    public void deleteImage(Image image);
}
