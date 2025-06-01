package com.pandora.carcontrol.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.pandora.carcontrol.data.models.CarCommand;
import com.pandora.carcontrol.data.models.CarHistory;
import com.pandora.carcontrol.data.models.CarProfile;
import com.pandora.carcontrol.data.models.CarSettings;
import com.pandora.carcontrol.data.models.CarStatus;

// Определение базы данных Room
@Database(entities = {
        CarStatus.class,
        CarSettings.class,
        CarProfile.class,
        CarCommand.class,
        CarHistory.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "car_control_db";
    private static AppDatabase instance;

    // Получение DAO для управления статусом автомобиля
    public abstract CarStatusDao carStatusDao();

    // Получение DAO для управления настройками автомобиля
    public abstract CarSettingsDao carSettingsDao();

    // Получение DAO для управления профилем автомобиля
    public abstract CarProfileDao carProfileDao();

    // Получение DAO для управления командами автомобиля
    public abstract CarCommandDao carCommandDao();

    // Получение DAO для управления историей событий автомобиля
    public abstract CarHistoryDao carHistoryDao();

    // Получение экземпляра базы данных
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}