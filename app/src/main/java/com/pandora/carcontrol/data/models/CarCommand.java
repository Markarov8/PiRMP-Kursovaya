package com.pandora.carcontrol.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_commands")
public class CarCommand {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String type;
    private String timestamp;
    private String status;
    private String message;

    public CarCommand(long id, String type, String timestamp, String status, String message) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}