package ua.quasilin.assistant.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import ua.quasilin.assistant.MainActivity;
import ua.quasilin.assistant.services.MainService;
import ua.quasilin.assistant.services.NotificationListener;
import ua.quasilin.assistant.services.OperationJobService;
import ua.quasilin.assistant.utils.Notificator;
import ua.quasilin.assistant.utils.ServiceStarter;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, new Intent(context, MainService.class));
            } else {
                ServiceStarter.Start(context, new Intent(context, MainService.class));
            }
//            ServiceStarter.Start(context, new Intent(context, NotificationListener.class));
        }
    }
}
