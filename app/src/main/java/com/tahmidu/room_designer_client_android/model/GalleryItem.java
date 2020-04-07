package com.tahmidu.room_designer_client_android.model;

import androidx.annotation.NonNull;

public class GalleryItem
{
    private Item item;
    private String modelDir;

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

    @NonNull
    @Override
    public String toString() {
        return "GalleryItem{" +
                "itemId=" + item.getItemId() +
                ", modelDir='" + modelDir + '\'' +
                '}';
    }
}
