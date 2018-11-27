package ua.quasilin.assistant.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by szpt_user045 on 27.11.2018.
 */

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("Notification", sbn.getNotification().toString());
    }
}
