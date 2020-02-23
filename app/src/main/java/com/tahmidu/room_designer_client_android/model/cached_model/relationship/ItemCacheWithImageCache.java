package com.tahmidu.room_designer_client_android.model.cached_model.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tahmidu.room_designer_client_android.model.cached_model.ImageCached;
import com.tahmidu.room_designer_client_android.model.cached_model.ItemCached;

import java.util.List;

public class ItemCacheWithImageCache
{
    @Embedded public ItemCached itemCached;
    @Relation(parentColumn = "itemId",entityColumn = "cachedItemId")
    public List<ImageCached> cachedImages;
}
