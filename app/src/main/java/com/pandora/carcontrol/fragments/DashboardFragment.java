package com.pandora.carcontrol.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
        // Наблюдение за статусом автомобиля
        viewModel.getCarStatus().observe(getViewLifecycleOwner(), this::updateUI);
        // Наблюдение за настройками автомобиля
        viewModel.getCarSettings().observe(getViewLifecycleOwner(), settings -> {
            binding.infoBar.simBalance.setText(String.format("%.2f₽", settings.getSimBalance()));
        });
        // Наблюдение за профилем автомобиля
        viewModel.getCarProfile().observe(getViewLifecycleOwner(), profile -> {
            binding.header.carName.setText(profile.getCarName());
        });
        // Наблюдение за режимом разработчика
        viewModel.getIsDevMode().observe(getViewLifecycleOwner(), isDevMode -> {
            binding.devModeButton.setVisibility(isDevMode ? View.VISIBLE : View.GONE);
        });
    }

    private void setupClickListeners() {
        // Кнопка блокировки/разблокировки автомобиля
        binding.unlockButton.setOnClickListener(v -> {
            CarStatus status = viewModel.getCarStatus().getValue();
            if (status != null) {
                viewModel.sendCommand(status.isLocked() ? "UNLOCK" : "LOCK");
            }
        });
        // Кнопка запуска/остановки двигателя
        binding.engineButton.setOnClickListener(v -> {
            CarStatus status = viewModel.getCarStatus().getValue();
            if (status != null) {
                viewModel.sendCommand(status.isRunning() ? "STOP_ENGINE" : "START_ENGINE");
            }
        });
        // Кнопка вызова телефона
        binding.phoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
            startActivity(intent);
        });
        // Кнопка вызова телефона в информационной панели
        binding.infoBar.phoneCallButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
            startActivity(intent);
        });
        // Кнопка отправки сообщения
        binding.infoBar.messageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + SUPPORT_PHONE));
            startActivity(intent);
        });
        // Кнопка автомобиля - секретная активация режима разработчика
        binding.carImage.setOnClickListener(v -> {
            tapCount++;
            if (tapCount >= 5) {
                viewModel.toggleDevMode();
                tapCount = 0;
                Toast.makeText(requireContext(), "Режим разработчика активирован", Toast.LENGTH_SHORT).show();
            }
            // Сброс счётчика нажатий через 2 секунды
            tapResetHandler.removeCallbacksAndMessages(null);
            tapResetHandler.postDelayed(() -> tapCount = 0, 2000);
        });
        // Кнопка режима разработчика
        binding.devModeButton.setOnClickListener(v -> {
            DevModeDialogFragment dialog = new DevModeDialogFragment();
            dialog.show(getChildFragmentManager(), "dev_mode_dialog");
        });
    }

    private void updateUI(CarStatus status) {
        if (status == null) return;

        // Обновление заголовка
        String securityStatus = status.isLocked() ? "Под охраной" : "Без охраны";
        binding.header.carStatus.setText(DateFormatter.formatDateTime(status.getLastUpdate()) + " • " + securityStatus);

        // Обновление быстрых управляющих элементов
        binding.lockIcon.setSelected(status.isLocked());
        binding.engineIcon.setSelected(status.isRunning());

        // Обновление индикаторов состояния
        binding.batteryValue.setText(String.format("%.1fV", status.getBatteryLevel()));
        binding.fuelValue.setText(String.format("%.0fл", status.getFuelLevel()));
        binding.engineTempValue.setText(String.format("%.0f°", status.getEngineTemp()));
        binding.cabinTempValue.setText(String.format("%.0f°", status.getCabinTemp()));
        binding.outsideTempValue.setText(String.format("%.0f°", status.getOutsideTemp()));

        // Обновление статуса GPS
        if (!status.isHasConnection()) {
            binding.noDataText.setVisibility(View.VISIBLE);
            binding.gpsText.setVisibility(View.GONE);
            binding.warningMessage.setVisibility(View.GONE);

            ImageView signalIcon = binding.header.signalIcon;
            signalIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.danger)));
        } else {
            ImageView signalIcon = binding.header.signalIcon;
            signalIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.text)));
            binding.noDataText.setVisibility(View.GONE);

            // Проверка всех условий для предупреждений
            StringBuilder warningMessage = new StringBuilder();

            // Проверка низкого уровня топлива
            if (status.getFuelLevel() < 12) {
                warningMessage.append("Низкий уровень топлива!\n\n");
                ImageView ic_droplet = binding.icDroplet;
                ic_droplet.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.danger)));
            } else {
                ImageView ic_droplet = binding.icDroplet;
                ic_droplet.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
            }

            // Проверка низкого вольтажа батареи
            if (status.getBatteryLevel() < 11.5) {
                warningMessage.append("Низкий вольтаж батареи!\nРекомендуется использовать пусковое устройство.\n\n");
                ImageView icBattery = binding.icBattery;
                icBattery.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.danger)));
            }
            // Проверка высокого вольтажа батареи
            else if (status.getBatteryLevel() > 15) {
                warningMessage.append("Высокий вольтаж батареи!\nРекомендуется обратиться в сервисный центр.\n\n");
                ImageView icBattery = binding.icBattery;
                icBattery.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.danger)));
            }
            else {
                ImageView icBattery = binding.icBattery;
                icBattery.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
            }

            // Проверка низкой температуры на улице
            if (status.getOutsideTemp() < 4) {
                warningMessage.append("Возможен гололёд!\nБудьте осторожны.");
            }

            // Установка сообщения или скрытие
            if (warningMessage.length() > 0) {
                binding.warningMessage.setText(warningMessage.toString().trim());
                binding.warningMessage.setVisibility(View.VISIBLE);
            } else {
                binding.warningMessage.setVisibility(View.GONE);
            }

            if (status.getLocation().getLatitude() == null) {
                binding.gpsText.setVisibility(View.VISIBLE);
            } else {
                binding.gpsText.setVisibility(View.GONE);
            }
        }

        // Обновление нижних управляющих элементов
        binding.unlockIcon.setImageResource(status.isLocked() ? R.drawable.ic_lock_open : R.drawable.ic_lock);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tapResetHandler.removeCallbacksAndMessages(null);
        binding = null;
    }
}