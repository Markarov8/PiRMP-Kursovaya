package com.pandora.carcontrol;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pandora.carcontrol.databinding.ActivityMainBinding;
import com.pandora.carcontrol.fragments.ControlsFragment;
import com.pandora.carcontrol.fragments.DashboardFragment;
import com.pandora.carcontrol.fragments.HistoryFragment;
import com.pandora.carcontrol.fragments.LoginFragment;
import com.pandora.carcontrol.fragments.SettingsFragment;
import com.pandora.carcontrol.utils.PreferenceManager;
import com.pandora.carcontrol.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Для контейнера фрагментов
        View fragmentContainer = findViewById(R.id.fragment_container);
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(0, statusBarHeight, 0, 0);
            return insets;
        });

        // Initialize ViewModel
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Initialize PreferenceManager
        PreferenceManager preferenceManager = new PreferenceManager(this);

        // Setup bottom navigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);

        // Check if user is logged in
        if (preferenceManager.isLoggedIn()) {
            // User is logged in, show dashboard
            loadFragment(new DashboardFragment());
        } else {
            // User is not logged in, show login screen
            loadFragment(new LoginFragment());
            // Hide bottom navigation until logged in
            binding.bottomNavigation.setVisibility(android.view.View.GONE);
        }

        // Observe authentication state
        viewModel.getIsAuthenticated().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                binding.bottomNavigation.setVisibility(android.view.View.VISIBLE);
                loadFragment(new DashboardFragment());
                binding.bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
            } else {
                binding.bottomNavigation.setVisibility(android.view.View.GONE);
                loadFragment(new LoginFragment());
            }
        });
//      WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.nav_history) {
            fragment = new HistoryFragment();
        } else if (itemId == R.id.nav_controls) {
            fragment = new ControlsFragment();
        } else if (itemId == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}