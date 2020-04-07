package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;
import android.content.ContentResolver;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.LibraryRepo;
import com.tahmidu.room_designer_client_android.repository.StatisticRepo;
import com.tahmidu.room_designer_client_android.repository.UserRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainViewModel extends AndroidViewModel
{
    private final String TAG = "MAIN_VIEW_MODEL";

    //Navigation
    public final static int MAIN_LIB_FRAGMENT = 0;
    public final static int ITEM_FRAGMENT = 1;
    public final static int AR_ACTIVITY = 2;
    public final static int USER_ITEM_FRAGMENT = 3;
    public final static int USER_LIB_FRAGMENT = 4;
    public final static int ADD_ITEM_FRAGMENT = 5;
    public final static int EDIT_ITEM_FRAGMENT = 6;
    public final static int MY_ACCOUNT_FRAGMENT = 7;
    public final static int CHANGE_ACC_DETAILS_FRAGMENT = 8;
    public final static int CHANGE_ACC_PASSWORD_FRAGMENT = 9;
    public final static int WELCOME_ACTIVITY = 10;

    //Navigation
    private final MutableLiveData<Integer> navigateFragment = new MutableLiveData<>();

    //Main Library
    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();

    //User Library
    private final MutableLiveData<List<Item>> userItemsLiveData = new MutableLiveData<>();

    //Item
    private final MutableLiveData<String> contactInfoLiveData = new MutableLiveData<>();

    //Add Item
    private List<MediaFile> previewImages = new ArrayList<>();
    private MediaFile thumbnail;
    private List<MediaFile> modelFiles = new ArrayList<>();

    //User Item
    private MutableLiveData<String> statisticsLiveData = new MutableLiveData<>();

    //My Account
    private MutableLiveData<User> usersAccountLiveData = new MutableLiveData<>();
    private String firstName;
    private String lastName;
    private String phoneNumber;

    //Password Change
    private String password;
    private String reEnterPassword;
    private MutableLiveData<String> passwordChangeStatus = new MutableLiveData<>();

    //Repository
    private final LibraryRepo libraryRepo;
    private final StatisticRepo statisticRepo;
    private final UserRepo userRepo;

    private PreferenceProvider preferenceProvider;

    //Search
    private Map<Integer, Integer> categories = new HashMap<>();
    private Map<Integer, Integer> types = new HashMap<>();
    private String searchItem = null;
    private boolean filterChanged = false;

    public MainViewModel(@NonNull Application application)
    {
        super(application);

        itemsLiveData.setValue(new ArrayList<>());
        contactInfoLiveData.setValue("N/A");

        libraryRepo = LibraryRepo.getInstance();
        statisticRepo = StatisticRepo.getInstance();
        userRepo = UserRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());
    }

    //-----------------------------Navigation-----------------------------

    public void navigateFragment(int id)
    {
        navigateFragment.postValue(id);
    }

    //-----------------------------Main Library-----------------------------

    public void fetchMainLibrary()
    {
        List<Integer> listCategories =  new ArrayList<>(categories.values());
        List<Integer> listTypes =  new ArrayList<>(types.values());

        if(listCategories.size() == 0)
            listCategories = null;
        if(listTypes.size() == 0)
            listTypes = null;

        libraryRepo.fetchMainLibrary(itemsLiveData, searchItem, listCategories, listTypes,
                preferenceProvider);
        Log.d(TAG, "Main Library Page Number: " + preferenceProvider.getPage());
    }

    public void toggleCategory(int category)
    {
        if(categories.containsValue(category))
            categories.remove(category);
        else
            categories.put(category, category);
        filterChanged = true;
    }

    public void toggleType(int type)
    {
        if(types.containsValue(type))
            types.remove(type);
        else
            types.put(type, type);
        filterChanged = true;
    }

    //-----------------------------User Library-----------------------------

    public void fetchUserLibrary()
    {
        libraryRepo.fetchUserLibrary(userItemsLiveData, preferenceProvider);
        Log.d(TAG, "User Library Page Number: " + preferenceProvider.getPage());
    }

    //-----------------------------Item-----------------------------

    public void retrieveContactInfo()
    {
        libraryRepo.retrieveContactInfo(preferenceProvider.getItem().getUser(), contactInfoLiveData,
                preferenceProvider.getJWTToken(), preferenceProvider);
    }

    //-----------------------------Item Submission-----------------------------

    public void submitItem(String title, String desc, String catName, String typeName,
                           ContentResolver contentResolver)
    {
        libraryRepo.submitItem(preferenceProvider, title, desc, catName, typeName, previewImages,
                thumbnail, modelFiles, contentResolver);
    }

    //-----------------------------Edit Item-----------------------------

    public void editItem(String title, String desc, String catName, String typeName,
                         ContentResolver contentResolver)
    {
        libraryRepo.editItem(preferenceProvider, title, desc, catName,
                typeName, previewImages, thumbnail, modelFiles, contentResolver);
    }

    public void removeThumbnail()
    {
        libraryRepo.removeThumbnail(preferenceProvider);
    }

    public void removeImages()
    {
        libraryRepo.removeImages(preferenceProvider);
    }

    public void removeModels()
    {
        libraryRepo.removeModel(preferenceProvider);
    }


    //-----------------------------My Account-----------------------------

    public void fetchUserDetails()
    {
        userRepo.fetchUserDetails(preferenceProvider, usersAccountLiveData);
    }

    public void changeUserDetails(String firstName, String lastName, String phoneNum)
    {
        userRepo.changeUserDetails(preferenceProvider, firstName, lastName, null, phoneNum);
        navigateFragment(MY_ACCOUNT_FRAGMENT);
    }

    public void changePassword(String password, String reEnterPassword)
    {
        final String PASSWORD_NO_MATCH = "The passwords you've provided do not match!";
        if(password.equals(reEnterPassword)) {
            userRepo.changeUserDetails(preferenceProvider, null, null,
                    password, null);
            navigateFragment(MY_ACCOUNT_FRAGMENT);
        }else
            passwordChangeStatus.postValue(PASSWORD_NO_MATCH);

    }

    public void deleteUser()
    {
        userRepo.deleteUser(preferenceProvider);
        navigateFragment(WELCOME_ACTIVITY);
    }

    //-----------------------------Users Item-----------------------------

    public void fetchItemStatistics()
    {
        statisticRepo.retrieveStatistics(statisticsLiveData, preferenceProvider);
    }

    public void deleteSelectedItem()
    {
        libraryRepo.removeItem(preferenceProvider.getItem().getItemId(), preferenceProvider);
    }

    //-----------------------------Statistics-----------------------------

    public void incrementView()
    {
        statisticRepo.incrementView(preferenceProvider);
    }

    //-----------------------------Aux-----------------------------

    public void resetPage()
    {
        preferenceProvider.savePage(0);
    }

    public void clickedItem(int listPosition) {
        Item item = Objects.requireNonNull(itemsLiveData.getValue()).get(listPosition);
        preferenceProvider.saveItem(item);
    }

    public void clickedUserItem(int listPosition)
    {
        Item item = Objects.requireNonNull(userItemsLiveData.getValue()).get(listPosition);
        preferenceProvider.saveItem(item);
    }

    public void setLibraryCachePath(String path)
    {
        libraryRepo.setCachePath(path);
    }

    public boolean currentItemHasModel()
    {
        return preferenceProvider.getItem().isHasModel();
    }

    //-----------------------------Getters and Setters-----------------------------

    public boolean isFilterChanged() {
        return filterChanged;
    }

    public void setFilterChanged(boolean filterChanged) {
        this.filterChanged = filterChanged;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public Item getSelectedItem()
    {
        return preferenceProvider.getItem();
    }

    public MutableLiveData<Integer> getNavigateFragment() {
        return navigateFragment;
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public MutableLiveData<List<Item>> getUserItemsLiveData() {
        return userItemsLiveData;
    }

    public MutableLiveData<String> getContactInfoLiveData() {
        return contactInfoLiveData;
    }

    public MutableLiveData<String> getStatisticsLiveData() {
        return statisticsLiveData;
    }

    public List<MediaFile> getPreviewImages() {
        return previewImages;
    }

    public MutableLiveData<User> getUsersAccountLiveData() {
        return usersAccountLiveData;
    }

    public void setPreviewImages(List<MediaFile> previewImages) {
        this.previewImages = previewImages;
    }

    public MediaFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MediaFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<MediaFile> getModelFiles() {
        return modelFiles;
    }

    public void setModelFiles(List<MediaFile> modelFiles) {
        this.modelFiles = modelFiles;
    }

    public PreferenceProvider getPreferenceProvider() {
        return preferenceProvider;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MutableLiveData<String> getPasswordChangeStatus() {
        return passwordChangeStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReEnterPassword() {
        return reEnterPassword;
    }

    public void setReEnterPassword(String reEnterPassword) {
        this.reEnterPassword = reEnterPassword;
    }
}
