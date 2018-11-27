package ua.quasilin.assistant;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ua.quasilin.assistant.services.MainService;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.CustomAuthenticator;
import ua.quasilin.assistant.utils.Notificator;
import ua.quasilin.assistant.utils.Permissions;
import ua.quasilin.assistant.utils.RunChecker;
import ua.quasilin.assistant.utils.ServiceStarter;

public class MainActivity extends AppCompatActivity {

    boolean bound = false;
    MainService mainService;
    ServiceConnection serviceConnection;
    Intent serviceIntent;
    ApplicationParameters parameters;
    CustomAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(getApplicationContext(), MainService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MainService.ServiceBinder binder = (MainService.ServiceBinder) service;
                mainService = binder.getService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        ServiceStarter.Start(getApplicationContext(), serviceIntent);
//        bindToService();
        parameters = ApplicationParameters.getInstance(getApplicationContext());
        authenticator = new CustomAuthenticator(parameters);
        initValues();

    }

    private void initValues() {
        Switch mainSwitch = findViewById(R.id.mainSwitcher);
        mainSwitch.setChecked(parameters.isEnable());
        mainSwitch.setOnClickListener(v -> {
            parameters.setEnable(mainSwitch.isChecked());
        });

        EditText login = findViewById(R.id.loginEdit);
        login.setText(parameters.getLogin());
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                parameters.setLogin(login.getText().toString());
            }
        });

        EditText password = findViewById(R.id.editPassword);
        password.setText(parameters.getPassword());
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                parameters.setPassword(password.getText().toString());
            }
        });

        EditText check = findViewById(R.id.editCheck);
        Button checkButton = findViewById(R.id.checkButton);
        TextView textArea = findViewById(R.id.textArea);

        checkButton.setOnClickListener(view -> {
            String checkText = check.getText().toString();
            if (!checkText.isEmpty()){
                @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        String data = bundle.getString("data");
                        String contact = data;
                        try {
                            JSONObject json = new JSONObject(data);
                            contact = json.getString("Contact");
                            textArea.setText(checkText + ": " + contact);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        textArea.setText(contact);

                    }
                };
                Runnable runnable = () -> {
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", authenticator.Request(checkText));
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
        StringBuilder builder = new StringBuilder();

        textArea.setText(builder.toString());

        Button testButton = findViewById(R.id.test);
        testButton.setOnClickListener(v -> {
            Notificator.show(getApplicationContext(), "Test by test", 1);

        });

    }



    void bindToService() {
        bindService(serviceIntent, serviceConnection, BIND_ABOVE_CLIENT);
    }



    void unbindFromService() {
        if (bound) {
            unbindService(serviceConnection);
            bound=false;
        }
    }
}
