package com.perspectivev.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AppServiceOptions")
public class AppServiceOption {
    @PrimaryKey
    private int id;
    @ColumnInfo
    private boolean isServiceActive;
    @ColumnInfo
    private String responseText;
    @ColumnInfo
    private String selectedSim;

    public AppServiceOption(int id, boolean isServiceActive, String responseText, String selectedSim) {
        this.id = id;
        this.isServiceActive = isServiceActive;
        this.responseText = responseText;
        this.selectedSim = selectedSim;
    }

    public String getSelectedSim() {
        return selectedSim;
    }

    public void setSelectedSim(String selectedSim) {
        this.selectedSim = selectedSim;
    }

    public String getResponseText() {
        return responseText;
    }

    public AppServiceOption(int id, boolean isServiceActive, String responseText) {
        this.id = id;
        this.isServiceActive = isServiceActive;
        this.responseText = responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public AppServiceOption(int id, boolean isServiceActive) {
        this.id = id;
        this.isServiceActive = isServiceActive;
    }

    public AppServiceOption() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isServiceActive() {
        return isServiceActive;
    }

    public void setServiceActive(boolean serviceActive) {
        isServiceActive = serviceActive;
    }
}
