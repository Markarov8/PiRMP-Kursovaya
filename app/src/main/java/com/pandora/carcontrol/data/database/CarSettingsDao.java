package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pandora.carcontrol.data.models.CarSettings;

@Dao
public interface CarSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarSettings settings);

    @Update
    void update(CarSettings settings);

    @Query("SELECT * FROM car_settings WHERE id = 1")
    CarSettings getCarSettings();
}