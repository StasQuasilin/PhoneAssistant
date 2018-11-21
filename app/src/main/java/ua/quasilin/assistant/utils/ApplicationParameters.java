package ua.quasilin.assistant.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.net.FileNameMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ua.quasilin.assistant.R;
import ua.quasilin.assistant.services.MainService;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class ApplicationParameters {
    @SuppressLint("StaticFieldLeak")
    private static ApplicationParameters instance;
    private boolean enable = true;
    private String login = "administrator";
    private String password = "111111";
    private Context context;
    private HashMap<String, String> history = new HashMap<>();
    public int instances = 0;

    private ApplicationParameters(Context context) {
        this.context = context;
        Read();
    }

    public static ApplicationParameters getInstance(Context context) {
        if (instance == null){
            instance = new ApplicationParameters(context);
        }
        return instance;
    }

    private void Read(){
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.preferences), Context.MODE_PRIVATE);
        if (preferences != null) {
            enable = preferences.getBoolean(String.valueOf(R.string.enable_key), enable);
            login = preferences.getString(String.valueOf(R.string.login_key), login);
            password = preferences.getString(String.valueOf(R.string.password_key), password);
        }

        ReadHistory();
    }

    void ReadHistory() {
        SharedPreferences historyPreferences = context.getSharedPreferences(String.valueOf(R.string.history), Context.MODE_PRIVATE);
        if (historyPreferences != null){
            for (Map.Entry<String, ?> entry : historyPreferences.getAll().entrySet()){
                history.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    private void Save() {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(R.string.enable_key), enable);
        editor.putString(String.valueOf(R.string.login_key), login);
        editor.putString(String.valueOf(R.string.password_key), password);
        editor.apply();
    }

    private void SaveHistory() {
        SharedPreferences historyPreferences = context.getSharedPreferences(String.valueOf(R.string.history), Context.MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyPreferences.edit();

        for (Map.Entry<String, String> entry : history.entrySet()){
            historyEditor.putString(entry.getKey(), entry.getValue());
        }

        historyEditor.apply();

    }

    public boolean isEnable() {
        return enable;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        Save();
    }

    public void setLogin(String login) {
        this.login = login;
        Save();
    }

    public void setPassword(String password) {
        this.password = password;
        Save();
    }

    String getUrl() {
        return "https://web-1c.42clouds.com/222cbd881bc28a5e41416b28/1c_my_770_31/hs/PhoneAssistant/Contacts";
    }

    public void put(String key, String value) {
        history.put(key, value);
        SaveHistory();
    }

    public HashMap<String, String> getHistory() {
        return history;
    }
}
