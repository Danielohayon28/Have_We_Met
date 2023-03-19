package com.project.havewemet.model;

import static androidx.room.ForeignKey.CASCADE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.project.havewemet.MyApplication;

import java.util.HashMap;
import java.util.Map;


@Entity //(foreignKeys = @ForeignKey(entity = AppUser.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Status {


    static final String ID = "id",
        USER_ID = "user_id",
        CONTENT = "content",
        LAST_UPDATED = "lastUpdated",
        LOCAL_LAST_UPDATED = "local_last_updated_statuses";
    public static String COLLECTION = "statuses";

    //status posted by AppUser

    @PrimaryKey
    @NonNull
    public String id = "";
    @NonNull
    public String userId = "";

    public String content = ""; //content of post status posted by appuser

    public long lastUpdated;

    public Status(@NonNull String id, @NonNull String userId, String content) {
        this.id = id;
        this.userId = userId;
        this.content = content;
    }

    public static void setLocalLastUpdate(Long lastUpdate) {
        SharedPreferences sp = MyApplication.getMyContext().getSharedPreferences("TAG",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(LOCAL_LAST_UPDATED,lastUpdate);
        editor.commit();
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static Status fromJson(Map<String, Object> json) {

        String id = (String) json.get(ID);
        String user_id = (String) json.get(USER_ID);
        String content = (String) json.get(CONTENT);
        Status status = new Status(id, user_id, content);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            status.setLastUpdated(time.getSeconds());
        }catch (Exception ignored){}
        return status;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public long getLastUpdated(){
        return lastUpdated;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(USER_ID, userId);
        json.put(CONTENT, content);
        return json;
    }

}
