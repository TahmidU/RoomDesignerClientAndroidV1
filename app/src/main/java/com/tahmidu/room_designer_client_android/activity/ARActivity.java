package com.tahmidu.room_designer_client_android.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.tahmidu.room_designer_client_android.AR.ModelLoader;
import com.tahmidu.room_designer_client_android.AR.PointerDrawable;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.adapter.MainRecyclerAdapter;
import com.tahmidu.room_designer_client_android.databinding.ActivityArBinding;
import com.tahmidu.room_designer_client_android.view_model.ARViewModel;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ARActivity extends AppCompatActivity
{
    private final static String TAG = "AR_ACTIVITY";

    private ARViewModel arViewModel;

    private ActivityArBinding binding;

    private PointerDrawable pointerDrawable = new PointerDrawable();
    private boolean isTracking = false;
    private boolean isHitting;
    private boolean downloadState = false;

    private ModelLoader modelLoader;

    private ArFragment arFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ar);

        Log.d(TAG, "OnCreate Called");

        //View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication());
        arViewModel = new ViewModelProvider(this, factory)
                .get(ARViewModel.class);

        binding.setVM(arViewModel);
        binding.setLifecycleOwner(this);

        if(getIntent().getExtras() != null)
            downloadState = getIntent().getExtras().getBoolean("download");
        Log.d(TAG, "Download State: " + downloadState);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        if (arFragment != null) {
            arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime ->
            {
                arFragment.onUpdate(frameTime);
                onUpdate();
            });
        }

        modelLoader = new ModelLoader(new WeakReference<>(this));

        arViewModel.getIsTracking().observe(this, aBoolean -> {
            if(downloadState)
                arViewModel.fetchModel();
        });

        arViewModel.getArModelDirLiveData().observe(this, this::addObject);
    }

    private void onUpdate()
    {
        boolean trackingChanged = updateTracking();
        View contentView = findViewById(android.R.id.content);



        if(trackingChanged)
        {
            if(isTracking)
                contentView.getOverlay().add(pointerDrawable);
            else
                contentView.getOverlay().remove(pointerDrawable);

            contentView.invalidate();
        }

        if(isTracking)
        {
            boolean hitTestChanged = updateHitTest();
            if(hitTestChanged)
            {
                pointerDrawable.setEnabled(isHitting);
                contentView.invalidate();
            }

        }
    }

    private boolean updateHitTest() {
        Frame frame = arFragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private boolean updateTracking()
    {
        Frame frame =  arFragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING;

        if(isTracking)
            arViewModel.setIsTracking(true);

        return isTracking != wasTracking;
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }
/*
    private void initializeGallary()
    {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

    }

    private void addToGallary(String dir)
    {
        //LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(android.R.drawable.ic_menu_today);
        imageView.setOnClickListener(view -> addObject(dir));
        Log.d(TAG, Uri.parse(dir).toString());

        //gallery.addView(imageView);
    }*/

    private void addObject(String dir) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            Log.d(TAG, "Frame is not null");
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    modelLoader.loadModel(hit.createAnchor(), dir);
                    break;

                }
            }
        }else
            Log.d(TAG, "Frame is null");
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    public void onException(Throwable throwable){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Ar Error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_ar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
