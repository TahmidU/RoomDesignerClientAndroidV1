package com.tahmidu.room_designer_client_android.model;

import android.graphics.Bitmap;

public class GalleryItem
{
    private Item item;
    private String modelDir;

    public GalleryItem(){}

    public GalleryItem(Item item, String modelDir) {
        this.item = item;
        this.modelDir = modelDir;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getModelDir() {
        return modelDir;
    }

    public void setModelDir(String modelDir) {
        this.modelDir = modelDir;
    }

    @Override
    public String toString() {
        return "GalleryItem{" +
                "itemId=" + item.getItemId() +
                ", modelDir='" + modelDir + '\'' +
                '}';
    }
}
