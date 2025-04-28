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

import com.pandora.carcontrol.databinding.FragmentSettingsBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MainViewModel viewModel;
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
        // Edit profile button
        binding.editButton.setOnClickListener(v -> {
            EditProfileDialogFragment dialog = new EditProfileDialogFragment();
            dialog.show(getChildFragmentManager(), "edit_profile_dialog");
        });

        // Phone container
        binding.phoneContainer.setOnClickListener(v -> {
            String phoneNumber = binding.phoneText.getText().toString().split(" • ")[0].replace("SIM ", "");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        // Logout button
        binding.logoutButton.setOnClickListener(v -> {
            viewModel.logout();
        });
    }

    private void observeData() {
        // Observe profile
        viewModel.getCarProfile().observe(getViewLifecycleOwner(), profile -> {
            binding.profileId.setText(profile.getAccountNumber());
            binding.profileVersion.setText(profile.getVerificationCode());
            binding.profileAppVersion.setText("App Ver. " + profile.getAppVersion());

            binding.profileLastName.setText(profile.getUserLastName());
            binding.profileName.setText(profile.getUserName());
        });

        // Observe settings
        viewModel.getCarSettings().observe(getViewLifecycleOwner(), settings -> {
            binding.phoneText.setText(String.format("SIM %s • %.2f₽", settings.getSimNumber(), settings.getSimBalance()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}