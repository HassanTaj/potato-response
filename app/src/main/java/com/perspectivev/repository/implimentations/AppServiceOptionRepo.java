package com.perspectivev.repository.implimentations;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.repository.DbConsts;
import com.perspectivev.repository.interfaces.IRepository;
import com.perspectivev.repository.processors.AppServiceOptionProcessor;

import java.util.ArrayList;

import com.perspectivev.repository.daos.*;

public class AppServiceOptionRepo implements IRepository<AppServiceOption> {
    private Context _ctx;
//    private AppServiceOptionProcessor _appServiceProcessor;
    private AppServiceOptionDao _appServiceProcessor;

    public AppServiceOptionRepo(Context ctx) {
        this._ctx = ctx;
//        this._appServiceProcessor = new AppServiceOptionProcessor(this._ctx);
        this._appServiceProcessor = AppDbContext.getDatabase(this._ctx).appServiceOptions();

    }

    @Override
    public void create(AppServiceOption item) {
        this._appServiceProcessor.create(item);
    }

    @Override
    public void delete(AppServiceOption item) {
        this._appServiceProcessor.delete(item);
    }

    @Override
    public ArrayList<AppServiceOption> getAll() {
        return (ArrayList<AppServiceOption>) this._appServiceProcessor.getAll();
    }

    @Override
    public void update(AppServiceOption item) {
        this._appServiceProcessor.update(item);
    }

    @Override
    public AppServiceOption getById(int id) {
        return this._appServiceProcessor.getById(id);
    }

    @Override
    public AppServiceOption getFirst() {
        return  this._appServiceProcessor.getFirst();
    }
}
