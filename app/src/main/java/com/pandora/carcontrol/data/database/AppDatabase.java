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

    public abstract CarStatusDao carStatusDao();
    public abstract CarSettingsDao carSettingsDao();
    public abstract CarProfileDao carProfileDao();
    public abstract CarCommandDao carCommandDao();
    public abstract CarHistoryDao carHistoryDao();

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