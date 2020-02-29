package com.tahmidu.room_designer_client_android.repository.database.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.tahmidu.room_designer_client_android.model.Image;
import com.tahmidu.room_designer_client_android.model.Item;


import java.util.List;

public class ItemWithImage
{
    @Embedded public Item item;
    @Relation(parentColumn = "itemId",entityColumn = "itemId")
    public List<Image> images;
}
