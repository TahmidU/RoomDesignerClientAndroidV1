package com.tahmidu.room_designer_client_android.model;


public class Model
{
    private long modelId;
    private String uri;
    private long ItemId;

    public Model() {
    }

    public Model(long modelId, String uri, long itemId) {
        this.modelId = modelId;
        this.uri = uri;
        this.ItemId = itemId;
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

    public long getItemId() {
        return ItemId;
    }

    public void setItemId(long itemId) {
        ItemId = itemId;
    }
}
