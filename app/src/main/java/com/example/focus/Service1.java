package com.example.focus;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

public class Service1 extends Service {
    //IMPORTANT: Include the service in AndroidManifest.xml.
    MediaPlayer mp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //used by applications (example-activity) to bind to the service and interact with it.
        //IBinder is an object that defines programming interface to interact with service.
        return null;  //no interface between application and service
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //intent is the service requested, flags--additional data about service requested, startId--unique id for specific request to start
        //This is called when a service is requested by an activity.
        // Let it continue running until it is stopped.
        mp=MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI); //Media player object to play default ringtone.
        mp.setLooping(true);
        mp.start();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY; //status of program--created a fresh copy of service.
    }

    @Override
    public void onDestroy() { //called when a service is no longer in use.
        super.onDestroy(); //clean up resources.
        mp.stop();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
}

