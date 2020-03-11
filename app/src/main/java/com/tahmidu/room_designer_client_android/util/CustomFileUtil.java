package com.tahmidu.room_designer_client_android.util;

import android.os.FileUtils;
import android.util.Log;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class CustomFileUtil
{
    private static final String TAG = "CUSTOM FILE UTIL";

    public static String getExtension(String dir)
    {
        File file = new File(dir);
        return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
    }

    public static Completable deleteDir(String parentDir)
    {
        return Completable.fromRunnable(() -> {
            File file = new File(parentDir);
            if(file.isDirectory())
            {
                if(file.listFiles() != null)
                    for(File f: file.listFiles())
                        deleteChildren(f.getAbsolutePath());
            }
        });
    }

    private static void deleteChildren(String dir)
    {
        Log.d(TAG, "Deleting: " + dir);
        File file = new File(dir);
        if(file.isDirectory())
        {
            if(file.listFiles() != null)
                for(File f: file.listFiles())
                    deleteChildren(f.getAbsolutePath());
        }
        file.delete();
    }
}
