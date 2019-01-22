package ua.quasilin.assistant;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ua.quasilin.assistant.services.MainService;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.CustomAuthenticator;
import ua.quasilin.assistant.utils.HistoryArchive;
import ua.quasilin.assistant.utils.HistoryType;
import ua.quasilin.assistant.utils.Preferences;
import ua.quasilin.assistant.utils.ServiceStarter;
import ua.quasilin.assistant.utils.connection.IConnector;
import ua.quasilin.assistant.utils.connection.OkConnector;

public class MainActivity extends AppCompatActivity {

    boolean bound = false;
    MainService mainService;
    ServiceConnection serviceConnection;
    Intent serviceIntent;
    ApplicationParameters parameters;
    IConnector connector;
    HistoryArchive archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        archive = HistoryArchive.getArchive(getApplicationContext());

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
//        ServiceStarter.Start(getApplicationContext(), new Intent(getApplicationContext(), NotificationListener.class));
//        bindToService();
        parameters = ApplicationParameters.getInstance(getApplicationContext());
        connector = new OkConnector(parameters);
        initValues();

    }

    private void initValues() {

        Switch mainSwitch = findViewById(R.id.mainSwitcher);
        mainSwitch.setChecked(parameters.isEnable());
        mainSwitch.setOnClickListener(s -> {
            parameters.setEnable(mainSwitch.isChecked());
        });

        Button preferences = findViewById(R.id.preferences);
        preferences.setOnClickListener(click ->
                startActivity(new Intent(getApplicationContext(), Preferences.class)));

        EditText checkInput = findViewById(R.id.checkText);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        checkInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (v.getId() == R.id.checkText && !hasFocus){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        Button checkButton = findViewById(R.id.checkButton);

//        checkButton.setOnClickListener( (View check) -> {
//            checkInput.clearFocus();
//            String checkText= checkInput.getText().toString();
//            if (!checkText.isEmpty()){
//                progressBar.setVisibility(View.VISIBLE);
//                @SuppressLint("HandlerLeak") Handler handler = new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        String data = msg.getData().getString("data");
//                        if (data != null) {
//                            try {
//                                JSONObject json = new JSONObject(data);
//                                String contact = json.getString("contact");
//                                checkInput.setText("");
//                                archive.addToArchive(HistoryType.custom, checkText, contact);
//                            } catch (JSONException e) {
//                                Log.i("Wrong json ", "_" + data);
//                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                };
//
//                Runnable runnable = () -> {
//                    Message message = handler.obtainMessage();
//                    Bundle bundle = new Bundle();
//
//                    try {
//                        bundle.putString("data", connector.Request(checkText));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    message.setData(bundle);
//                    handler.sendMessage(message);
//
//                };
//
//                Thread thread = new Thread(runnable);
//                thread.start();
//            }
//        });

        checkButton.setOnClickListener(view -> {
            Runnable runnable = () -> {
                String check = checkInput.getText().toString();
                try {
                    String request = connector.Request(check);
                    if (request != null) {
                        try {
                            JSONObject json = new JSONObject(request);
                            int status = json.getInt("status");
                            switch (status){
                                case 200:
                                    archive.addToArchive(HistoryType.custom, check, json.getString("contact"));
                                    break;
                                case 204:
                                    archive.addToArchive(HistoryType.custom, check, "Не определен");
                                    break;
                                case 401:
                                    Toast.makeText(getApplicationContext(), "Не ужадлсь получить пройти авторизацию", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Не ужадлсь получить информацию", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();

        });
        LinearLayout list = findViewById(R.id.history_list);
        archive.updateMe(list);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        archive.updateMe(null);
    }
}
