package com.perspectivev.repository.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.entities.ServiceLog;

import java.util.List;

@Dao
public interface ServiceLogDao {
    @Query("SELECT * FROM ServiceLogs")
    List<ServiceLog> getAll();

    @Query("SELECT * FROM ServiceLogs WHERE id IN (:ids)")
    List<ServiceLog> getById(int[] ids);

    @Query("SELECT * FROM ServiceLogs WHERE id = (:mId)")
    ServiceLog getById(int mId);

    @Query("SELECT * FROM ServiceLogs WHERE serviceType = (:svctype)")
    ServiceLog getByServiceType(String  svctype);

    @Query("SELECT * FROM ServiceLogs ORDER BY id ASC LIMIT 1")
    ServiceLog getFirst();

    @Insert
    void create(ServiceLog... obs);

    @Update
    void update(ServiceLog... objs);

    @Delete
    void delete(ServiceLog obj);
}
