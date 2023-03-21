package com.project.havewemet.model;

import static androidx.room.ForeignKey.CASCADE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.project.havewemet.MyApplication;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;



@Entity //(foreignKeys = @ForeignKey(entity = AppUser.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Status {


    static final String ID = "id",
            USER_ID = "user_id",
            CONTENT = "content",

    TIME = "time",
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

    public String time = "";
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


    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time){
        this.time = time;
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
    public String getTime() {
        return time;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(USER_ID, userId);
        json.put(CONTENT, content);
        json.put(TIME, FieldValue.serverTimestamp());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    public static Status fromJson(Map<String, Object> json) {

        String id = (String) json.get(ID);
        String user_id = (String) json.get(USER_ID);
        String content = (String) json.get(CONTENT);
        Status status = new Status(id, user_id, content);
        try{
            Timestamp timestamp = (Timestamp) json.get(TIME);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            status.setTime(dateFormat.format(timestamp.toDate()));
            timestamp = (Timestamp) json.get(LAST_UPDATED);
            status.setLastUpdated(timestamp.getSeconds());

        }catch (Exception ignored){}
        return status;
    }

}
