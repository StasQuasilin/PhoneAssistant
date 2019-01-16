package ua.quasilin.assistant.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class Permissions {
    public static void insert(Context context) {
        if(Build.VERSION.SDK_INT > 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, Service.class);
            context.startService(intent);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
