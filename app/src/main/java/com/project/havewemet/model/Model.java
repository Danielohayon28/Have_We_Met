package com.project.havewemet.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.havewemet.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private LiveData<List<Status>> liveStatusList;
    public LiveData<List<Status>> getAllStatuses() {
        if (liveStatusList == null){
            liveStatusList = localDb.statusDao().getAllStatuses();
            refreshStatuses();
        }
        return liveStatusList;
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
        Log.e("test", "refresh statuses called");
        EventStatusesListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Status.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        fbModel.getAllStatusesSince(localLastUpdate, list->{
            executor.execute(()->{
                //todo: remove the Toast from here, its just for testing purpose
                Log.e("size", "size: "+list.size());
                new Handler(Looper.getMainLooper()).post(()->{
                    Toast.makeText(MyApplication.getMyContext(), "size: "+list.size(), Toast.LENGTH_SHORT).show();
                });
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



    public void addAppUser(AppUser appUser, Listener<Void> listener){
        // TODO: firebase adding will be handled later
        executor.execute(()->{
            localDb.appUserDao().insertAll(appUser);
            listener.onComplete(null);  //as nothing is need to be returned (as interface) on adding so Void
        });
    }

    public void getUserByName(String name, Listener<AppUser> listener){
        executor.execute(()->{
            listener.onComplete(localDb.appUserDao().getUserByName(name));
        });
    }

    public void signIn(String email, String pass, Model.Listener<Boolean> listener){
        fbModel.signIn(email, pass, listener);
    }

    // as we are storing extra info of appuser, so pass the appUser as well
    public void signUp(String email, String pass, AppUser appUser, Model.Listener<Boolean> listener){
        fbModel.signUp(email, pass, appUser, listener);
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


    public void signout() {
        fbModel.signout();
    }
    public String getUserId(){
        return fbModel.getFirebaseUser().getUid();
    }
}
