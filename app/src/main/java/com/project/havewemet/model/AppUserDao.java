package com.project.havewemet.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(AppUser... appUsers);

    @Query("select * from AppUser")
    LiveData<List<AppUser>> getAllUsers();

    @Query("select * from AppUser where id = :userId")
    AppUser getUserById(String userId);

    //temporary method
    @Query("select * from AppUser where name = :name")
    AppUser getUserByName(String name);

}
