package com.tahmidu.room_designer_client_android.repository.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataConverter
{
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
