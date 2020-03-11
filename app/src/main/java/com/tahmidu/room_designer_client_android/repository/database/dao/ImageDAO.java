package com.tahmidu.room_designer_client_android.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.tahmidu.room_designer_client_android.model.Image;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ImageDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertImage(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertImages(List<Image> images);

    @Update
    Completable updateImages(List<Image> images);

    @Delete
    Completable deleteImage(Image image);

    @Query("SELECT * FROM Image")
    Flowable<Image> getAllImages();

    @Query("SELECT * FROM Image WHERE imageId = :imageId")
    Flowable<Image> getModelById(Long imageId);
}
