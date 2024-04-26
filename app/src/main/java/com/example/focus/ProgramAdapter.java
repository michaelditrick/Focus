package com.example.focus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {
    private Context context;
    private List<Drawable> appIcons;
    private List<String> appNames;
    private List<String> appUsageTime;
//    private String[] appUsageTime;
//    private int[] appIcons;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //TextView appName;
        TextView appTime;
        ImageView rowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //appName = itemView.findViewById(R.id.appName);
            appTime = itemView.findViewById(R.id.appTime);
            rowImage = itemView.findViewById(R.id.appIcon);
        }
    }

    // provide suitable constructor for program adapter
    //public ProgramAdapter(Context context, List<String> programAppNames, List<String> programAppTimes, List<Drawable> images){
    public ProgramAdapter(Context context, List<String> programAppTimes, List<Drawable> images){
        this.context = context;
        //this.appNames = programAppNames;
        this.appUsageTime = programAppTimes;
        this.appIcons = images;
    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create inflater object and inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_usage, parent,false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, int position) {
        // Replace contents of a view to be invoked by the layout manager
        // Get element from your dataset at this position and replace contents of the view with with that element
        //holder.appName.setText(appNames.get(position));
        holder.appTime.setText(appUsageTime.get(position));
        holder.rowImage.setImageDrawable(appIcons.get(position));
    }

    @Override
    public int getItemCount() {
        return appUsageTime.size();
    }
}
