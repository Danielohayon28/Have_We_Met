package com.project.havewemet.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.havewemet.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private LiveData<List<Status>> liveStatusList;
    private LiveData<List<AppUser>> liveAppUserList;
    public LiveData<List<Status>> getAllStatuses() {
        if (liveStatusList == null){
            liveStatusList = localDb.statusDao().getAllStatuses();
        }
        //both statuses and the authors need to be refreshed because, there is aggregation
        refreshStatuses();
        getAllUsers();

        return liveStatusList;
    }

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor spEditor;

    private Model(){
        sharedPref = MyApplication.getMyContext().getSharedPreferences("signed_in_user_info", Context.MODE_PRIVATE);
        spEditor = sharedPref.edit();
    }

    public LiveData<List<AppUser>> getAllUsers(){
        //when gettings statuses, associated users also need to be fetched
        if (liveAppUserList == null){
            liveAppUserList = localDb.appUserDao().getAllUsers();
        }
        refreshAppUsers();
        return liveAppUserList;
    }
    public enum LoadingState{
        LOADING, NOT_LOADING
    }

    final public MutableLiveData<LoadingState> EventStatusesListLoadingState =
            new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);
    final public MutableLiveData<LoadingState> EventAppUsersListLoadingState =
            new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    private final FirebaseModel fbModel = new FirebaseModel();
    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }

    //create an executor to run required tasks on background threads, such as database handling
    private final Executor executor = Executors.newSingleThreadExecutor();

    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void refreshStatuses() {  // refreshes the posts/statuses of users
        EventStatusesListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Status.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        fbModel.getAllStatusesSince(localLastUpdate, list->{
            executor.execute(()->{
                Long time = localLastUpdate;
                for(Status status: list){
                    // insert new records into ROOM
                    localDb.statusDao().insertAll(status);
                    if (time < status.getLastUpdated())
                        time = status.getLastUpdated();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Status.setLocalLastUpdate(time);
                EventStatusesListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void refreshAppUsers(){
        EventAppUsersListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = AppUser.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        fbModel.getAllAppUsersSince(localLastUpdate, list->{
            executor.execute(()->{
                Long time = localLastUpdate;
                for(AppUser appuser: list){
                    // insert new records into ROOM
                    localDb.appUserDao().insertAll(appuser);
                    if (time < appuser.getLastUpdated()){
                        time = appuser.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                AppUser.setLocalLastUpdate(time);
                EventAppUsersListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public LiveData<List<Status>> getAllStatusesByUser(String userId){
        return localDb.statusDao().getAllStatusesByUser(userId);
    }

    private void saveSigningInfoLocally(AppUser appUser){
        spEditor.putString(AppUser.ID, appUser.getId());
        spEditor.putString(AppUser.NAME, appUser.getName());
        spEditor.putString(AppUser.USERNAME, appUser.getUsername());
        spEditor.putString(AppUser.AVATAR_URL, appUser.getAvatarUrl());
        spEditor.commit();
    }
    public void signIn(String email, String pass, Model.Listener<Boolean> listener){
        fbModel.signIn(email, pass, bool->{
            //we need to keep the info of signed in user to sharedpref for easy fast access
            if (bool){
                fbModel.getUserById(fbModel.getFirebaseUser().getUid(), appUser->{
                    saveSigningInfoLocally(appUser);
                    listener.onComplete(true);
                });
            }else{
                listener.onComplete(false);
            }
        });
    }


    // as we are storing extra info of appuser, so pass the appUser as well
    public void signUp(String email, String pass, AppUser appUser, Model.Listener<Boolean> listener){
        fbModel.signUp(email, pass, appUser, bool->{
            if (bool){
                fbModel.getUserById(fbModel.getFirebaseUser().getUid(), appUserloggedIn->{
                    saveSigningInfoLocally(appUserloggedIn);
                    listener.onComplete(true);
                });
            }else{
                listener.onComplete(false);
            }
        });
    }

    public AppUser getSignedInAppUser(){
        AppUser appUser = new AppUser();
        appUser.setId(sharedPref.getString(AppUser.ID, ""));
        appUser.setName(sharedPref.getString(AppUser.NAME, ""));
        appUser.setUsername(sharedPref.getString(AppUser.USERNAME, ""));
        appUser.setAvatarUrl(sharedPref.getString(AppUser.AVATAR_URL, ""));
        return appUser;
    }

    public String getSignedInEmail(){
        return fbModel.getFirebaseUser().getEmail();
    }

    public void editProfile(AppUser appUser, String email, Listener<Boolean> listener){
        fbModel.editProfile(appUser, email, listener);
    }

    public boolean isSignedIn(){
        return fbModel.getFirebaseUser() != null;
    }

    public void addStatus(Status status, Model.Listener<Void> listener){
        fbModel.addStatus(status, nothing ->{
            refreshStatuses();
            listener.onComplete(null);
        });
    }

    public void getUserById(String userId, Listener<AppUser> listener){
        Executors.newSingleThreadExecutor().execute(()->{
            AppUser user = localDb.appUserDao().getUserById(userId);
            listener.onComplete(user);
        });
    }


    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        fbModel.uploadImage(name,bitmap,listener);
    }

    public void signout() {
        fbModel.signout();
    }

    public String getUserId(){
        return fbModel.getFirebaseUser().getUid();
    }
}
