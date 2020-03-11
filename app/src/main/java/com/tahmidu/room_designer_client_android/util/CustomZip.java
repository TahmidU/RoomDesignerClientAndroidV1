package com.tahmidu.room_designer_client_android.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.Observable;

public class CustomZip
{
    private static final String TAG = "CUSTOM ZIP";

    public static Observable<Boolean> unzip(byte[] buffer, String zipName, String outputPath)
    {
        Log.d(TAG, Thread.currentThread().getName());

        if(buffer != null)
            Log.d("CUSTOM ZIP", "Buffer Length: "+buffer.length);
        else
            Log.d("CUSTOM ZIP", "Buffer is null");

        Log.d(TAG, outputPath);

        boolean dirResult = false;
        File destDir = new File(outputPath);
        if (!destDir.exists()) {
            dirResult = destDir.mkdirs();
        }
        Log.d(TAG, "Make Dir Result: " + dirResult + " And does dir exist: " + destDir.exists());

        ZipInputStream zipInputStream;

        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry zipEntry;

            while((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                Log.d(TAG, "Unzipping: "+zipEntry.getName());

                FileOutputStream fileOutputStream =
                        new FileOutputStream(outputPath+ "/" + zipEntry.getName());

                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                for(int length = zipInputStream.read(); length != -1; length = zipInputStream.read())
                    bufferedOutputStream.write(length);


                bufferedOutputStream.close();
                fileOutputStream.close();
                zipInputStream.closeEntry();
            }
            zipInputStream.close();

        }catch (IOException e)
        {
            e.printStackTrace();
            return Observable.just(false);
        }
        return Observable.just(true);
    }
}
