package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.tahmidu.room_designer_client_android.model.cached_model.ImageCached;
import com.tahmidu.room_designer_client_android.model.cached_model.ItemCached;

import java.util.List;

@Dao
public interface ImageCachedDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertImage(ImageCached imageCached);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertImages(List<ImageCached> imageCaches);

    @Update
    public void updateImages(List<ImageCached> imageCaches);

    @Delete
    public void deleteImage(ImageCached imageCached);
}
