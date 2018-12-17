package ua.quasilin.assistant.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import ua.quasilin.assistant.MainActivity;
import ua.quasilin.assistant.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class Notificator {

    private static final String channelId = "ua.quasilin.assistant";

    public static Notification build(Context context, int id) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Main Channel";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("This is DESCRIPTION!!!");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setOngoing(true)
                        .setContentText(context.getResources().getString(R.string.notify_in_background))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(resultPendingIntent)
                ;



        Notification notification = builder.build();

//        notificationManager.notify(id, notification);
        return notification;
    }

    public static void show(Context context, String contact, int id) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.ic_menu_call)
                        .setVibrate(new long[0])
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setTimeoutAfter(60000)
                        .setOnlyAlertOnce(true)
                        .setContentTitle(contact);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_CALL);
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;

        NotificationManager notificationManager =(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
}

