package com.tahmidu.room_designer_client_android.model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.tahmidu.room_designer_client_android.repository.database.DataConverter;
import java.util.Date;
import java.util.List;

@Entity
public class Item
{
    @PrimaryKey private long itemId;
    private String name;
    private String description;
    private boolean hasModel;
    @TypeConverters(DataConverter.class)
    private Date date;

    @Ignore
    private long model;
    @Ignore
    private long category;
    @Ignore
    private long type;
    @Ignore
    private long user;
    @Ignore
    private long itemVariant;
    @Ignore
    private List<Long> images;
    @Ignore
    private List<Long> itemDownloads;
    @Ignore
    private List<Long> itemViews;
    @Ignore
    private List<Long> favourites;
    @Ignore
    private transient Bitmap thumbnail;

    public Item(long itemId, String name, String description, boolean hasModel, Date date) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.hasModel = hasModel;
        this.date = date;
    }

    @Ignore
    public Item(long itemId, String name, String description, boolean hasModel, Date date, long model,
                long category, long type, Bitmap thumbnail) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.hasModel = hasModel;
        this.date = date;
        this.model = model;
        this.category = category;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    @Ignore
    public Item(long itemId, String name, String description, boolean hasModel, Date date, long model,
                long user, long category, long itemVariant, long type, List<Long> images,
                List<Long> itemDownloads, List<Long> itemViews, List<Long> favourites) {
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
        this.itemDownloads = itemDownloads;
        this.itemViews = itemViews;
        this.favourites = favourites;
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

    public boolean isHasModel() {
        return hasModel;
    }

    public void setHasModel(boolean hasModel) {
        this.hasModel = hasModel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Ignore
    public long getModel() {
        return model;
    }

    @Ignore
    public void setModel(long model) {
        this.model = model;
    }

    @Ignore
    public long getUser() {
        return user;
    }

    @Ignore
    public void setUser(long user) {
        this.user = user;
    }

    @Ignore
    public long getCategory() {
        return category;
    }

    @Ignore
    public void setCategory(long category) {
        this.category = category;
    }

    @Ignore
    public long getItemVariant() {
        return itemVariant;
    }

    @Ignore
    public void setItemVariant(long itemVariant) {
        this.itemVariant = itemVariant;
    }

    @Ignore
    public long getType() {
        return type;
    }

    @Ignore
    public void setType(long type) {
        this.type = type;
    }

    @Ignore
    public List<Long> getImages() {
        return images;
    }

    @Ignore
    public void setImages(List<Long> images) {
        this.images = images;
    }

    @Ignore
    public List<Long> getItemDownloads() {
        return itemDownloads;
    }

    @Ignore
    public void setItemDownloads(List<Long> itemDownloads) {
        this.itemDownloads = itemDownloads;
    }

    @Ignore
    public List<Long> getItemViews() {
        return itemViews;
    }

    @Ignore
    public void setItemViews(List<Long> itemViews) {
        this.itemViews = itemViews;
    }

    @Ignore
    public List<Long> getFavourites() {
        return favourites;
    }

    @Ignore
    public void setFavourites(List<Long> favourites) {
        this.favourites = favourites;
    }

    @Ignore
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Ignore
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
