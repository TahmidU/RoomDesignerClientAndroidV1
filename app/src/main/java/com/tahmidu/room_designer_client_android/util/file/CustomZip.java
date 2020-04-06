package com.tahmidu.room_designer_client_android.util.file;

import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
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

        String texturePath = "/textures";

        boolean dirResult = false;
        File destDir = new File(outputPath);
        if (!destDir.exists()) {
            dirResult = destDir.mkdirs();
        }

        boolean textureResult = false;
        File textureDir = new File(outputPath+texturePath);
        if(!textureDir.exists())
            textureResult = textureDir.mkdir();

        Log.d(TAG, "Files destination created? " + dirResult);
        Log.d(TAG, "Textures destination created? " + textureResult);

        ZipInputStream zipInputStream;

        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry zipEntry;

            //Extract and sort files in proper location.
            while((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                Log.d(TAG, "Unzipping: "+zipEntry.getName());
                String fileExtension = CustomFileUtil.getExtension(zipEntry.getName());

                FileOutputStream fileOutputStream;

                //Check if its a texture.
                String path = "";
                if(fileExtension.equals(".png") || fileExtension.equals(".jpg")
                        || fileExtension.equals(".jpeg") || fileExtension.equals(".bmp")
                        || fileExtension.equals(".tif"))
                {
                    path = outputPath + texturePath + "/" + zipEntry.getName();
                    Log.d(TAG, "Extracting to" + path);
                    fileOutputStream = new FileOutputStream(path);
                }else
                    {
                        path = outputPath+ "/" + zipEntry.getName();
                        Log.d(TAG, "Extracting to" + path);
                        fileOutputStream = new FileOutputStream(path);
                    }

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
