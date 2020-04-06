package com.tahmidu.room_designer_client_android.model;


public class Image
{
    private long imageId;
    private String uri;
    private boolean isThumbnail;
    private long itemId;

    public Image() {
    }

    public Image(long imageId, String uri, boolean isThumbnail, long itemId) {
        this.imageId = imageId;
        this.uri = uri;
        this.isThumbnail = isThumbnail;
        this.itemId = itemId;
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

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
