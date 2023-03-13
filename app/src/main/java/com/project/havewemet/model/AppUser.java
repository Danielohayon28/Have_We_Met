package com.project.havewemet.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//refers to users of app
@Entity
public class AppUser {

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

}
