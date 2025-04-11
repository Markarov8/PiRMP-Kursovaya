package com.pandora.carcontrol.data.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_status")
public class CarStatus {
    @PrimaryKey
    private int id = 1; // Single instance

    private boolean isRunning;
    private boolean isLocked;
    private boolean isAlarm;
    private float engineTemp;
    private float cabinTemp;
    private float outsideTemp;
    private float fuelLevel;
    private float batteryLevel;

    @Embedded
    private Location location;

    private String lastUpdate;
    private boolean hasConnection;

    public CarStatus(boolean isRunning, boolean isLocked, boolean isAlarm, float engineTemp, float cabinTemp,
                     float outsideTemp, float fuelLevel, float batteryLevel, Location location,
                     String lastUpdate, boolean hasConnection) {
        this.isRunning = isRunning;
        this.isLocked = isLocked;
        this.isAlarm = isAlarm;
        this.engineTemp = engineTemp;
        this.cabinTemp = cabinTemp;
        this.outsideTemp = outsideTemp;
        this.fuelLevel = fuelLevel;
        this.batteryLevel = batteryLevel;
        this.location = location;
        this.lastUpdate = lastUpdate;
        this.hasConnection = hasConnection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public float getEngineTemp() {
        return engineTemp;
    }

    public void setEngineTemp(float engineTemp) {
        this.engineTemp = engineTemp;
    }

    public float getCabinTemp() {
        return cabinTemp;
    }

    public void setCabinTemp(float cabinTemp) {
        this.cabinTemp = cabinTemp;
    }

    public float getOutsideTemp() {
        return outsideTemp;
    }

    public void setOutsideTemp(float outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    public float getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public float getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isHasConnection() {
        return hasConnection;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }
}