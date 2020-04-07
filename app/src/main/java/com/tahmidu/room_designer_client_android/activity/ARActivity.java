package com.tahmidu.room_designer_client_android.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.tahmidu.room_designer_client_android.AR.ModelLoader;
import com.tahmidu.room_designer_client_android.AR.PointerDrawable;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.adapter.GalleryRecyclerAdapter;
import com.tahmidu.room_designer_client_android.adapter.SubLibraryRecyclerAdapter;
import com.tahmidu.room_designer_client_android.constant.Type;
import com.tahmidu.room_designer_client_android.databinding.ActivityArBinding;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.view_model.ARViewModel;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.lang.ref.WeakReference;
import java.util.List;

public class ARActivity extends AppCompatActivity
{
    private final static String TAG = "AR_ACTIVITY";

    //View Model
    private ARViewModel arViewModel;

    //Activity Details
    private PointerDrawable pointerDrawable = new PointerDrawable();
    private boolean isTracking = false;
    private boolean isHitting;
    private boolean downloadState = false;
    private boolean isDeleteMode = false;

    //AR
    private ModelLoader modelLoader;
    private ArFragment arFragment;

    //Delete
    private View arBorder;
    private ImageButton deleteBtn;

    //Gallery
    private GalleryRecyclerAdapter galleryRecyclerAdapter;
    private ExpandableLayout galleyExpandableLayout;
    private ImageButton galleryBtn;

