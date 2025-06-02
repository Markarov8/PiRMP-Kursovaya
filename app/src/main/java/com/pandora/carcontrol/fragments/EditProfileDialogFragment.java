package com.pandora.carcontrol.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.pandora.carcontrol.R;
import com.pandora.carcontrol.data.models.CarProfile;
import com.pandora.carcontrol.databinding.DialogEditProfileBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class EditProfileDialogFragment extends DialogFragment {

    private DialogEditProfileBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_PandoraCarControl_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditProfileBinding.inflate(inflater, container, false);
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

        // Observe profile
        viewModel.getCarProfile().observe(getViewLifecycleOwner(), this::updateUI);

        // Save button
        binding.saveButton.setOnClickListener(v -> {
            saveProfile();
            dismiss();
        });
    }

    private void updateUI(CarProfile profile) {
        binding.carNameInput.setText(profile.getCarName());
        binding.userNameInput.setText(profile.getUserName());
        binding.userLastNameInput.setText(profile.getUserLastName());
        binding.verificationCodeInput.setText(profile.getVerificationCode());
    }

    private void saveProfile() {
        String carName = binding.carNameInput.getText().toString();
        String userName = binding.userNameInput.getText().toString();
        String userLastName = binding.userLastNameInput.getText().toString();
        String verificationCode = binding.verificationCodeInput.getText().toString();

        CarProfile currentProfile = viewModel.getCarProfile().getValue();
        if (currentProfile != null) {
            CarProfile updatedProfile = new CarProfile(
                    carName,
                    userName,
                    userLastName,
                    "1234@mail.ru",
                    "DHNR90P",
                    verificationCode,
                    currentProfile.getAppVersion()
            );

            viewModel.updateCarProfile(updatedProfile);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}