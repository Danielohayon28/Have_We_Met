package com.project.havewemet.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.havewemet.MyApplication;


@Database(entities = {AppUser.class, Status.class}, version = 80)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract AppUserDao appUserDao();
    public abstract StatusDao statusDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppLocalDbRepository.class,
                        "have_we_met.db") //database name of our application
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}

