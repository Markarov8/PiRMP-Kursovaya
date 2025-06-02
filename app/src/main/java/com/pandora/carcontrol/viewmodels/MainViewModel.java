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
    public final PreferenceManager preferenceManager;
    public final MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isDevMode = new MutableLiveData<>(false);
    private final MutableLiveData<String> accountNumber = new MutableLiveData<>();
    private final MutableLiveData<String> verificationCode = new MutableLiveData<>();

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


    public void logout() {
        preferenceManager.setLoggedIn(false);
        isAuthenticated.setValue(false);
    }

    // Метод для получения статуса автомобиля
    public LiveData<CarStatus> getCarStatus() {
        return repository.getCarStatus();
    }

    // Метод для обновления статуса автомобиля
    public void updateCarStatus(CarStatus status) {
        repository.updateCarStatus(status);
    }

    // Метод для получения настроек автомобиля
    public LiveData<CarSettings> getCarSettings() {
        return repository.getCarSettings();
    }

    // Метод для обновления настроек автомобиля
    public void updateCarSettings(CarSettings settings) {
        repository.updateCarSettings(settings);
    }

    // Метод для получения профиля автомобиля
    public LiveData<CarProfile> getCarProfile() {
        return repository.getCarProfile();
    }

    // Метод для обновления профиля автомобиля
    public void updateCarProfile(CarProfile profile) {
        repository.updateCarProfile(profile);
        // Сохранение номера аккаунта и кода верификации в ViewModel
        accountNumber.setValue(profile.getAccountNumber());
        verificationCode.setValue(profile.getVerificationCode());
    }

    // Метод для отправки команды автомобилю
    public void sendCommand(String commandType) {
        repository.sendCommand(commandType);
    }

    // Метод для получения списка команд
    public LiveData<List<CarCommand>> getCommands() {
        return repository.getCommands();
    }

    // Метод для получения истории событий
    public LiveData<List<CarHistory>> getHistory() {
        return repository.getHistory();
    }

    // Метод для очистки истории событий
    public void clearHistory() {
        repository.clearHistory();
    }

    // Метод для получения состояния режима разработчика
    public LiveData<Boolean> getIsDevMode() {
        return isDevMode;
    }

    // Метод для переключения режима разработчика
    public void toggleDevMode() {
        isDevMode.setValue(isDevMode.getValue() != null && !isDevMode.getValue());
    }

    // Метод для переключения соединения с автомобилем
    public void toggleConnection() {
        repository.toggleConnection();
    }

    // Метод для получения номера аккаунта
    public LiveData<String> getAccountNumber() {
        return accountNumber;
    }

    // Метод для получения кода верификации
    public LiveData<String> getVerificationCode() {
        return verificationCode;
    }
}