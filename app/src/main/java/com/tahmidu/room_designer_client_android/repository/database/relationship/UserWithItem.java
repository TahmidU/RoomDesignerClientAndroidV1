package com.tahmidu.room_designer_client_android.repository.database.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.User;

import java.util.List;

public class UserWithItem
{
    @Embedded
    public User user;
    @Relation(parentColumn = "userId", entityColumn = "user")
    public List<Item> items;
}
