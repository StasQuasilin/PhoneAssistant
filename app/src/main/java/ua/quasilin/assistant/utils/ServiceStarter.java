package ua.quasilin.assistant.utils;

import android.app.ActivityManager;
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

    public static ActivityManager.RunningServiceInfo getService(Context context, Intent intent) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MainService.class.getName().equals(service.service.getClassName())) {
                return service;
            }
        }
        return null;
    }
}
