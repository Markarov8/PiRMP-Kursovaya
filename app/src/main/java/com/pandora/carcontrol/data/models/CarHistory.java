package com.pandora.carcontrol.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_history")
public class CarHistory {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String type;
    private String timestamp;
    private String details;
    private String mode;

    public CarHistory(long id, String type, String timestamp, String details, String mode) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.details = details;
        this.mode = mode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}