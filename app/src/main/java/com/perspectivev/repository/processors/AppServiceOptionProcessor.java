package com.perspectivev.repository.processors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.repository.DbConsts;
import com.perspectivev.repository.interfaces.IRepository;

import java.util.ArrayList;

public class AppServiceOptionProcessor extends BaseProcessor implements IRepository<AppServiceOption> {
    private  Context _ctx;

    public AppServiceOptionProcessor(Context context) {
        super(context);
    }

    @Override
    public void create(AppServiceOption item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DbConsts.AppServiceOptionsTbl.C_IsServiceActive, item.isServiceActive());
        value.put(DbConsts.AppServiceOptionsTbl.C_ResponseText, item.getResponseText());
        value.put(DbConsts.AppServiceOptionsTbl.C_SelectedSim, item.getSelectedSim());
        db.insert(DbConsts.AppServiceOptionsTbl.Tbl_Name,null,value);
    }

    @Override
    public void delete(AppServiceOption item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbConsts.AppServiceOptionsTbl.Tbl_Name,"Id = ?",new String[]{Integer.toString(item.getId())});
    }

    @Override
    public ArrayList<AppServiceOption> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<AppServiceOption> appServiceOptions = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+DbConsts.AppServiceOptionsTbl.Tbl_Name+"",null);
        while (cursor.moveToNext()){
//            AppServiceOption aso= new AppServiceOption();
            appServiceOptions.add(new AppServiceOption(
                    cursor.getInt(cursor.getColumnIndex(DbConsts.AppServiceOptionsTbl.C_ID)),
                    cursor.getInt(cursor.getColumnIndex(DbConsts.AppServiceOptionsTbl.C_IsServiceActive)) ==0? false: true,
                    cursor.getString(cursor.getColumnIndex(DbConsts.AppServiceOptionsTbl.C_ResponseText)),
                    cursor.getString(cursor.getColumnIndex(DbConsts.AppServiceOptionsTbl.C_SelectedSim))
            ));
        }
        return appServiceOptions;
    }

    @Override
    public void update(AppServiceOption item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DbConsts.AppServiceOptionsTbl.C_IsServiceActive, item.isServiceActive());
        value.put(DbConsts.AppServiceOptionsTbl.C_ResponseText, item.getResponseText());
        value.put(DbConsts.AppServiceOptionsTbl.C_SelectedSim, item.getSelectedSim());
        db.update(DbConsts.AppServiceOptionsTbl.Tbl_Name,value,"id=?",new String[]{ Integer.toString(item.getId())});
    }

    @Override
    public AppServiceOption getById(int id) {
        for(AppServiceOption item:getAll()){
            if(item.getId()==id){
                return item;
            }
        }
        return null;
    }

    @Override
    public AppServiceOption getFirst() {
        return null;
    }
}
