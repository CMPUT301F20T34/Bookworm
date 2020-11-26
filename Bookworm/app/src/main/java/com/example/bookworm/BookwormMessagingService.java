package com.example.bookworm;

import android.app.Notification;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Handles a notification from the database when the app is in
 * the foreground. When in the background, the system tray handles the
 * notification.
 */
public class BookwormMessagingService extends FirebaseMessagingService {
    private String TAG = "Bookworm Messaging Service";

    /**
     * The method which receives and handles the RemoteMesage object containing the notification.
     * @param remoteMessage the RemoteMessage object which contains all notification information.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            /*
             * Build a notification from a RemoteMessage object
             * https://wajahatkarim.com/2018/05/firebase-notifications-in-background--foreground-in-android/
             * Wajahat Karim
             * Accessed November 16th, 2020
             */
            Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID_1)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.drawable.ic_baseline_message_1)
                .build();
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(0, notification);
        }
    }
}
