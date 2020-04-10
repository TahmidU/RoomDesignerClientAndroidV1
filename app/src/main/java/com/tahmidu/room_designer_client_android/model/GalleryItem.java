package com.tahmidu.room_designer_client_android.model;

import androidx.annotation.NonNull;

public class GalleryItem
{
    private Item item;
    private String modelDir;
    private String textureDir;

    public GalleryItem(Item item) {
        this.item = item;
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

    public String getTextureDir() {
        return textureDir;
    }


    public void setTextureDir(String textureDir) {
        this.textureDir = textureDir;
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
