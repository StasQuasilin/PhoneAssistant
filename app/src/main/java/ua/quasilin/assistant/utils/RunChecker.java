package ua.quasilin.assistant.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Quasilin on 05.11.2018.
 */

public class RunChecker {
    public static boolean isRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
