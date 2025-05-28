package com.pandora.carcontrol.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pandora.carcontrol.R;
import com.pandora.carcontrol.data.models.CarStatus;
import com.pandora.carcontrol.databinding.FragmentControlsBinding;
import com.pandora.carcontrol.utils.DateFormatter;
import com.pandora.carcontrol.viewmodels.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ControlsFragment extends Fragment {

    private FragmentControlsBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentControlsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupClickListeners();
        observeCarStatus();
    }

    private void setupClickListeners() {
        // Lock/Unlock button
        binding.lockButton.setOnClickListener(v -> {
            CarStatus status = viewModel.getCarStatus().getValue();
            if (status != null) {
                viewModel.sendCommand(status.isLocked() ? "UNLOCK" : "LOCK");
            }
        });

        // Engine start/stop button
        binding.engineButton.setOnClickListener(v -> {
            CarStatus status = viewModel.getCarStatus().getValue();
            if (status != null) {
                viewModel.sendCommand(status.isRunning() ? "STOP_ENGINE" : "START_ENGINE");
            }
        });

        // Alarm button
        binding.alarmButton.setOnClickListener(v -> {
            viewModel.sendCommand("TRIGGER_ALARM");
        });

        // Locate button
        binding.locateButton.setOnClickListener(v -> {
            viewModel.sendCommand("LOCATE");
        });

        // Dev mode button
//        binding.devModeButton.setOnClickListener(v -> {
//            viewModel.toggleDevMode();
//            Toast.makeText(requireContext(), "Режим разработчика активирован", Toast.LENGTH_SHORT).show();
//        });
    }

    private void observeCarStatus() {
        viewModel.getCarStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == null) return;

            // Update button texts
            binding.lockButtonText.setText(status.isLocked() ? "Открыть автомобиль" : "Закрыть автомобиль");
            binding.engineButtonText.setText(status.isRunning() ? "Остановить двигатель" : "Запустить двигатель");

            // Update status cards
            binding.engineTempValue.setText(String.format("%.1f°C", status.getEngineTemp()));
            binding.cabinTempValue.setText(String.format("%.1f°C", status.getCabinTemp()));
            binding.outsideTempValue.setText(String.format("%.1f°C", status.getOutsideTemp()));
            binding.fuelLevelValue.setText(String.format("%.0fл", status.getFuelLevel()));
            binding.batteryLevelValue.setText(String.format("%.1fV", status.getBatteryLevel()));

            // Update system status
            binding.engineStatus.setText(status.isRunning() ? "Запущен" : "Остановлен");
            binding.engineStatus.setTextColor(getResources().getColor(
                    status.isRunning() ? R.color.success : R.color.text_secondary, null));

            binding.securityStatus.setText(status.isLocked() ? "Включена" : "Выключена");
            binding.securityStatus.setTextColor(getResources().getColor(
                    status.isLocked() ? R.color.success : R.color.text_secondary, null));

            binding.alarmStatus.setText(status.isAlarm() ? "Активна" : "Неактивна");
            binding.alarmStatus.setTextColor(getResources().getColor(
                    status.isAlarm() ? R.color.warning : R.color.text_secondary, null));

            binding.gpsStatus.setText(status.getLocation().getLatitude() != null ? "Доступен" : "Нет данных");
            binding.gpsStatus.setTextColor(getResources().getColor(
                    status.getLocation().getLatitude() != null ? R.color.success : R.color.text_secondary, null));

            binding.connectionStatus.setText(status.isHasConnection() ? "Установлена" : "Отсутствует");
            binding.connectionStatus.setTextColor(getResources().getColor(
                    status.isHasConnection() ? R.color.success : R.color.text_secondary, null));

            binding.lastUpdateTime.setText(DateFormatter.formatTime(status.getLastUpdate()));
        });

        // Observe dev mode
//        viewModel.getIsDevMode().observe(getViewLifecycleOwner(), isDevMode -> {
//            binding.devModeButton.setVisibility(isDevMode ? View.VISIBLE : View.GONE);
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}