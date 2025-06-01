package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pandora.carcontrol.data.models.CarCommand;
import java.util.List;

// DAO для управления командами автомобиля
@Dao
public interface CarCommandDao {
    // Вставка команды в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CarCommand command);

    // Обновление команды в базе данных
    @Update
    void update(CarCommand command);

    // Получение всех команд из базы данных
    @Query("SELECT * FROM car_commands ORDER BY timestamp DESC")
    List<CarCommand> getAllCommands();

    // Удаление всех команд из базы данных
    @Query("DELETE FROM car_commands")
    void deleteAll();
}