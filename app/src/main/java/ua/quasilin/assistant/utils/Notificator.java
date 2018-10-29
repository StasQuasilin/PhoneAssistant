package ua.quasilin.assistant.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import ua.quasilin.assistant.MainActivity;
import ua.quasilin.assistant.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class Notificator {
    public static void build(Context context) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setOngoing(true)
                        .setContentTitle(context.getResources().getString(R.string.notify_title))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(resultPendingIntent)
//                        .setColor(Color.GREEN));
                        .setContentText(context.getResources().getString(R.string.notify_in_background));


        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
