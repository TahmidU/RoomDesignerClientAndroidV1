package com.tahmidu.room_designer_client_android.model.cached_model.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tahmidu.room_designer_client_android.model.cached_model.ItemCached;
import com.tahmidu.room_designer_client_android.model.cached_model.ModelCached;

import java.util.List;

public class ItemCacheWithModelCache
{
    @Embedded
    public ItemCached itemCached;
    @Relation(parentColumn = "itemId",entityColumn = "cachedItemId")
    public List<ModelCached> cachedModels;
}
