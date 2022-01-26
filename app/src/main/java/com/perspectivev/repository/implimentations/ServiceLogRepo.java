package com.perspectivev.repository.implimentations;

import android.content.Context;
import android.os.AsyncTask;

import com.perspectivev.entities.ServiceLog;
import com.perspectivev.repository.daos.AppDbContext;
import com.perspectivev.repository.daos.ServiceLogDao;
import com.perspectivev.repository.interfaces.IRepository;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ServiceLogRepo implements IRepository<ServiceLog> {

    private Context _ctx;
//    private ServiceLogProcessor _serviceLogProcessor;
    private ServiceLogDao _serviceLogProcessor;
    public  ServiceLogRepo(Context ctx){
        this._ctx = ctx;
        _serviceLogProcessor = AppDbContext.getDatabase(this._ctx).serviceLogs();
    }

    @Override
    public void create(ServiceLog item) {
        _serviceLogProcessor.create(item);
    }

    @Override
    public void delete(ServiceLog item) {
        _serviceLogProcessor.delete(item);
    }

    @Override
    public ArrayList<ServiceLog> getAll() {
        return (ArrayList<ServiceLog>) _serviceLogProcessor.getAll();
    }

    @Override
    public void update(ServiceLog item) {
        _serviceLogProcessor.update(item);
    }

    @Override
    public ServiceLog getById(int id) {
        return _serviceLogProcessor.getById(id);
    }

    @Override
    public ServiceLog getFirst() {
        return _serviceLogProcessor.getFirst();
    }

    public ServiceLog getByServiceType(String type){
        ServiceLog sl = null;
//        for (ServiceLog log: getAll()) {
//            if(log.getServiceType().equals(type)){
//                sl= log;
//            }
//        }
        sl = _serviceLogProcessor.getByServiceType(type);

        return sl;
    }
}
