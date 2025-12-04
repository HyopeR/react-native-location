package com.hyoper.location;

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

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import com.hyoper.location.providers.RNLocationProvider;

public class RNLocationForeground extends Service {
    private static final String CHANNEL_ID = "RNLocationForeground";
    private static final String CHANNEL_NAME = "Location Service";

    private static final int NOTIFICATION_ID = 0x2000 + 1;
    private static String notificationIcon = "ic_launcher";
    private static String notificationTitle = "Location Service Running";
    private static String notificationContent = "Location is being used by the app.";

    private static RNLocationProvider locationProvider = null;
    public static boolean locationProviderRunning = false;

    public static void setLocationProvider(RNLocationProvider provider) {
        locationProvider = provider;
    }

    public static void setNotification(@Nullable ReadableMap map) {
        if (map != null && map.hasKey("icon") && map.getType("icon") == ReadableType.String) {
            notificationIcon = map.getString("icon");
        } else {
            notificationIcon = "ic_launcher";
        }

        if (map != null && map.hasKey("title") && map.getType("title") == ReadableType.String) {
            notificationTitle = map.getString("title");
        } else {
            notificationTitle = "Location Service Running";
        }

        if (map != null && map.hasKey("content") && map.getType("content") == ReadableType.String) {
            notificationContent = map.getString("content");
        } else {
            notificationContent = "Location is being used by the app.";
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
            locationProvider.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (locationProvider != null) {
            locationProviderRunning = false;
            locationProvider.stop();
            locationProvider = null;
        }

        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        if (locationProvider != null) {
            locationProviderRunning = false;
            locationProvider.stop();
            locationProvider = null;
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
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
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
        int icon = getResourceIdForResourceName(notificationIcon, context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, code, intent, flag);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    private int getResourceIdForResourceName(String resourceName, Context context) {
        String packageName = context.getPackageName();
        int resourceId = context.getResources().getIdentifier(resourceName, "mipmap", packageName);
        if (resourceId == 0) {
            resourceId = context.getResources().getIdentifier(resourceName, "drawable", packageName);
        }
        if (resourceId == 0) {
            resourceId = context.getApplicationInfo().icon;
        }
        return resourceId;
    }
}
