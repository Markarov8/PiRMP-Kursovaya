package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.pandora.carcontrol.data.models.CarHistory;
import java.util.List;

// DAO для управления историей событий автомобиля
@Dao
public interface CarHistoryDao {
    // Вставка записи истории в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CarHistory history);

    // Получение всех записей истории из базы данных
    @Query("SELECT * FROM car_history ORDER BY timestamp DESC")
    List<CarHistory> getAllHistory();

    // Удаление всех записей истории из базы данных
    @Query("DELETE FROM car_history")
    void deleteAll();
}