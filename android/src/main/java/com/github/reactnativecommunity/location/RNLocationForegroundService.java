package com.github.reactnativecommunity.location;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RNLocationForegroundService extends Service {
    private static final String CHANNEL_ID = "RNLocationForegroundService";
    private static final int NOTIFICATION_ID = 1001;
    private static RNLocationProvider locationProvider = null;
    public static boolean locationProviderRunning = false;

    public static void setLocationProvider(RNLocationProvider provider) {
        locationProvider = provider;
    }

    public static void restartLocationProvider() {
        if (locationProvider != null && locationProviderRunning) {
            locationProvider.stopUpdatingLocation();
            locationProvider.startUpdatingLocation();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (locationProvider != null) {
            locationProviderRunning = true;
            locationProvider.startUpdatingLocation();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (locationProvider != null) {
            locationProviderRunning = false;
            locationProvider.stopUpdatingLocation();
        }

        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        if (locationProvider != null) {
            locationProviderRunning = false;
            locationProvider.stopUpdatingLocation();
        }

        super.onTaskRemoved(intent);
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification() {
        Context context = getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

        int code = (int) (System.currentTimeMillis() & 0xfffffff);
        int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                ? PendingIntent.FLAG_MUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, code, intent, flag);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Location Service Running")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW);

        int iconSource = getResourceIdForResourceName("ic_launcher", context);
        notificationBuilder.setSmallIcon(iconSource);

        return notificationBuilder.build();
    }

    private int getResourceIdForResourceName(String resourceName, Context context) {
        String packageName = context.getPackageName();
        int resourceIdDrawable = context.getResources().getIdentifier(resourceName, "drawable", packageName);
        int resourceIdMipmap = context.getResources().getIdentifier(resourceName, "mipmap", packageName);
        return resourceIdDrawable | resourceIdMipmap;
    }
}
