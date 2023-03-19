package com.project.havewemet.model;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.project.havewemet.MyApplication;

import java.util.HashMap;
import java.util.Map;

//refers to users of app
@Entity
public class AppUser {

    private static final String LOCAL_LAST_UPDATED = "local_last_updated_statuses",
    ID = "id",
    NAME = "name",
    USERNAME = "username",
    AVATAR_URL = "avatar_url";

    public static String COLLECTION = "app_users";
    @PrimaryKey
    @NonNull
    public String id = "";
    public String name = "",
        username = "", //there is a username for each user which is different from id (by firebase)
        avatarUrl = "";

    public long lastUpdated;

    public AppUser(){} //required empty constructor by room library

    public AppUser(String id, String name, String username, String avatarUrl, long lastUpdated){
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.lastUpdated = lastUpdated;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long lastUpdated) {
        SharedPreferences sp = MyApplication.getMyContext().getSharedPreferences("TAG",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(LOCAL_LAST_UPDATED, lastUpdated);
        editor.commit();
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, ID);
        json.put(NAME, name);
        json.put(USERNAME, username);
        json.put(AVATAR_URL, avatarUrl);
        return json;
    }
}
