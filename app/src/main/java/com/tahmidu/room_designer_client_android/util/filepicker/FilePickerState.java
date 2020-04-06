package com.tahmidu.room_designer_client_android.util.filepicker;

import android.util.Log;

public class FilePickerState
{
    private final String TAG = "FILE PICKER STATE";

    private static FilePickerState instance;
    private static FilePickerStatus filePickerStatus;

    public static FilePickerState getInstance() {
        if(instance == null)
        {
            filePickerStatus = FilePickerStatus.NONE;
            return instance = new FilePickerState();
        }

        return instance;
    }

    public void setStatus(FilePickerStatus filePickerStatus) {
        Log.d(TAG, "Now set to: " + filePickerStatus.toString());
        FilePickerState.filePickerStatus = filePickerStatus;
    }

    public FilePickerStatus getStatus()
    {
        return filePickerStatus;
    }
}
