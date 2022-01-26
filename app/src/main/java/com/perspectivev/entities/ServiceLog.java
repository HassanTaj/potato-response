package com.perspectivev.entities;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ServiceLogs")
public class ServiceLog {
    @PrimaryKey
    private int id;
    @ColumnInfo
    private String serviceName;
    @ColumnInfo
    private String serviceType;
    @ColumnInfo
    private String serviceStartTime;
    @ColumnInfo
    private String serviceEndTime;

    public ServiceLog(int id, String serviceName, String serviceType, String serviceStartTime, String serviceEndTime) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.serviceStartTime = serviceStartTime;
        this.serviceEndTime = serviceEndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Service > " + this.getServiceName()+ "\nStart : " + this.getServiceStartTime() + "\nEnd : " + this.getServiceEndTime() + "";
    }
}
