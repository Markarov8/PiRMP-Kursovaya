package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pandora.carcontrol.data.models.CarProfile;

@Dao
public interface CarProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarProfile profile);

    @Update
    void update(CarProfile profile);

    @Query("SELECT * FROM car_profile WHERE id = 1")
    CarProfile getCarProfile();
}