package ua.quasilin.assistant.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Random;

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
    CustomAuthenticator authenticator;
    CallReceiver receiver;
    public static boolean wasRunning = false;


    Random random = new Random();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        parameters = ApplicationParameters.getInstance(getApplicationContext());
        Permissions.insert(getApplicationContext());

        receiver = new CallReceiver(parameters);
        this.registerReceiver(receiver, new IntentFilter("android.intent.action.PHONE_STATE"));

        Toast.makeText(this, "Служба \'Phone Assistant\' создана",
                Toast.LENGTH_SHORT).show();
        Notificator.build(getBaseContext(), 1);

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
        unregisterReceiver(receiver);
        Toast.makeText(this, "Служба \'Phone Assistant\' астанавилась",
                Toast.LENGTH_SHORT).show();
    }
}
