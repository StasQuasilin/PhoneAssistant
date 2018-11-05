package ua.quasilin.assistant.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ua.quasilin.assistant.services.MainService;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class ServiceStarter {
    public static void Start(Context context, Intent intent){
        if (!RunChecker.isRunning(MainService.class, context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }
}
