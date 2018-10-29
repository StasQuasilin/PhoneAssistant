package ua.quasilin.assistant.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ua.quasilin.assistant.services.MainService;
import ua.quasilin.assistant.utils.ServiceStarter;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
            ServiceStarter.Start(context, new Intent(context, MainService.class));
        }
    }
}
