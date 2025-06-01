package com.pandora.carcontrol.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Модель данных для профиля автомобиля
@Entity(tableName = "car_profile")
public class CarProfile {
    @PrimaryKey
    private int id = 1; // Единственный экземпляр
    private String carName;
    private String userName;
    private String userLastName;
    private String accountNumber;
    private String deviceCode;
    private String verificationCode;
    private String appVersion;

    public CarProfile(String carName, String userName, String userLastName, String accountNumber,
                      String deviceCode, String verificationCode, String appVersion) {
        this.carName = carName;
        this.userName = userName;
        this.userLastName = userLastName;
        this.accountNumber = accountNumber;
        this.deviceCode = deviceCode;
        this.verificationCode = verificationCode;
        this.appVersion = appVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}