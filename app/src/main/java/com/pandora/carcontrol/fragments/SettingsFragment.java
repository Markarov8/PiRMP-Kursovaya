package com.pandora.carcontrol.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pandora.carcontrol.data.models.CarStatus;
import com.pandora.carcontrol.databinding.FragmentSettingsBinding;
import com.pandora.carcontrol.utils.DateFormatter;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MainViewModel viewModel;
    private final String SUPPORT_PHONE = "1111";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupClickListeners();
        observeData();
    }

    private void setupClickListeners() {
        // Редактирование профиля
        binding.editButton.setOnClickListener(v -> {
            EditProfileDialogFragment dialog = new EditProfileDialogFragment();
            dialog.show(getChildFragmentManager(), "edit_profile_dialog");
        });

        // Переход в Телефон и вставка номера
        binding.phoneContainer.setOnClickListener(v -> {
            String phoneNumber = binding.phoneText.getText().toString().split(" • ")[0].replace("SIM ", "");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        // Кнопка Выхода из профиля
        binding.logoutButton.setOnClickListener(v -> {
            viewModel.logout();
        });

        // Phone button
        binding.infoBar.phoneCallButton.setOnClickListener(v -> {
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
    }

    private void observeData() {
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

        // Обсерверы для профиля и настроек
        viewModel.getCarProfile().observe(getViewLifecycleOwner(), profile -> {
            binding.profileId.setText(profile.getAccountNumber());
            binding.profileVersion.setText(profile.getVerificationCode());
            binding.profileAppVersion.setText(profile.getAppVersion());
            binding.profileLastName.setText(profile.getUserLastName());
            binding.profileName.setText(profile.getUserName());
        });
        viewModel.getCarSettings().observe(getViewLifecycleOwner(), settings -> {
            binding.phoneText.setText(String.format("SIM %s • %.2f₽", settings.getSimNumber(), settings.getSimBalance()));
        });
    }

    private void updateUI(CarStatus status) {
        if (status == null) return;

        // Update header
        String securityStatus = status.isLocked() ? "Под охраной" : "Без охраны";
        binding.header.carStatus.setText(DateFormatter.formatDateTime(status.getLastUpdate()) + " • " + securityStatus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}