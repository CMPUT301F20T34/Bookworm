package com.example.bookworm;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID_1 = "requestNotification";
    public static final String CHANNEL_ID_2 = "requestAcceptNotification";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel requestNotification = new NotificationChannel(
                CHANNEL_ID_1,
                "Request Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            requestNotification.setDescription("Notifies the user of a request made on one of the books they own.");

            NotificationChannel requestAcceptNotification = new NotificationChannel(
                CHANNEL_ID_1,
                "Request Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            requestAcceptNotification.setDescription("Notifies the user that a request on a book has been accepted.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(requestNotification);
            manager.createNotificationChannel(requestAcceptNotification);
        }
    }
}
