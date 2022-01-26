package com.perspectivev.repository.daos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.entities.ServiceLog;
import com.perspectivev.repository.DbConsts;

@Database(entities = {
        AppServiceOption.class,
        ServiceLog.class,
}, version = 1)
public abstract class AppDbContext extends RoomDatabase {
    public abstract AppServiceOptionDao appServiceOptions();
    public abstract ServiceLogDao serviceLogs();

    public static AppDbContext getDatabase(Context ctx){
        return Room.databaseBuilder(ctx,AppDbContext.class,DbConsts.DB_NAME)
                .allowMainThreadQueries().build();
    }
}
