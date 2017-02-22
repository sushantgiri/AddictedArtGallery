package com.addictedartgallery.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.addictedartgallery.R;
import com.addictedartgallery.utils.Preferences;
import com.addictedartgallery.utils.ViewUtils;
import com.addictedartgallery.activities.Splash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class AddictedMessagingService extends FirebaseMessagingService {
    Preferences preferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> messageData = remoteMessage.getData();
        String message = "";

        if(messageData != null) {
            for (Map.Entry<String, String> entry : messageData.entrySet()) {
                message = message + entry.getValue();
            }
        }

        preferences = new Preferences(getApplicationContext());
        if(preferences.getEndpointArn() != null)
        {

            if (ViewUtils.isAppIsInBackground(getApplicationContext()))
                createNotification(message);
            else {
                Intent messageReceived = new Intent(ViewUtils.MESSAGE_RECEIVED);
                messageReceived.putExtra(ViewUtils.MESSAGE, message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageReceived);
            }
        }
    }



    private void createNotification(String messageBody) {

        Intent intent = new Intent(this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Addicted Art Gallery")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
