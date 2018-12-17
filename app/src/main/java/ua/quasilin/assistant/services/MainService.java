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


import java.util.Timer;
import java.util.TimerTask;

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

    final IBinder binder = new ServiceBinder();
    ApplicationParameters parameters;
    CallReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            super.startForeground(1, Notificator.build(getBaseContext(), 1));
        } else {
            Toast.makeText(getApplicationContext(),
                    "Служба \'Phone Assistant\' работает в фоновом режиме", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        parameters = ApplicationParameters.getInstance(getApplicationContext());
        Permissions.insert(getApplicationContext());

        receiver = new CallReceiver(parameters);
        registerReceiver(receiver, new IntentFilter("android.intent.action.PHONE_STATE"));

        return START_STICKY;
    }

    public class ServiceBinder extends Binder{
        public MainService getService() {
            return MainService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Служба \'Phone Assistant\' остановлена", Toast.LENGTH_LONG);
        }
    }
}
