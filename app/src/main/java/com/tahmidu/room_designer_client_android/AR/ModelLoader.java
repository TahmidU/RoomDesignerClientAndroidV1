package com.tahmidu.room_designer_client_android.AR;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Texture;
import com.tahmidu.room_designer_client_android.activity.ARActivity;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

/**
 * Loads model from a directory.
 */
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
     * @param modalDir
     */
    public void loadModel(Anchor anchor, String modalDir, String textureDir, Context context)
    {
        Uri uriLocation = Uri.fromFile(new File(modalDir));
        CompletableFuture<Texture> futureTexture = Texture.builder().setSource(context,
                Uri.fromFile(new File(textureDir))).build();
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
                .thenAcceptBoth(futureTexture, ((modelRenderable, texture) ->
                {
                    modelRenderable.getMaterial().setTexture(MaterialFactory.MATERIAL_TEXTURE, texture);
                    owner.get().addNodeToScene(anchor, modelRenderable);
                }))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(owner.get());
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });
    }
}
