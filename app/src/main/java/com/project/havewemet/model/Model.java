package com.project.havewemet.model;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }

    private Executor executor = Executors.newSingleThreadExecutor();

    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void addAppUser(AppUser appUser, Listener<Void> listener){
        executor.execute(()->{
            localDb.appUserDao().insertAll(appUser);
            listener.onComplete(null);
        });
    }

    public void getUserByName(String name, Listener<AppUser> listener){
        executor.execute(()->{
            listener.onComplete(localDb.appUserDao().getUserByName(name));
        });
    }
}
