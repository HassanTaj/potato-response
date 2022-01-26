package com.perspectivev.repository.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.perspectivev.entities.AppServiceOption;

import java.util.List;

@Dao
public interface AppServiceOptionDao {
    @Query("SELECT * FROM AppServiceOptions")
    List<AppServiceOption> getAll();

    @Query("SELECT * FROM AppServiceOptions WHERE id IN (:ids)")
    List<AppServiceOption> getById(int[] ids);

    @Query("SELECT * FROM AppServiceOptions WHERE id = (:mId)")
    AppServiceOption getById(int mId);

    @Query("SELECT * FROM AppServiceOptions ORDER BY id ASC LIMIT 1")
    AppServiceOption getFirst();

    @Insert
    void create(AppServiceOption... obs);

    @Update
    void update(AppServiceOption... objs);

    @Delete
    void delete(AppServiceOption obj);
}
