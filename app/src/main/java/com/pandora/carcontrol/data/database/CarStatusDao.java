package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pandora.carcontrol.data.models.CarStatus;

@Dao
public interface CarStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarStatus status);

    @Update
    void update(CarStatus status);

    @Query("SELECT * FROM car_status WHERE id = 1")
    CarStatus getCarStatus();
}