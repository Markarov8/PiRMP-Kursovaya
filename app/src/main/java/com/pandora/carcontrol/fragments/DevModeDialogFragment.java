package com.pandora.carcontrol.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.pandora.carcontrol.R;
import com.pandora.carcontrol.data.models.CarStatus;
import com.pandora.carcontrol.data.models.Location;
import com.pandora.carcontrol.databinding.DialogDevModeBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DevModeDialogFragment extends DialogFragment {

    private DialogDevModeBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_PandoraCarControl_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDevModeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Close button
        binding.closeButton.setOnClickListener(v -> dismiss());

        // Observe car status
        viewModel.getCarStatus().observe(getViewLifecycleOwner(), this::updateUI);

        // Setup listeners
        setupListeners();
    }

    private void setupListeners() {
        // Engine running switch
        binding.engineRunningSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CarStatus currentStatus = viewModel.getCarStatus().getValue();
            if (currentStatus != null) {
                CarStatus updatedStatus = new CarStatus(
                        isChecked,
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
                viewModel.updateCarStatus(updatedStatus);
            }
        });

        // Car locked switch
        binding.carLockedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CarStatus currentStatus = viewModel.getCarStatus().getValue();
            if (currentStatus != null) {
                CarStatus updatedStatus = new CarStatus(
                        currentStatus.isRunning(),
                        isChecked,
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
                viewModel.updateCarStatus(updatedStatus);
            }
        });

        // Alarm active switch
        binding.alarmActiveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CarStatus currentStatus = viewModel.getCarStatus().getValue();
            if (currentStatus != null) {
                CarStatus updatedStatus = new CarStatus(
                        currentStatus.isRunning(),
                        currentStatus.isLocked(),
                        isChecked,
                        currentStatus.getEngineTemp(),
                        currentStatus.getCabinTemp(),
                        currentStatus.getOutsideTemp(),
                        currentStatus.getFuelLevel(),
                        currentStatus.getBatteryLevel(),
                        currentStatus.getLocation(),
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                        currentStatus.isHasConnection()
                );
                viewModel.updateCarStatus(updatedStatus);
            }
        });

        // Connection switch
        binding.connectionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.toggleConnection();
        });

        // Engine temperature seekbar
        binding.engineTempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress - 20; // -20 to 120
                binding.engineTempValue.setText(String.format("%.0f°C", value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = seekBar.getProgress() - 20; // -20 to 120
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            value,
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // Cabin temperature seekbar
        binding.cabinTempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress - 20; // -20 to 50
                binding.cabinTempValue.setText(String.format("%.0f°C", value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = seekBar.getProgress() - 20; // -20 to 50
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            value,
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // Outside temperature seekbar
        binding.outsideTempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress - 40; // -40 to 50
                binding.outsideTempValue.setText(String.format("%.0f°C", value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = seekBar.getProgress() - 40; // -40 to 50
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            value,
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // Fuel level seekbar
        binding.fuelLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.fuelLevelValue.setText(String.format("%dл", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = seekBar.getProgress();
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            value,
                            currentStatus.getBatteryLevel(),
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // Battery level seekbar
        binding.batteryLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress / 10.0f; // 0 to 20
                binding.batteryLevelValue.setText(String.format("%.1fV", value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = seekBar.getProgress() / 10.0f; // 0 to 20
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            value,
                            currentStatus.getLocation(),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // GPS coordinates switch
        binding.gpsCoordinatesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CarStatus currentStatus = viewModel.getCarStatus().getValue();
            if (currentStatus != null) {
                Location currentLocation = currentStatus.getLocation();
                Location newLocation;

                if (isChecked) {
                    // Set default coordinates
                    newLocation = new Location(37.7749, -122.4194, currentLocation.getAddress());
                } else {
                    // Clear coordinates
                    newLocation = new Location(null, null, currentLocation.getAddress());
                }

                CarStatus updatedStatus = new CarStatus(
                        currentStatus.isRunning(),
                        currentStatus.isLocked(),
                        currentStatus.isAlarm(),
                        currentStatus.getEngineTemp(),
                        currentStatus.getCabinTemp(),
                        currentStatus.getOutsideTemp(),
                        currentStatus.getFuelLevel(),
                        currentStatus.getBatteryLevel(),
                        newLocation,
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                        currentStatus.isHasConnection()
                );
                viewModel.updateCarStatus(updatedStatus);

                // Update UI for location fields
                updateLocationFields(isChecked);
            }
        });

        // Address input
        binding.addressInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String address = binding.addressInput.getText().toString();
                CarStatus currentStatus = viewModel.getCarStatus().getValue();
                if (currentStatus != null) {
                    Location currentLocation = currentStatus.getLocation();
                    Location newLocation = new Location(
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            address
                    );

                    CarStatus updatedStatus = new CarStatus(
                            currentStatus.isRunning(),
                            currentStatus.isLocked(),
                            currentStatus.isAlarm(),
                            currentStatus.getEngineTemp(),
                            currentStatus.getCabinTemp(),
                            currentStatus.getOutsideTemp(),
                            currentStatus.getFuelLevel(),
                            currentStatus.getBatteryLevel(),
                            newLocation,
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                            currentStatus.isHasConnection()
                    );
                    viewModel.updateCarStatus(updatedStatus);
                }
            }
        });

        // Latitude input
        binding.latitudeInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    double latitude = Double.parseDouble(binding.latitudeInput.getText().toString());
                    CarStatus currentStatus = viewModel.getCarStatus().getValue();
                    if (currentStatus != null) {
                        Location currentLocation = currentStatus.getLocation();
                        Location newLocation = new Location(
                                latitude,
                                currentLocation.getLongitude(),
                                currentLocation.getAddress()
                        );

                        CarStatus updatedStatus = new CarStatus(
                                currentStatus.isRunning(),
                                currentStatus.isLocked(),
                                currentStatus.isAlarm(),
                                currentStatus.getEngineTemp(),
                                currentStatus.getCabinTemp(),
                                currentStatus.getOutsideTemp(),
                                currentStatus.getFuelLevel(),
                                currentStatus.getBatteryLevel(),
                                newLocation,
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                                currentStatus.isHasConnection()
                        );
                        viewModel.updateCarStatus(updatedStatus);
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, ignore
                }
            }
        });

        // Longitude input
        binding.longitudeInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    double longitude = Double.parseDouble(binding.longitudeInput.getText().toString());
                    CarStatus currentStatus = viewModel.getCarStatus().getValue();
                    if (currentStatus != null) {
                        Location currentLocation = currentStatus.getLocation();
                        Location newLocation = new Location(
                                currentLocation.getLatitude(),
                                longitude,
                                currentLocation.getAddress()
                        );

                        CarStatus updatedStatus = new CarStatus(
                                currentStatus.isRunning(),
                                currentStatus.isLocked(),
                                currentStatus.isAlarm(),
                                currentStatus.getEngineTemp(),
                                currentStatus.getCabinTemp(),
                                currentStatus.getOutsideTemp(),
                                currentStatus.getFuelLevel(),
                                currentStatus.getBatteryLevel(),
                                newLocation,
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(new Date()),
                                currentStatus.isHasConnection()
                        );
                        viewModel.updateCarStatus(updatedStatus);
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, ignore
                }
            }
        });
    }

    private void updateUI(CarStatus status) {
        if (status == null) return;

        // Update switches
        binding.engineRunningSwitch.setChecked(status.isRunning());
        binding.carLockedSwitch.setChecked(status.isLocked());
        binding.alarmActiveSwitch.setChecked(status.isAlarm());
        binding.connectionSwitch.setChecked(status.isHasConnection());

        // Update seekbars
        binding.engineTempSeekbar.setProgress((int) status.getEngineTemp() + 20);
        binding.engineTempValue.setText(String.format("%.0f°C", status.getEngineTemp()));

        binding.cabinTempSeekbar.setProgress((int) status.getCabinTemp() + 20);
        binding.cabinTempValue.setText(String.format("%.0f°C", status.getCabinTemp()));

        binding.outsideTempSeekbar.setProgress((int) status.getOutsideTemp() + 40);
        binding.outsideTempValue.setText(String.format("%.0f°C", status.getOutsideTemp()));

        binding.fuelLevelSeekbar.setProgress((int) status.getFuelLevel());
        binding.fuelLevelValue.setText(String.format("%.0fл", status.getFuelLevel()));

        binding.batteryLevelSeekbar.setProgress((int) (status.getBatteryLevel() * 10));
        binding.batteryLevelValue.setText(String.format("%.1fV", status.getBatteryLevel()));

        // Update location fields
        boolean hasCoordinates = status.getLocation().getLatitude() != null;
        binding.gpsCoordinatesSwitch.setChecked(hasCoordinates);
        updateLocationFields(hasCoordinates);

        binding.addressInput.setText(status.getLocation().getAddress());

        if (hasCoordinates) {
            binding.latitudeInput.setText(String.valueOf(status.getLocation().getLatitude()));
            binding.longitudeInput.setText(String.valueOf(status.getLocation().getLongitude()));
        }
    }

    private void updateLocationFields(boolean hasCoordinates) {
        binding.latitudeContainer.setVisibility(hasCoordinates ? View.VISIBLE : View.GONE);
        binding.longitudeContainer.setVisibility(hasCoordinates ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}