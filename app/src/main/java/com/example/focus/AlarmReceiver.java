package com.example.focus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// Create a new Java class file AlarmReceiver.java
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notification_id", 0);

        // Create an explicit intent for an Activity you want to start
        Intent resultIntent = new Intent(context, RoutinesActivity.class); // Replace TargetActivity.class with the activity you want to open.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a NotificationChannel if API level 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ScheduleChannel";
            String description = "Channel for schedule reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("SCHEDULE_NOTIFICATION", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "SCHEDULE_NOTIFICATION")
                .setSmallIcon(R.drawable.routine) // Replace with your notification icon.
                .setContentTitle("Schedule Reminder")
                .setContentText("Your scheduled event is about to start!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}

