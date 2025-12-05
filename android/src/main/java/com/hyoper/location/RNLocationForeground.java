package com.hyoper.location;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
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

    private static RNLocationProvider provider = null;
    public static boolean providerWorking = false;

    public static void setProvider(RNLocationProvider _provider) {
        provider = _provider;
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

    public static void start(@NonNull Context context) {
        if (providerWorking) return;

        Intent intent = new Intent(context, RNLocationForeground.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stop(@NonNull Context context) {
        if (!providerWorking) return;

        Intent intent = new Intent(context, RNLocationForeground.class);
        context.stopService(intent);
    }

    public static void reset() {
        provider = null;
        providerWorking = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Notification notification = buildNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (provider != null) {
            provider.start();
            providerWorking = true;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (provider != null) {
            provider.stop();
            provider = null;
            providerWorking = false;
        }

        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        if (provider != null) {
            provider.stop();
            provider = null;
            providerWorking = false;
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

    private int getNotificationIcon(String name, Context context) {
        String packageName = context.getPackageName();
        int resourceId = context.getResources().getIdentifier(name, "mipmap", packageName);
        if (resourceId == 0) resourceId = context.getResources().getIdentifier(name, "drawable", packageName);
        if (resourceId == 0) resourceId = context.getApplicationInfo().icon;
        return resourceId;
    }

    private Notification buildNotification() {
        Context context = getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

        int code = (int) (System.currentTimeMillis() & 0xfffffff);
        int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                ? PendingIntent.FLAG_MUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;
        int icon = getNotificationIcon(notificationIcon, context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, code, intent, flag);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }
}
