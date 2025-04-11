package com.pandora.carcontrol.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pandora.carcontrol.data.CarRepository;
import com.pandora.carcontrol.data.models.CarCommand;
import com.pandora.carcontrol.data.models.CarHistory;
import com.pandora.carcontrol.data.models.CarProfile;
import com.pandora.carcontrol.data.models.CarSettings;
import com.pandora.carcontrol.data.models.CarStatus;
import com.pandora.carcontrol.utils.PreferenceManager;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final CarRepository repository;
    private final PreferenceManager preferenceManager;
    private final MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isDevMode = new MutableLiveData<>(false);

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        preferenceManager = new PreferenceManager(application);

        // Initialize authentication state from preferences
        isAuthenticated.setValue(preferenceManager.isLoggedIn());
    }

    // Authentication
    public LiveData<Boolean> getIsAuthenticated() {
        return isAuthenticated;
    }

    public void login(String username, String password) {
        // In a real app, you would validate credentials against a server
        // For this example, we'll accept any non-empty credentials
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            preferenceManager.setLoggedIn(true);
            isAuthenticated.setValue(true);
        }
    }

    public void logout() {
        preferenceManager.setLoggedIn(false);
        isAuthenticated.setValue(false);
    }

    // Car Status
    public LiveData<CarStatus> getCarStatus() {
        return repository.getCarStatus();
    }

    public void updateCarStatus(CarStatus status) {
        repository.updateCarStatus(status);
    }

    // Car Settings
    public LiveData<CarSettings> getCarSettings() {
        return repository.getCarSettings();
    }

    public void updateCarSettings(CarSettings settings) {
        repository.updateCarSettings(settings);
    }

    // Car Profile
    public LiveData<CarProfile> getCarProfile() {
        return repository.getCarProfile();
    }

    public void updateCarProfile(CarProfile profile) {
        repository.updateCarProfile(profile);
    }

    // Car Commands
    public void sendCommand(String commandType) {
        repository.sendCommand(commandType);
    }

    public LiveData<List<CarCommand>> getCommands() {
        return repository.getCommands();
    }

    // Car History
    public LiveData<List<CarHistory>> getHistory() {
        return repository.getHistory();
    }

    public void clearHistory() {
        repository.clearHistory();
    }

    // Dev Mode
    public LiveData<Boolean> getIsDevMode() {
        return isDevMode;
    }

    public void toggleDevMode() {
        isDevMode.setValue(isDevMode.getValue() != null && !isDevMode.getValue());
    }

    // Connection
    public void toggleConnection() {
        repository.toggleConnection();
    }
}