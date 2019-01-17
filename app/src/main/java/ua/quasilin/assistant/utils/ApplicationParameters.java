package ua.quasilin.assistant.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    private boolean enable;
    private Context context;
    private SharedPreferences defaultSharedPreferences;

    private ApplicationParameters(Context context) {
        this.context = context;
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        }
    }

    private void Save() {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(R.string.enable_key), enable);
        editor.apply();
    }

    public boolean isEnable() {
        return enable;
    }

    public String getLogin() {
        return defaultSharedPreferences.getString("login", "");
    }

    public String getPassword() {
        return defaultSharedPreferences.getString("password", "");
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        Save();
    }

    public String getUrl() {
        return defaultSharedPreferences.getString("url", "");
    }

    public Context getContext() {
        return context;
    }

    public boolean getBoolean(String name, boolean def) {
        return defaultSharedPreferences.getBoolean(name, def);
    }

    public void setBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }
}
