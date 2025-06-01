package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pandora.carcontrol.data.models.CarSettings;

// DAO для управления настройками автомобиля
@Dao
public interface CarSettingsDao {
    // Вставка настроек в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarSettings settings);

    // Обновление настроек в базе данных
    @Update
    void update(CarSettings settings);

    // Получение настроек из базы данных
    @Query("SELECT * FROM car_settings WHERE id = 1")
    CarSettings getCarSettings();
}