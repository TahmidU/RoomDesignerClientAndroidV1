package com.tahmidu.room_designer_client_android.model.cached_model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImageCached
{
    @PrimaryKey private long imageId;
    private String uri;
    private boolean isThumbnail;
    private long cachedItemId;

    public ImageCached(long imageId, String uri, boolean isThumbnail, long cachedItemId) {
        this.imageId = imageId;
        this.uri = uri;
        this.isThumbnail = isThumbnail;
        this.cachedItemId = cachedItemId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public long getCachedItemId() {
        return cachedItemId;
    }

    public void setCachedItemId(long cachedItemId) {
        this.cachedItemId = cachedItemId;
    }
}
