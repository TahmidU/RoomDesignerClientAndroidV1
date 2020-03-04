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

    public void loadModel(Anchor anchor, String dir)
    {
        if(owner.get() == null)
        {
            Log.d(TAG, "Fragment/Activity is null. Cannot load model.");
            return;
        }
        ModelRenderable.builder()
                .setSource(owner.get(), RenderableSource.builder()
                        .setSource(owner.get(),Uri.fromFile(new File(dir)), RenderableSource.SourceType.GLTF2)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()).setRegistryId(Uri.fromFile(new File(dir)))
                .build()
                .thenAccept(modelRenderable -> owner.get().addNodeToScene(anchor, modelRenderable))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(owner.get());
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });
/*                .handle((modelRenderable, throwable) ->
                {
                    ARActivity activity = owner.get();
                    if(activity == null )
                        return null;
                    else if(throwable != null)
                        activity.onException(throwable);
                    else
                        activity.addNodeToScene(anchor, modelRenderable);
                    return null;
                });*/
    }
}
