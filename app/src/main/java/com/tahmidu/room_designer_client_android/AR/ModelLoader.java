package com.tahmidu.room_designer_client_android.AR;

import android.app.AlertDialog;
import android.net.Uri;
import android.util.Log;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.tahmidu.room_designer_client_android.activity.ARActivity;

import java.io.File;
import java.lang.ref.WeakReference;

public class ModelLoader
{
    private final WeakReference<ARActivity> owner;
    private static final String TAG = "ModelLoader";

    public ModelLoader(WeakReference<ARActivity> owner) {
        this.owner = owner;
    }

    /**
     * Load the model into scene by accessing the models directory.
     * @param anchor
     * @param dir
     */
    public void loadModel(Anchor anchor, String dir)
    {
        Uri uriLocation = Uri.fromFile(new File(dir));
        Log.d(TAG, uriLocation.getPath());
        if(owner.get() == null)
        {
            Log.d(TAG, "Fragment/Activity is null. Cannot load model.");
            return;
        }
        ModelRenderable.builder()
                .setSource(owner.get(), RenderableSource.builder()
                        .setSource(owner.get(),uriLocation, RenderableSource.SourceType.GLTF2)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()).setRegistryId(uriLocation)
                .build()
                .thenAccept(modelRenderable -> owner.get().addNodeToScene(anchor, modelRenderable))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(owner.get());
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });
    }
}
