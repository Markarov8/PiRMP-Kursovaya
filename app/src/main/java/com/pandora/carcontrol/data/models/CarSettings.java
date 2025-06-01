package com.pandora.carcontrol.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_settings")
public class CarSettings {
    @PrimaryKey
    private int id = 1;

    private boolean autoStartEnabled;
    private float autoStartTemperature;
    private String autoStartTime;

    private boolean notifyOnAlarm;
    private boolean notifyOnUnlock;
    private boolean geofenceEnabled;
    private float geofenceRadius;

    private boolean autoHeat;
    private boolean autoCool;
    private float targetTemp;

    private float simBalance;
    private String simNumber;

    public CarSettings(boolean autoStartEnabled, float autoStartTemperature, String autoStartTime,
                       boolean notifyOnAlarm, boolean notifyOnUnlock, boolean geofenceEnabled,
                       float geofenceRadius, boolean autoHeat, boolean autoCool, float targetTemp,
                       float simBalance, String simNumber) {
        this.autoStartEnabled = autoStartEnabled;
        this.autoStartTemperature = autoStartTemperature;
        this.autoStartTime = autoStartTime;
        this.notifyOnAlarm = notifyOnAlarm;
        this.notifyOnUnlock = notifyOnUnlock;
        this.geofenceEnabled = geofenceEnabled;
        this.geofenceRadius = geofenceRadius;
        this.autoHeat = autoHeat;
        this.autoCool = autoCool;
        this.targetTemp = targetTemp;
        this.simBalance = simBalance;
        this.simNumber = simNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAutoStartEnabled() {
        return autoStartEnabled;
    }

    public void setAutoStartEnabled(boolean autoStartEnabled) {
        this.autoStartEnabled = autoStartEnabled;
    }

    public float getAutoStartTemperature() {
        return autoStartTemperature;
    }

    public void setAutoStartTemperature(float autoStartTemperature) {
        this.autoStartTemperature = autoStartTemperature;
    }

    public String getAutoStartTime() {
        return autoStartTime;
    }

    public void setAutoStartTime(String autoStartTime) {
        this.autoStartTime = autoStartTime;
    }

    public boolean isNotifyOnAlarm() {
        return notifyOnAlarm;
    }

    public void setNotifyOnAlarm(boolean notifyOnAlarm) {
        this.notifyOnAlarm = notifyOnAlarm;
    }

    public boolean isNotifyOnUnlock() {
        return notifyOnUnlock;
    }

    public void setNotifyOnUnlock(boolean notifyOnUnlock) {
        this.notifyOnUnlock = notifyOnUnlock;
    }

    public boolean isGeofenceEnabled() {
        return geofenceEnabled;
    }

    public void setGeofenceEnabled(boolean geofenceEnabled) {
        this.geofenceEnabled = geofenceEnabled;
    }

    public float getGeofenceRadius() {
        return geofenceRadius;
    }

    public void setGeofenceRadius(float geofenceRadius) {
        this.geofenceRadius = geofenceRadius;
    }

    public boolean isAutoHeat() {
        return autoHeat;
    }

    public void setAutoHeat(boolean autoHeat) {
        this.autoHeat = autoHeat;
    }

    public boolean isAutoCool() {
        return autoCool;
    }

    public void setAutoCool(boolean autoCool) {
        this.autoCool = autoCool;
    }

    public float getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(float targetTemp) {
        this.targetTemp = targetTemp;
    }

    public float getSimBalance() {
        return simBalance;
    }

    public void setSimBalance(float simBalance) {
        this.simBalance = simBalance;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }
}