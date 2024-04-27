package com.example.focus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private final List<String> schedules;
    private final ScheduleAdapterListener listener;

    public ScheduleAdapter(List<String> schedules, ScheduleAdapterListener listener) {
        this.schedules = schedules;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        String scheduleString = schedules.get(position);
        holder.scheduleText.setText(formatScheduleDisplay(scheduleString));

        holder.deleteButton.setOnClickListener(view -> {
            listener.onDeleteSchedule(position, scheduleString);
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    private String formatScheduleDisplay(String scheduleString) {
        String[] parts = scheduleString.split(",");
        String timeRange = parts[0];
        StringBuilder daysBuilder = new StringBuilder(" on ");
        String[] dayLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i = 1; i < parts.length; i++) {
            if ("1".equals(parts[i])) {
                if (daysBuilder.length() > 4) { // Add comma if not the first day
                    daysBuilder.append(", ");
                }
                daysBuilder.append(dayLabels[i - 1]);
            }
        }

        return timeRange + daysBuilder.toString();
    }

    public interface ScheduleAdapterListener {
        void onDeleteSchedule(int position, String schedule);
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView scheduleText;
        Button deleteButton;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
