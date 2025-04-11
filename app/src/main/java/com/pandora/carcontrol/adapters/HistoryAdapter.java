package com.pandora.carcontrol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandora.carcontrol.R;
import com.pandora.carcontrol.data.models.CarHistory;
import com.pandora.carcontrol.utils.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Object> items = new ArrayList<>();
    private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public void submitList(Map<String, List<CarHistory>> groupedHistory) {
        items.clear();

        for (Map.Entry<String, List<CarHistory>> entry : groupedHistory.entrySet()) {
            // Add date header
            items.add(entry.getKey());

            // Add history items
            items.addAll(entry.getValue());
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            String dateString = (String) items.get(position);

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
                if (date != null) {
                    headerHolder.dateText.setText(dateFormat.format(date));
                }
            } catch (ParseException e) {
                headerHolder.dateText.setText(dateString);
            }
        } else {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            CarHistory history = (CarHistory) items.get(position);

            // Set time
            itemHolder.timeText.setText(DateFormatter.formatTime(history.getTimestamp()));

            // Set icon and details based on history type
            switch (history.getType()) {
                case "ENGINE_START":
                    itemHolder.iconView.setImageResource(R.drawable.ic_power);
                    itemHolder.titleText.setText("Запуск двигателя");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                case "ENGINE_STOP":
                    itemHolder.iconView.setImageResource(R.drawable.ic_power);
                    itemHolder.titleText.setText("Остановка двигателя");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                case "LOCK":
                    itemHolder.iconView.setImageResource(R.drawable.ic_lock);
                    itemHolder.titleText.setText("Постановка под охрану");
                    if (history.getMode() != null) {
                        itemHolder.modeText.setText(history.getMode());
                        itemHolder.modeText.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.modeText.setVisibility(View.GONE);
                    }
                    break;
                case "UNLOCK":
                    itemHolder.iconView.setImageResource(R.drawable.ic_lock_open);
                    itemHolder.titleText.setText("Снятие с охраны");
                    if (history.getMode() != null) {
                        itemHolder.modeText.setText(history.getMode());
                        itemHolder.modeText.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.modeText.setVisibility(View.GONE);
                    }
                    break;
                case "ALARM":
                    itemHolder.iconView.setImageResource(R.drawable.ic_bell);
                    itemHolder.titleText.setText("Тревога");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                case "LOCATE":
                    itemHolder.iconView.setImageResource(R.drawable.ic_map_pin);
                    itemHolder.titleText.setText("Запрос местоположения");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                case "GSM_LOST":
                    itemHolder.iconView.setImageResource(R.drawable.ic_wifi_off);
                    itemHolder.titleText.setText("Потеря GSM-связи");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                case "GSM_RESTORED":
                    itemHolder.iconView.setImageResource(R.drawable.ic_wifi);
                    itemHolder.titleText.setText("Восстановление GSM-связи");
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
                default:
                    itemHolder.iconView.setImageResource(R.drawable.ic_check_square);
                    itemHolder.titleText.setText(history.getDetails());
                    itemHolder.modeText.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView iconView;
        TextView timeText;
        TextView titleText;
        TextView modeText;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.icon);
            timeText = itemView.findViewById(R.id.time_text);
            titleText = itemView.findViewById(R.id.title_text);
            modeText = itemView.findViewById(R.id.mode_text);
        }
    }
}