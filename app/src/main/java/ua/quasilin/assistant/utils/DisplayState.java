package ua.quasilin.assistant.utils;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;

/**
 * Created by szpt_user045 on 18.12.2018.
 */

public class DisplayState {

    private DisplayManager displayManager;
    private KeyguardManager keyguardManager;
    private PowerManager powerManager;

    public DisplayState(Context context) {
//        displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public boolean isScreenLock() {
//        for (Display display : displayManager.getDisplays()){
//            if (display.getState() == Display.STATE_OFF){
//                return true;
//            }
//        }
//
//        return false;
        Log.i("Screen state", String.valueOf(powerManager.isInteractive()));
        return powerManager.isInteractive();
//        return keyguardManager.isDeviceLocked();
    }
}
