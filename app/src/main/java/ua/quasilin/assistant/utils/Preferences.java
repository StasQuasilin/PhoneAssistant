package ua.quasilin.assistant.utils;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import ua.quasilin.assistant.R;

/**
 * Created by szpt_user045 on 18.12.2018.
 */

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
