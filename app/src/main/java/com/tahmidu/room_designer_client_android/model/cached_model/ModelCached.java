package com.tahmidu.room_designer_client_android.model.cached_model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ModelCached
{
    @PrimaryKey private long modelId;
    private String uri;
    private long cachedItemId;

    public ModelCached(long modelId, String uri, long cachedItemId) {
        this.modelId = modelId;
        this.uri = uri;
        this.cachedItemId = cachedItemId;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getCachedItemId() {
        return cachedItemId;
    }

    public void setCachedItemId(long cachedItemId) {
        this.cachedItemId = cachedItemId;
    }
}
