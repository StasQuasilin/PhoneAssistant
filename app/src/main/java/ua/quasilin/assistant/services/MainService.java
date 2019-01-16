package ua.quasilin.assistant.services;

import android.annotation.SuppressLint;
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
import ua.quasilin.assistant.receivers.ScreenReceiver;
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
    CustomAuthenticator authenticator;

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
        authenticator =new CustomAuthenticator(parameters);

//        new Thread(() -> {
//            authenticator.Request(null);
//        }).start();


        Permissions.insert(getApplicationContext());

        receiver = new CallReceiver(parameters, getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction("android.intent.action.PHONE_STATE");

        registerReceiver(receiver, intentFilter);

        return START_STICKY;
    }

    public class ServiceBinder extends Binder{
        public MainService getService() {
            return MainService.this;
        }
    }

    @SuppressLint("ShowToast")
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
