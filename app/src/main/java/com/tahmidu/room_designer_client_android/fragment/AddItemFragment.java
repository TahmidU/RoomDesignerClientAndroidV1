package com.tahmidu.room_designer_client_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentAddItemBinding;
import com.tahmidu.room_designer_client_android.util.file.CustomFileUtil;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Add Items
 */
public class AddItemFragment extends Fragment {

    private final String TAG = "ADD_ITEM_FRAGMENT";

    //File Requests
    private final static int FILE_REQUEST_THUMBNAIL = 1;
    private final static int FILE_REQUEST_IMAGES = 2;
    private final static int FILE_REQUEST_MODELS = 3;

    private FragmentAddItemBinding binding;

    //Model View
    private MainViewModel mainViewModel;

    //Views
    private TextView thumbnailSelectedTxt;
    private TextView imagesSelectedTxt;
    private TextView modelsSelectedTxt;
    private TextView errorTxt;

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_item, container, false);

        Log.d(TAG, "called");

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        String TOOLBAR_TITLE = "Add Item";
        toolbar.setTitle(TOOLBAR_TITLE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);
        Button uploadThumbnailBtn = binding.getRoot().findViewById(R.id.add_item_upload_thumbnail_btn);
        Button uploadImagesBtn = binding.getRoot().findViewById(R.id.add_item_upload_images_btn);
        Button uploadModelBtn = binding.getRoot().findViewById(R.id.add_item_upload_models_btn);
        EditText title = binding.getRoot().findViewById(R.id.add_item_title_edittext);
        EditText desc = binding.getRoot().findViewById(R.id.add_item_desc_edittext);
        TextView descWordCount = binding.getRoot().findViewById(R.id.add_item_desc_watcher);
        Spinner catSpinner = binding.getRoot().findViewById(R.id.add_item_cat_spinner);
        Spinner typeSpinner = binding.getRoot().findViewById(R.id.add_item_type_spinner);
        Button submitBtn = binding.getRoot().findViewById(R.id.add_item_submit_btn);
        thumbnailSelectedTxt = binding.getRoot().findViewById(R.id.add_item_thumbnail_selected_text);
        imagesSelectedTxt = binding.getRoot().findViewById(R.id.add_item_images_selected_text);
        modelsSelectedTxt = binding.getRoot().findViewById(R.id.add_item_models_selected_text);
        errorTxt = binding.getRoot().findViewById(R.id.add_item_error_text);

        //Set Drawer
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Set up Spinners
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types, android.R.layout.simple_spinner_item);
        catSpinner.setAdapter(catAdapter);
        typeSpinner.setAdapter(typeAdapter);

        //Listeners
        uploadThumbnailBtn.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setSuffixes("jpg","jpeg","png","webp")
                    .setCheckPermission(true)
                    .setShowImages(true)
                    .enableImageCapture(true)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .setShowAudios(false)
                    .setShowVideos(false)
                    .setShowFiles(false)
                    .setShowImages(true)
                    .build());
            startActivityForResult(intent, FILE_REQUEST_THUMBNAIL);
        });

        uploadImagesBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setSuffixes("jpg","jpeg","png","webp")
                    .setCheckPermission(true)
                    .setShowImages(true)
                    .enableImageCapture(true)
                    .setMaxSelection(5)
                    .setSkipZeroSizeFiles(true)
                    .setShowAudios(false)
                    .setShowVideos(false)
                    .setShowFiles(false)
                    .setShowImages(true)
                    .build());
            startActivityForResult(intent, FILE_REQUEST_IMAGES);
        });

        uploadModelBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setSuffixes("bin","gltf")
                    .setCheckPermission(true)
                    .setShowImages(true)
                    .enableImageCapture(true)
                    .setMaxSelection(2)
                    .setSkipZeroSizeFiles(true)
                    .setShowVideos(false)
                    .setShowAudios(false)
                    .setShowImages(false)
                    .setShowFiles(true)
                    .build());
            startActivityForResult(intent, FILE_REQUEST_MODELS);
        });

        submitBtn.setOnClickListener(v -> {
            boolean isValid = false;
            HashMap<String, Boolean> permissibleType = new HashMap<>();
            permissibleType.put(".bin", true);
            permissibleType.put(".gltf", true);
            permissibleType.put(".png", true);
            permissibleType.put(".jpg", true);

            if(mainViewModel.getModelFiles().size() == 0)
                isValid = true;

            if(mainViewModel.getModelFiles().size() < 4)
            {
                for(MediaFile mf : mainViewModel.getModelFiles())
                {
                    String ext = CustomFileUtil.getExtension(mf.getName());
                    Log.d(TAG, "File Extension: " + ext);
                    Boolean result = permissibleType.get(ext);
                    isValid = result != null;

                    if(!isValid)
                    {
                        String msg = "Attempted to Upload Impermissible Type for Model";
                        errorTxt.setText(msg);
                    }

                    Log.d(TAG, "File Name: " + mf.getName() + ", Content Type: " + mf.getMimeType());
                }
            }else
                {
                    String msg = "Exceeded Limit for Model";
                    errorTxt.setText(msg);
                }

            if(isValid) {
                mainViewModel.submitItem(title.getText().toString(), desc.getText().toString(),
                        catSpinner.getSelectedItem().toString(),
                        typeSpinner.getSelectedItem().toString(), getActivity().getContentResolver());
                FragmentManager transactionManager = getActivity().getSupportFragmentManager();
                transactionManager.popBackStack();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.length() + "/1000";
                descWordCount.setText(string);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        desc.addTextChangedListener(textWatcher);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        mainViewModel.getNavigateFragment().setValue(MainViewModel.ADD_ITEM_FRAGMENT);

        //Fragment transaction setup
        FragmentManager transactionMan = getActivity().getSupportFragmentManager();
        mainViewModel.getNavigateFragment().setValue(MainViewModel.ADD_ITEM_FRAGMENT);

        //Observers
        mainViewModel.getNavigateFragment().observe(getActivity(), integer ->
        {
            if(integer == MainViewModel.USER_LIB_FRAGMENT)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new UserLibraryFragment());
                transaction.commit();
            }
        });

        mainViewModel.setLibraryCachePath(getContext().getFilesDir().getAbsolutePath()+"/cache-upload");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<MediaFile> files = null;
        if (data != null) {
            files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
        }

        if(files != null) {
            if (FILE_REQUEST_THUMBNAIL == requestCode) {
                mainViewModel.setThumbnail(files.get(0));
                Log.d(TAG, "modelFiles size: " + (mainViewModel.getThumbnail() != null));
                if (mainViewModel.getThumbnail() != null) {
                    String msg = "1/1 Files Selected";
                    thumbnailSelectedTxt.setText(msg);
                }
            }
            if (FILE_REQUEST_IMAGES == requestCode) {
                mainViewModel.setPreviewImages(files);
                Log.d(TAG, "previewImagesFiles size: " + mainViewModel.getPreviewImages().size());
                String msg = mainViewModel.getPreviewImages().size() + "/5 Files Selected";
                imagesSelectedTxt.setText(msg);
            }
            if (FILE_REQUEST_MODELS == requestCode) {
                mainViewModel.setModelFiles(files);
                Log.d(TAG, "modelFiles size: " + mainViewModel.getModelFiles().size());
                String msg = mainViewModel.getModelFiles().size()
                        + "/3 Files Selected (must include gltf and bin files)";
                modelsSelectedTxt.setText(msg);
            }
        }
    }
}
