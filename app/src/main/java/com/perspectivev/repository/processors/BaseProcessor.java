package com.perspectivev.repository.processors;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;

import com.perspectivev.repository.DbConsts;

public class BaseProcessor extends SQLiteOpenHelper {

    public BaseProcessor(Context context) {
        super(context, DbConsts.DB_NAME, null, DbConsts.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                DbConsts.AppServiceOptionsTbl.Tbl_Name +
                "(" +
                DbConsts.AppServiceOptionsTbl.C_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbConsts.AppServiceOptionsTbl.C_IsServiceActive+ " BOOLEAN NOT NULL," +
                DbConsts.AppServiceOptionsTbl.C_ResponseText+ " TEXT NOT NULL," +
                DbConsts.AppServiceOptionsTbl.C_SelectedSim+ " TEXT NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                DbConsts.ServiceLogTbl.Tbl_Name + "(" +
                DbConsts.ServiceLogTbl.C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbConsts.ServiceLogTbl.C_ServiceName + " TEXT NOT NULL," +
                DbConsts.ServiceLogTbl.C_ServiceType + " TEXT NOT NULL," +
                DbConsts.ServiceLogTbl.C_ServiceStartTime + " TEXT NOT NULL," +
                DbConsts.ServiceLogTbl.C_ServiceEndTime + " TEXT NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+DbConsts.AppServiceOptionsTbl.Tbl_Name+"");
        db.execSQL("DROP TABLE IF EXISTS " + DbConsts.ServiceLogTbl.Tbl_Name + "");
        onCreate(db);
    }
}
