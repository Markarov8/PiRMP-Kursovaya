package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pandora.carcontrol.data.models.CarCommand;

import java.util.List;

@Dao
public interface CarCommandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CarCommand command);

    @Update
    void update(CarCommand command);

    @Query("SELECT * FROM car_commands ORDER BY timestamp DESC")
    List<CarCommand> getAllCommands();

    @Query("DELETE FROM car_commands")
    void deleteAll();
}