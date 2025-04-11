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

import com.pandora.carcontrol.databinding.FragmentLoginBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Пожалуйста, введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show loading
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginButton.setEnabled(false);

            // Simulate network delay
            view.postDelayed(() -> {
                viewModel.login(username, password);
                binding.progressBar.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);
            }, 1500);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}