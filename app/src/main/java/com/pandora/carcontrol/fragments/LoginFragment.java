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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pandora.carcontrol.databinding.FragmentLoginBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private MainViewModel viewModel;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> {
            //Данные вводимые пользователем при авторизации
            String username = binding.usernameInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();
            if (!username.isEmpty() && !password.isEmpty()) {

                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    viewModel.preferenceManager.setLoggedIn(true);
                                    viewModel.isAuthenticated.setValue(true);
                                    Toast.makeText(requireContext(),"Логин успешен", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            else {
                Toast.makeText(requireContext(), "Пожалуйста, введите логин и пароль", Toast.LENGTH_LONG).show();
            }
        });

        binding.forgotPassword.setOnClickListener(v -> {
            // Показ уведомления
            Toast.makeText(requireContext(), "Логин, Пароль: " + "1234@mail.ru"
                    + ", " + "111111", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}