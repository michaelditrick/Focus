package com.example.focus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class sendNotification extends BroadcastReceiver {

    // The below parameters are some user-defined constants for creating notifications.
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    public static final String CHANNEL_ID = "Channel1";
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    public void onReceive (Context context , Intent intent) { //called when the BroadcastReceiver object receives an Intent broadcast.
        String alarmMessage = intent.getStringExtra("alarmMessage"); //Retrive the alarm message
        //create an instance of NotificationManager, call getSystemService(), passing in the NOTIFICATION_SERVICE constant.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;

        Notification notification = getNotification(context,"Focus time is set: " + alarmMessage);
        //Notification notificationEnd = getNotification(context,"Focus time ends now");

        //the device running the app has Android SDK 26 or up
        //createNotificationChannel method requires API level 26 or higher.
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            //Notifications may have different importance and priorities values.
            //https://developer.android.com/develop/ui/views/notifications/channels
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        notificationManager.notify(0 , notification) ; //Here notification id is user-defined. deliver the notification
        Log.d("===alarm pending===", "Yes.");
    }

    //User-defined method to create a notification and its contents.
    private Notification getNotification (Context context,String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( false ) ; //the notification does not cancel itself automatically
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setOngoing(true);
        Log.d("===Sending notification===", "Yes.");
        return builder.build() ;  //return notification object
    }
}

