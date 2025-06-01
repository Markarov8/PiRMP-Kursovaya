package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pandora.carcontrol.data.models.CarStatus;

// DAO для управления статусом автомобиля
@Dao
public interface CarStatusDao {
    // Вставка статуса в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarStatus status);

    // Обновление статуса в базе данных
    @Update
    void update(CarStatus status);

    // Получение статуса из базы данных
    @Query("SELECT * FROM car_status WHERE id = 1")
    CarStatus getCarStatus();
}