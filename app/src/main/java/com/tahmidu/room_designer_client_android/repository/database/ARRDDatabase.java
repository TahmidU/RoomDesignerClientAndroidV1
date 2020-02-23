package com.tahmidu.room_designer_client_android.repository.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tahmidu.room_designer_client_android.model.Item;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class ARRDDatabase extends RoomDatabase
{}
