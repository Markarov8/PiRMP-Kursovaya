package com.pandora.carcontrol.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pandora.carcontrol.data.models.CarProfile;

// DAO для управления профилем автомобиля
@Dao
public interface CarProfileDao {
    // Вставка профиля в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CarProfile profile);

    // Обновление профиля в базе данных
    @Update
    void update(CarProfile profile);

    // Получение профиля из базы данных
    @Query("SELECT * FROM car_profile WHERE id = 1")
    CarProfile getCarProfile();
}