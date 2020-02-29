package com.tahmidu.room_designer_client_android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.tahmidu.room_designer_client_android.R;

public class ARActivity extends AppCompatActivity
{
    private ArFragment arFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) ->
        {
            placeModel(hitResult.createAnchor());
        });
    }

    private void placeModel(Anchor anchor)
    {
        ModelRenderable
                .builder()
                .setSource(this, RenderableSource
                        .builder()
                        .setSource(this, Uri.parse(""), RenderableSource.SourceType.GLTF2)
                        .setScale(0.75f)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()).setRegistryId("").build()
                .thenAccept(modelRenderable -> addNoteToScene(modelRenderable, anchor))
                .exceptionally(throwable ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });
    }

    private void addNoteToScene(ModelRenderable modelRenderable, Anchor anchor)
    {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}
