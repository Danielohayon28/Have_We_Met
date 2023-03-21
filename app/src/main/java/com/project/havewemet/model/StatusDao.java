package com.project.havewemet.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Status... statuses);

    @Query("select * from Status")
    LiveData<List<Status>> getAllStatuses();

    @Query("select * from Status where userId=:userID")
    LiveData<List<Status>> getAllStatusesByUser(String userID);

}
