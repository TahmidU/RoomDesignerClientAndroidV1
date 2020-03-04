package com.tahmidu.room_designer_client_android.util;

import java.io.File;

public class CustomFileUtil
{
    public static String getExtension(String dir)
    {
        File file = new File(dir);
        return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
    }
}
