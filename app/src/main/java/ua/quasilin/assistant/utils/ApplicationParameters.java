package ua.quasilin.assistant.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.net.FileNameMap;

import ua.quasilin.assistant.R;

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
    }

    private void Save() {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(R.string.enable_key), enable);
        editor.putString(String.valueOf(R.string.login_key), login);
        editor.putString(String.valueOf(R.string.password_key), password);
        editor.apply();
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

    String getUrl() {
        return "https://web-1c.42clouds.com/222cbd881bc28a5e41416b28/1c_my_770_31/hs/PhoneAssistant/Contacts";
    }
}
