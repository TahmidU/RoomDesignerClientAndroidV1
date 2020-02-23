package com.tahmidu.room_designer_client_android.model.cached_model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tahmidu.room_designer_client_android.repository.database.DataConverter;

import java.util.Date;

@Entity
public class ItemCached
{
    @PrimaryKey private long itemId;
    private String name;
    private String description;
    @TypeConverters(DataConverter.class)
    private Date date;
    private boolean has_model;

    public ItemCached(long itemId, String name, String description, Date date, boolean has_model) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.has_model = has_model;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHas_model() {
        return has_model;
    }

    public void setHas_model(boolean has_model) {
        this.has_model = has_model;
    }
}