    //Library
    private ImageButton addModelBtn;
    private ExpandableLayout libraryExpandableLayout;
    private SubLibraryRecyclerAdapter libraryRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.tahmidu.room_designer_client_android.databinding.ActivityArBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ar);

        Log.d(TAG, "OnCreate Called");

        //Views
        deleteBtn = findViewById(R.id.delete_btn);
        arBorder = findViewById(R.id.ar_border);
        galleyExpandableLayout = findViewById(R.id.ar_expandable_gallery);
        galleryBtn = findViewById(R.id.gallery_btn);
        ImageButton galleryCancelBtn = findViewById(R.id.gallery_cancel_btn);
        RecyclerView galleryRecyclerView = findViewById(R.id.gallery_view);
        addModelBtn = findViewById(R.id.add_model_btn);
        libraryExpandableLayout = findViewById(R.id.ar_expandable_library);
        RecyclerView libraryRecyclerView = findViewById(R.id.library_sub_recycler_view);
        ImageButton libraryCancelBtn = findViewById(R.id.library_sub_cancel_btn);

        //Recycler View configuration
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,
                false));
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,
                false));

        galleryRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
        libraryRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));

        //View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication());
        arViewModel = new ViewModelProvider(this, factory)
                .get(ARViewModel.class);
        arViewModel.resetPage();

        binding.setVM(arViewModel);
        binding.setLifecycleOwner(this);

        //Activity configurations.
        galleyExpandableLayout.collapse();
        modelLoader = new ModelLoader(new WeakReference<>(this));

        //Does the activity need to immediately download an item after creation.
        if(getIntent().getExtras() != null)
            downloadState = getIntent().getExtras().getBoolean("download");
        Log.d(TAG, "Download State: " + downloadState);

        //Fetches
        arViewModel.fetchItems();
        if(downloadState)
            arViewModel.fetchModel();

        //AR session fragment setup
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        if (arFragment != null) {
            arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
                arFragment.onUpdate(frameTime);
                onUpdate();
            });
            arFragment.getArSceneView().getScene().addOnPeekTouchListener(this::handleSceneTouch);
        }

        //Observers
        arViewModel.getGalleryItemsLiveData().observe(this,
                galleryItems ->
                {
                    Log.d(TAG, "Gallery items live data has changed. Gallery Size "
                            + galleryItems.size() + " and Adapter Size " + galleryRecyclerAdapter.getItemCount());
                    galleryRecyclerAdapter.notifyDataSetChanged();
                });

        arViewModel.getItemsLiveData().observe(this, items ->
        {
            Log.d(TAG, "Gallery items live data has changed. Library Size "
                    + items.size() + " and Adapter Size " + libraryRecyclerAdapter.getItemCount());
            libraryRecyclerAdapter.notifyDataSetChanged();
        });

        //Listeners
        galleryBtn.setOnClickListener(v -> {
            if(!galleyExpandableLayout.isExpanded())
            {
                galleyExpandableLayout.expand();
                addModelBtn.setVisibility(View.GONE);
                galleryBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
            }

        });

        galleryCancelBtn.setOnClickListener(v -> {
            if(galleyExpandableLayout.isExpanded())
            {
                galleyExpandableLayout.collapse();
                addModelBtn.setVisibility(View.VISIBLE);
                galleryBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }

        });

        addModelBtn.setOnClickListener(v -> {
            if(!libraryExpandableLayout.isExpanded())
            {
                Log.d(TAG, "size of library: "+arViewModel.getItemsLiveData().getValue().size());
                libraryExpandableLayout.setVisibility(View.VISIBLE);
                libraryExpandableLayout.expand();
                addModelBtn.setVisibility(View.GONE);
                galleryBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
            }

        });

        libraryCancelBtn.setOnClickListener(v ->
        {
            if(libraryExpandableLayout.isExpanded())
            {
                libraryExpandableLayout.collapse();
                addModelBtn.setVisibility(View.VISIBLE);
                galleryBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }
        });

        deleteBtn.setOnClickListener(v ->
        {
            if(!isDeleteMode)
            {
                isDeleteMode = true;
                arBorder.setBackground(getDrawable(R.drawable.delete_border));
            }else
            {
                isDeleteMode = false;
                arBorder.setBackground(null);
            }
        });

        //Setup Adapter for RecyclerViews
        galleryRecyclerAdapter = new GalleryRecyclerAdapter(arViewModel.getGalleryItemsLiveData().getValue(),
                this, (view, position) -> {
                    if(arViewModel.getGalleryItemsLiveData().getValue() != null)
                        addObject(arViewModel.getGalleryItemsLiveData().getValue()
                                .get(position).getModelDir(),
                                arViewModel.getGalleryItemsLiveData()
                                        .getValue().get(position).getItem().getType());
                });
        galleryRecyclerView.setAdapter(galleryRecyclerAdapter);

        libraryRecyclerAdapter = new SubLibraryRecyclerAdapter(arViewModel.getItemsLiveData().getValue(),
                this, ((view, position) -> {
                    arViewModel.fetchModel(arViewModel.getItemsLiveData().getValue().get(position));
        }));
        libraryRecyclerView.setAdapter(libraryRecyclerAdapter);

        //Paging setup
        libraryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "Can the user scroll? " + recyclerView.canScrollVertically(1));
                if(!recyclerView.canScrollHorizontally(View.FOCUS_RIGHT)
                        && NetworkState.getInstance().getStatus().equals(NetworkStatus.DONE))
                {
                    arViewModel.fetchItems();
                }
            }
        });
    }

    /**
     * Updates the tracker.
     */
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

    /**
     * Updates the tracker. Checks if its hitting a valid plane.
     * @return boolean - valid plane.
     */
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

    /**
     * Check camera status.
     * @return boolean - is camera tracking.
     */
    private boolean updateTracking()
    {
        Frame frame =  arFragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING;

        return isTracking != wasTracking;
    }

    /**
     * Get the screens center.
     * @return android.graphics.Point.
     */
    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }

    /**
     * Adds object to scene.
     * @param dir models directory.
     * @param type models plane type.
     */
    private void addObject(String dir, long type) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        Plane.Type targetPlane = null;
        String message = "The item you have selected is of ";

        //Check plane type and overwrite message.
        if(type == Type.GROUND_PLANE)
        {
            targetPlane = Plane.Type.HORIZONTAL_UPWARD_FACING;
            message = message + "ground type";
        }
        if(type == Type.CEIL_PLANE)
        {
            targetPlane = Plane.Type.HORIZONTAL_DOWNWARD_FACING;
            message = message + "ceil-mount type";
        }
        if(type == Type.WALL_PLANE)
        {
            targetPlane = Plane.Type.VERTICAL;
            message = message + "wall-mount type";
        }

        if (frame != null) {
            Log.d(TAG, "Frame is not null");
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose()))
                {
                    Plane.Type planeType = ((Plane) trackable).getType();
                    if(planeType == targetPlane)
                        modelLoader.loadModel(hit.createAnchor(), dir);
                    else
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    break;

                }
            }
        }else
            Log.d(TAG, "Frame is null");
    }

    /**
     * Add node (model) to scene.
     * @param anchor
     * @param renderable
     */
    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setLocalScale(new Vector3(0.75f, 0.75f, 0.75f));
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    /**
     * Remove node (model) from scene.
     * @param node
     */
    public void removeNodeFromScene(Node node)
    {
        Log.d(TAG, "Remove Model called");
        if(node != null && arFragment != null)
        {
            Log.d(TAG, "Removing node " + node.getName());
            arFragment.getArSceneView().getScene().removeChild(node);
            node.setRenderable(null);
            node.setParent(null);
            Log.d(TAG, "Removal successful.");
        }else
            Log.d(TAG, "Failed to remove node.");
    }

    /**
     * Handle object touch.
     * @param hitTestResult
     * @param motionEvent
     */
    private void handleSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent)
    {
        if(isDeleteMode)
        {
            Node hitNode = hitTestResult.getNode();
            if(hitNode != null)
                removeNodeFromScene(hitNode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_ar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        arViewModel.resetPage();
    }
}
