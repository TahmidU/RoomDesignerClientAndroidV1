package com.tahmidu.room_designer_client_android.model;

import java.util.Date;
import java.util.List;

public class Item
{
    private long itemId;
    private String name;
    private String description;
    private boolean hasModel;
    private Date date;
    private long model;
    private long category;
    private long type;
    private long user;
    private long itemVariant;
    private List<Long> images;

    public Item() {
    }

    public Item(String name, String description, boolean hasModel, Date date) {
        this.name = name;
        this.description = description;
        this.hasModel = hasModel;
        this.date = date;
    }

    public Item(long itemId, String name, String description, boolean hasModel, Date date) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.hasModel = hasModel;
        this.date = date;
    }

    public Item(long itemId, String name, String description, boolean hasModel, Date date, long model,
                long user, long category, long itemVariant, long type, List<Long> images) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.hasModel = hasModel;
        this.date = date;
        this.model = model;
        this.user = user;
        this.category = category;
        this.itemVariant = itemVariant;
        this.type = type;
        this.images = images;
    }

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHasModel() {
        return hasModel;
    }

    public long getModel() {
        return model;
    }

    public void setModel(long model) {
        this.model = model;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public List<Long> getImages() {
        return images;
    }

}
