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
        String schedule = schedules.get(position);
        holder.scheduleText.setText(schedule);

        holder.deleteButton.setOnClickListener(view -> {
            listener.onDeleteSchedule(position, schedule);
        });
    }

    @Override
    public int getItemCount() {
        if (schedules != null) {
            return schedules.size();
        } else {
            return 0; // or return a default value
        }
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
