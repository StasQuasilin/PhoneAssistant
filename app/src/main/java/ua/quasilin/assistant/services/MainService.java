package ua.quasilin.assistant.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ua.quasilin.assistant.R;
import ua.quasilin.assistant.receivers.CallReceiver;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.CustomAuthenticator;
import ua.quasilin.assistant.utils.Notificator;
import ua.quasilin.assistant.utils.Permissions;
import ua.quasilin.assistant.utils.RunChecker;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class MainService extends Service {

    private static WindowManager windowManager;
    private static ViewGroup windowLayout;
    final IBinder binder = new ServiceBinder();
    ApplicationParameters parameters;
    CustomAuthenticator authenticator;
    CallReceiver receiver;
    static boolean isRun = false;

    Random random = new Random();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isRun) {
            Toast.makeText(this, "Служба \'Phone Assistant\' создана",
                    Toast.LENGTH_SHORT).show();
        }
        isRun = true;
    }

    public static void ShowBackground(Context context) {

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        assert layoutInflater != null;
        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.main_background, null);

        windowManager.addView(windowLayout, params);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Main Service", "Start");
//        ShowBackground(getApplicationContext());
        parameters = ApplicationParameters.getInstance(getApplicationContext());

        Permissions.insert(getApplicationContext());

        receiver = new CallReceiver(parameters);
        this.registerReceiver(receiver, new IntentFilter("android.intent.action.PHONE_STATE"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notificator.build(getBaseContext(), 1);
        }

        return START_STICKY;
    }

    public String getRandom() {
        return String.valueOf(random.nextInt());
    }

    public class ServiceBinder extends Binder{
        public MainService getService() {
            return MainService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun=false;
        unregisterReceiver(receiver);
        Toast.makeText(this, "Служба \'Phone Assistant\' астанавилась",
                Toast.LENGTH_SHORT).show();
    }
}
