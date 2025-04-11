package com.pandora.carcontrol.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pandora.carcontrol.data.database.AppDatabase;
import com.pandora.carcontrol.data.database.CarCommandDao;
import com.pandora.carcontrol.data.database.CarHistoryDao;
import com.pandora.carcontrol.data.database.CarProfileDao;
import com.pandora.carcontrol.data.database.CarSettingsDao;
import com.pandora.carcontrol.data.database.CarStatusDao;
import com.pandora.carcontrol.data.models.CarCommand;
import com.pandora.carcontrol.data.models.CarHistory;
import com.pandora.carcontrol.data.models.CarProfile;
import com.pandora.carcontrol.data.models.CarSettings;
import com.pandora.carcontrol.data.models.CarStatus;
import com.pandora.carcontrol.data.models.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CarRepository {

    private final CarStatusDao statusDao;
    private final CarSettingsDao settingsDao;
    private final CarProfileDao profileDao;
    private final CarCommandDao commandDao;
    private final CarHistoryDao historyDao;

    private final MutableLiveData<CarStatus> carStatus = new MutableLiveData<>();
    private final MutableLiveData<CarSettings> carSettings = new MutableLiveData<>();
    private final MutableLiveData<CarProfile> carProfile = new MutableLiveData<>();
    private final MutableLiveData<List<CarCommand>> commands = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<CarHistory>> history = new MutableLiveData<>(new ArrayList<>());

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Random random = new Random();

    // Periodic update handler
    private final Handler updateHandler = new Handler(Looper.getMainLooper());
    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateRandomValues();
            updateHandler.postDelayed(this, 5000); // Update every 5 seconds
        }
    };

    public CarRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        statusDao = database.carStatusDao();
        settingsDao = database.carSettingsDao();
        profileDao = database.carProfileDao();
        commandDao = database.carCommandDao();
        historyDao = database.carHistoryDao();

        // Initialize with default values
        initializeData();

        // Start periodic updates
        startPeriodicUpdates();
    }

    private void initializeData() {
        executor.execute(() -> {
            // Check if we have data in the database
            CarStatus status = statusDao.getCarStatus();
            if (status == null) {
                // Initialize with default values
                status = getDefaultCarStatus();
                statusDao.insert(status);
            }
            carStatus.postValue(status);

            CarSettings settings = settingsDao.getCarSettings();
            if (settings == null) {
                settings = getDefaultCarSettings();
                settingsDao.insert(settings);
            }
            carSettings.postValue(settings);

            CarProfile profile = profileDao.getCarProfile();
            if (profile == null) {
                profile = getDefaultCarProfile();
                profileDao.insert(profile);
            }
            carProfile.postValue(profile);

            // Load history
            List<CarHistory> historyList = historyDao.getAllHistory();
            history.postValue(historyList);

            // Load commands
            List<CarCommand> commandList = commandDao.getAllCommands();
            commands.postValue(commandList);
        });
    }

    private void startPeriodicUpdates() {
        updateHandler.post(updateRunnable);
    }

    private void stopPeriodicUpdates() {
        updateHandler.removeCallbacks(updateRunnable);
    }

    private void updateRandomValues() {
        CarStatus currentStatus = carStatus.getValue();
        if (currentStatus != null && currentStatus.isHasConnection() && !isPendingCommand()) {
            // Only update if we have connection and no pending commands
            CarStatus updatedStatus = new CarStatus(
                    currentStatus.isRunning(),
                    currentStatus.isLocked(),
                    currentStatus.isAlarm(),
                    // Small random changes to make it look like real-time data
                    Math.max(50, Math.min(110, currentStatus.getEngineTemp() + (random.nextFloat() - 0.5f))),
                    Math.max(15, Math.min(30, currentStatus.getCabinTemp() + (random.nextFloat() - 0.5f) * 0.4f)),
                    Math.max(0, Math.min(25, currentStatus.getOutsideTemp() + (random.nextFloat() - 0.5f) * 0.2f)),
                    Math.max(0, Math.min(100, currentStatus.getFuelLevel() + (currentStatus.isRunning() ? (random.nextFloat() * -0.05f) : 0))),
                    Math.max(0, Math.min(100, currentStatus.getBatteryLevel() + (currentStatus.isRunning() ? (random.nextFloat() * 0.1f) : (random.nextFloat() - 0.5f) * 0.2f))),
                    currentStatus.getLocation(),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                    currentStatus.isHasConnection()
            );

            updateCarStatus(updatedStatus);
        }
    }

    private boolean isPendingCommand() {
        List<CarCommand> commandList = commands.getValue();
        if (commandList != null) {
            for (CarCommand command : commandList) {
                if ("PENDING".equals(command.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Car Status
    public LiveData<CarStatus> getCarStatus() {
        return carStatus;
    }

    public void updateCarStatus(CarStatus status) {
        executor.execute(() -> {
            statusDao.update(status);
            handler.post(() -> carStatus.setValue(status));
        });
    }

    // Car Settings
    public LiveData<CarSettings> getCarSettings() {
        return carSettings;
    }

    public void updateCarSettings(CarSettings settings) {
        executor.execute(() -> {
            settingsDao.update(settings);
            handler.post(() -> carSettings.setValue(settings));
        });
    }

    // Car Profile
    public LiveData<CarProfile> getCarProfile() {
        return carProfile;
    }

    public void updateCarProfile(CarProfile profile) {
        executor.execute(() -> {
            profileDao.update(profile);
            handler.post(() -> carProfile.setValue(profile));
        });
    }

    // Commands
    public void sendCommand(String commandType) {
        executor.execute(() -> {
            // Create a new command
            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date());
            CarCommand command = new CarCommand(0, commandType, timestamp, "PENDING", null);

            // Insert into database
            long id = commandDao.insert(command);
            command.setId(id);

            // Update LiveData
            List<CarCommand> currentCommands = commands.getValue();
            if (currentCommands == null) {
                currentCommands = new ArrayList<>();
            }
            currentCommands.add(0, command);
            List<CarCommand> finalCurrentCommands = currentCommands;
            handler.post(() -> commands.setValue(finalCurrentCommands));

            // Process command after delay (simulate network request)
            handler.postDelayed(() -> processCommand(command), 2000);
        });
    }

    private void processCommand(CarCommand command) {
        executor.execute(() -> {
            CarStatus currentStatus = carStatus.getValue();
            if (currentStatus == null) return;

            // Check if we have connection
            if (!currentStatus.isHasConnection()) {
                // Update command status to failed
                command.setStatus("FAILED");
                command.setMessage("Нет связи с автомобилем");
                commandDao.update(command);

                // Update LiveData
                List<CarCommand> currentCommands = commands.getValue();
                if (currentCommands != null) {
                    for (int i = 0; i < currentCommands.size(); i++) {
                        if (currentCommands.get(i).getId() == command.getId()) {
                            currentCommands.set(i, command);
                            break;
                        }
                    }
                    handler.post(() -> commands.setValue(currentCommands));
                }
                return;
            }

            // Update command status to success
            command.setStatus("SUCCESS");
            commandDao.update(command);

            // Update LiveData
            List<CarCommand> currentCommands = commands.getValue();
            if (currentCommands != null) {
                for (int i = 0; i < currentCommands.size(); i++) {
                    if (currentCommands.get(i).getId() == command.getId()) {
                        currentCommands.set(i, command);
                        break;
                    }
                }
                handler.post(() -> commands.setValue(currentCommands));
            }

            // Update car status based on command
            CarStatus updatedStatus = null;
            CarHistory historyEntry = null;

            switch (command.getType()) {
                case "START_ENGINE":
                    updatedStatus = new CarStatus(
                            true, // isRunning
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );

                    historyEntry = new CarHistory(
                            0,
                            "ENGINE_START",
                            command.getTimestamp(),
                            "Запуск двигателя",
                            null
                    );
                    break;

                case "STOP_ENGINE":
                    updatedStatus = new CarStatus(
                            false, // isRunning
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );

                    historyEntry = new CarHistory(
                            0,
                            "ENGINE_STOP",
                            command.getTimestamp(),
                            "Остановка двигателя",
                            null
                    );
                    break;

                case "LOCK":
                    updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            true, // isLocked
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );

                    historyEntry = new CarHistory(
                            0,
                            "LOCK",
                            command.getTimestamp(),
                            "Постановка под охрану",
                            "Slave-режим"
                    );
                    break;

                case "UNLOCK":
                    updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            false, // isLocked
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );

                    historyEntry = new CarHistory(
                            0,
                            "UNLOCK",
                            command.getTimestamp(),
                            "Снятие с охраны",
                            "Slave-режим"
                    );
                    break;

                case "TRIGGER_ALARM":
                    updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            true, // isAlarm
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );

                    historyEntry = new CarHistory(
                            0,
                            "ALARM",
                            command.getTimestamp(),
                            "Тревога",
                            null
                    );

                    // Auto-reset alarm after 5 seconds
                    handler.postDelayed(() -> {
                        CarStatus alarmStatus = carStatus.getValue();
                        if (alarmStatus != null && alarmStatus.isAlarm()) {
                            CarStatus resetStatus = new CarStatus(
                                    alarmStatus.isRunning(),
                                    alarmStatus.isLocked(),
                                    false, // Reset alarm
                                    alarmStatus.getEngineTemp(),
                                    alarmStatus.getCabinTemp(),
                                    alarmStatus.getOutsideTemp(),
                                    alarmStatus.getFuelLevel(),
                                    alarmStatus.getBatteryLevel(),
                                    alarmStatus.getLocation(),
                                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                                    alarmStatus.isHasConnection()
                            );
                            updateCarStatus(resetStatus);
                        }
                    }, 5000);
                    break;

                case "LOCATE":
                    historyEntry = new CarHistory(
                            0,
                            "LOCATE",
                            command.getTimestamp(),
                            "Запрос местоположения",
                            null
                    );
                    break;
            }

            // Update status if needed
            if (updatedStatus != null) {
                updateCarStatus(updatedStatus);
            }

            // Add to history if we have an entry
            if (historyEntry != null) {
                long historyId = historyDao.insert(historyEntry);
                historyEntry.setId(historyId);

                List<CarHistory> currentHistory = history.getValue();
                if (currentHistory == null) {
                    currentHistory = new ArrayList<>();
                }
                currentHistory.add(0, historyEntry);

                // Limit history to 100 entries
                if (currentHistory.size() > 100) {
                    List<CarHistory> trimmedHistory = currentHistory.subList(0, 100);
                    handler.post(() -> history.setValue(trimmedHistory));
                } else {
                    List<CarHistory> finalCurrentHistory = currentHistory;
                    handler.post(() -> history.setValue(finalCurrentHistory));
                }
            }
        });
    }

    public LiveData<List<CarCommand>> getCommands() {
        return commands;
    }

    // History
    public LiveData<List<CarHistory>> getHistory() {
        return history;
    }

    public void clearHistory() {
        executor.execute(() -> {
            historyDao.deleteAll();
            handler.post(() -> history.setValue(new ArrayList<>()));
        });
    }

    // Connection
    public void toggleConnection() {
        executor.execute(() -> {
            CarStatus currentStatus = carStatus.getValue();
            if (currentStatus == null) return;

            boolean newConnectionState = !currentStatus.isHasConnection();

            // Update status
            CarStatus updatedStatus = new CarStatus(
                    currentStatus.isRunning(),
                    currentStatus.isLocked(),
                    currentStatus.isAlarm(),
                    currentStatus.getEngineTemp(),
                    currentStatus.getCabinTemp(),
                    currentStatus.getOutsideTemp(),
                    currentStatus.getFuelLevel(),
                    currentStatus.getBatteryLevel(),
                    currentStatus.getLocation(),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                    newConnectionState
            );

            updateCarStatus(updatedStatus);

            // Add history entry
            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date());
            CarHistory historyEntry = new CarHistory(
                    0,
                    newConnectionState ? "GSM_RESTORED" : "GSM_LOST",
                    timestamp,
                    newConnectionState ? "Восстановление GSM-связи" : "Потеря GSM-связи",
                    null
            );

            long historyId = historyDao.insert(historyEntry);
            historyEntry.setId(historyId);

            List<CarHistory> currentHistory = history.getValue();
            if (currentHistory == null) {
                currentHistory = new ArrayList<>();
            }
            currentHistory.add(0, historyEntry);

            // Limit history to 100 entries
            if (currentHistory.size() > 100) {
                List<CarHistory> trimmedHistory = currentHistory.subList(0, 100);
                handler.post(() -> history.setValue(trimmedHistory));
            } else {
                List<CarHistory> finalCurrentHistory = currentHistory;
                handler.post(() -> history.setValue(finalCurrentHistory));
            }
        });
    }

    // Default values
    private CarStatus getDefaultCarStatus() {
        return new CarStatus(
                false, // isRunning
                true, // isLocked
                false, // alarm
                66.0f, // engineTemp
                28.0f, // cabinTemp
                9.0f, // outsideTemp
                18.0f, // fuelLevel
                14.1f, // batteryLevel
                new Location(null, null, "123 Main St, San Francisco, CA"), // location
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()), // lastUpdate
                true // hasConnection
        );
    }

    private CarSettings getDefaultCarSettings() {
        return new CarSettings(
                false, // autoStartEnabled
                -10.0f, // autoStartTemperature
                "07:30", // autoStartTime
                true, // notifyOnAlarm
                true, // notifyOnUnlock
                false, // geofenceEnabled
                500.0f, // geofenceRadius
                true, // autoHeat
                true, // autoCool
                22.0f, // targetTemp
                41.27f, // simBalance
                "+7916*******" // simNumber
        );
    }

    private CarProfile getDefaultCarProfile() {
        return new CarProfile(
                "Рыжик", // carName
                "Артём", // userName
                "Маркаров", // userLastName
                "1078****", // accountNumber
                "DX****", // deviceCode
                "1.51", // verificationCode
                "1.30****" // appVersion
        );
    }
}