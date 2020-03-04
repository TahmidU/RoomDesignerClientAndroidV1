package com.tahmidu.room_designer_client_android.repository.database.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.Model;

public class ItemWithModel
{
    @Embedded
    public Item item;
    @Relation(parentColumn = "itemId",entityColumn = "itemId")
    public Model model;
}
