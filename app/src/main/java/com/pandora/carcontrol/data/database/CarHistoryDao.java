package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pandora.carcontrol.data.models.CarHistory;

import java.util.List;

@Dao
public interface CarHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CarHistory history);

    @Query("SELECT * FROM car_history ORDER BY timestamp DESC")
    List<CarHistory> getAllHistory();

    @Query("DELETE FROM car_history")
    void deleteAll();
}