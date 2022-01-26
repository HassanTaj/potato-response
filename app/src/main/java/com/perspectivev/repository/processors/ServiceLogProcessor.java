package com.perspectivev.repository.processors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.annotation.Nullable;

import com.perspectivev.entities.ServiceLog;
import com.perspectivev.repository.DbConsts;
import com.perspectivev.repository.interfaces.IRepository;

import java.util.ArrayList;

public class ServiceLogProcessor extends BaseProcessor implements IRepository<ServiceLog> {
    private  Context _ctx;
    public ServiceLogProcessor(Context context) {
        super(context);
    }

    @Override
    public void create(ServiceLog item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DbConsts.ServiceLogTbl.C_ServiceName, item.getServiceName());
        value.put(DbConsts.ServiceLogTbl.C_ServiceType, item.getServiceType());
        value.put(DbConsts.ServiceLogTbl.C_ServiceStartTime, item.getServiceStartTime());
        value.put(DbConsts.ServiceLogTbl.C_ServiceEndTime, item.getServiceEndTime());
        db.insert(DbConsts.ServiceLogTbl.Tbl_Name,null,value);
    }

    @Override
    public void delete(ServiceLog item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbConsts.ServiceLogTbl.Tbl_Name,"id = ?",new String[]{Integer.toString(item.getId())});
    }

    @Override
    public ArrayList<ServiceLog> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ServiceLog> serviceLogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+DbConsts.ServiceLogTbl.Tbl_Name + "",null);
        while (cursor.moveToNext()){
            serviceLogs.add(new ServiceLog(
                    cursor.getInt(cursor.getColumnIndex(DbConsts.ServiceLogTbl.C_ID)),
                    cursor.getString(cursor.getColumnIndex(DbConsts.ServiceLogTbl.C_ServiceName)),
                    cursor.getString(cursor.getColumnIndex(DbConsts.ServiceLogTbl.C_ServiceType)),
                    cursor.getString(cursor.getColumnIndex(DbConsts.ServiceLogTbl.C_ServiceStartTime)),
                    cursor.getString(cursor.getColumnIndex(DbConsts.ServiceLogTbl.C_ServiceEndTime))
            ));
        }
        return serviceLogs;
    }

    @Override
    public void update(ServiceLog item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DbConsts.ServiceLogTbl.C_ServiceName, item.getServiceName());
        value.put(DbConsts.ServiceLogTbl.C_ServiceType, item.getServiceType());
        value.put(DbConsts.ServiceLogTbl.C_ServiceStartTime, item.getServiceStartTime());
        value.put(DbConsts.ServiceLogTbl.C_ServiceEndTime, item.getServiceEndTime());
        db.update(DbConsts.ServiceLogTbl.Tbl_Name,value,"id=?",new String[]{ Integer.toString(item.getId())});
    }

    @Override
    public ServiceLog getById(int id) {
        for(ServiceLog item:getAll()){
            if(item.getId()==id){
                return item;
            }
        }
        return null;
    }

    @Override
    public ServiceLog getFirst() {
        return null;
    }
}
