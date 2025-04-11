package com.pandora.carcontrol.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.pandora.carcontrol.databinding.FragmentDashboardBinding;
import com.pandora.carcontrol.utils.DateFormatter;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private MainViewModel viewModel;
    private int tapCount = 0;
    private final Handler tapResetHandler = new Handler(Looper.getMainLooper());
    private final String SUPPORT_PHONE = "1111";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupObservers();
        setupClickListeners();
    }

    private void setupObservers() {
// Observe car status
        viewModel.getCarStatus().observe(getViewLifecycleOwner(), this::updateUI);

// Observe car settings
        viewModel.getCarSettings().observe(getViewLifecycleOwner(), settings -> {
            binding.infoBar.simBalance.setText(String.format("%.2f₽", settings.getSimBalance()));
        });

// Observe car profile
        viewModel.getCarProfile().observe(getViewLifecycleOwner(), profile -> {
            binding.header.carName.setText(profile.getCarName());
        });

// Observe dev mode
        viewModel.getIsDevMode().observe(getViewLifecycleOwner(), isDevMode -> {
            binding.devModeButton.setVisibility(isDevMode ? View.VISIBLE : View.GONE);
        });
    }

    private void setupClickListeners() {
        // Lock/Unlock button
        binding.unlockButton.setOnClickListener(v -> {
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

        // Phone button
        binding.phoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
            startActivity(intent);
        });

        // Message button
        binding.infoBar.messageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + SUPPORT_PHONE));
            startActivity(intent);
        });

        // Car image - secret dev mode activation
        binding.carImage.setOnClickListener(v -> {
            tapCount++;
            if (tapCount >= 5) {
                viewModel.toggleDevMode();
                tapCount = 0;
                Toast.makeText(requireContext(), "Режим разработчика активирован", Toast.LENGTH_SHORT).show();
            }

            // Reset tap count after 3 seconds
            tapResetHandler.removeCallbacksAndMessages(null);
            tapResetHandler.postDelayed(() -> tapCount = 0, 3000);
        });

        // Dev mode button
        binding.devModeButton.setOnClickListener(v -> {
            DevModeDialogFragment dialog = new DevModeDialogFragment();
            dialog.show(getChildFragmentManager(), "dev_mode_dialog");
        });
    }

    private void updateUI(CarStatus status) {
        if (status == null) return;

        // Update header
        String securityStatus = status.isLocked() ? "Под охраной" : "Без охраны";
        binding.header.carStatus.setText(DateFormatter.formatDateTime(status.getLastUpdate()) + " • " + securityStatus);

        // Update quick controls
        binding.lockIcon.setSelected(status.isLocked());
        binding.engineIcon.setSelected(status.isRunning());

        // Update status indicators
        binding.batteryValue.setText(String.format("%.1fV", status.getBatteryLevel()));
        binding.fuelValue.setText(String.format("%.0fл", status.getFuelLevel()));
        binding.engineTempValue.setText(String.format("%.0f°", status.getEngineTemp()));
        binding.cabinTempValue.setText(String.format("%.0f°", status.getCabinTemp()));
        binding.outsideTempValue.setText(String.format("%.0f°", status.getOutsideTemp()));

        // Update GPS status
        if (!status.isHasConnection()) {
            binding.noDataText.setVisibility(View.VISIBLE);
            binding.gpsText.setVisibility(View.GONE);
        } else {
            binding.noDataText.setVisibility(View.GONE);

            if (status.getLocation().getLatitude() == null) {
                binding.gpsText.setVisibility(View.VISIBLE);
            } else {
                binding.gpsText.setVisibility(View.GONE);
            }
        }

        // Update bottom controls
        binding.unlockIcon.setImageResource(status.isLocked() ? R.drawable.ic_lock_open : R.drawable.ic_lock);
//        binding.engineIcon.setImageResource(status.isRunning() ? R.drawable.ic_power : R.drawable.ic_power);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tapResetHandler.removeCallbacksAndMessages(null);
        binding = null;
    }
}