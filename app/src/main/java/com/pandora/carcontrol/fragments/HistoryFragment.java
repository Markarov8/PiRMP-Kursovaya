package com.pandora.carcontrol.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pandora.carcontrol.adapters.HistoryAdapter;
import com.pandora.carcontrol.data.models.CarHistory;
import com.pandora.carcontrol.databinding.FragmentHistoryBinding;
import com.pandora.carcontrol.viewmodels.MainViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private MainViewModel viewModel;
    private HistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupRecyclerView();
        setupClickListeners();
        observeHistory();
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.historyRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.clearButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Очистить историю")
                    .setMessage("Вы уверены, что хотите очистить всю историю?")
                    .setPositiveButton("Очистить", (dialog, which) -> viewModel.clearHistory())
                    .setNegativeButton("Отмена", null)
                    .show();
        });
    }

    private void observeHistory() {
        viewModel.getHistory().observe(getViewLifecycleOwner(), historyList -> {
            if (historyList == null || historyList.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.historyRecyclerView.setVisibility(View.GONE);
                binding.clearButton.setVisibility(View.GONE);
            } else {
                binding.emptyView.setVisibility(View.GONE);
                binding.historyRecyclerView.setVisibility(View.VISIBLE);
                binding.clearButton.setVisibility(View.VISIBLE);

                // Group history items by date
                Map<String, List<CarHistory>> groupedHistory = groupHistoryByDate(historyList);
                adapter.submitList(groupedHistory);
            }
        });
    }

    private Map<String, List<CarHistory>> groupHistoryByDate(List<CarHistory> historyList) {
        Map<String, List<CarHistory>> groupedHistory = new TreeMap<>((o1, o2) -> o2.compareTo(o1)); // Sort by date descending

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        for (CarHistory item : historyList) {
            try {
                Date date = isoFormat.parse(item.getTimestamp());
                if (date != null) {
                    String dateString = dateFormat.format(date);

                    if (!groupedHistory.containsKey(dateString)) {
                        groupedHistory.put(dateString, new ArrayList<>());
                    }

                    groupedHistory.get(dateString).add(item);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return groupedHistory;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}