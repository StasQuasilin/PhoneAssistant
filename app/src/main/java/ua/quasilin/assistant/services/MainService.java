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

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class MainService extends Service {

    final IBinder binder = new ServiceBinder();
    ApplicationParameters parameters;
    CustomAuthenticator authenticator;

    Random random = new Random();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        parameters = ApplicationParameters.getInstance(getApplicationContext());

        Permissions.insert(getApplicationContext());
        this.registerReceiver(new CallReceiver(parameters), new IntentFilter("android.intent.action.PHONE_STATE"));
        Toast.makeText(this, "Служба \'Phone Assistant\' создана",
                Toast.LENGTH_SHORT).show();
        Notificator.build(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
}
