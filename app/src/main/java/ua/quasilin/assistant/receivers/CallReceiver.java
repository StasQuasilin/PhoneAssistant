package ua.quasilin.assistant.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Authenticator;

import ua.quasilin.assistant.R;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.CustomAuthenticator;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class CallReceiver extends BroadcastReceiver {

    private static WindowManager windowManager;
    @SuppressLint("StaticFieldLeak")
    private static ViewGroup windowLayout;
    ApplicationParameters parameters;
    CustomAuthenticator authenticator;
    private boolean incomeCall = false;

    public CallReceiver(ApplicationParameters parameters) {
        this.parameters = parameters;
        authenticator = new CustomAuthenticator(parameters);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String extra = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (extra.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if (!incomeCall) {
                    if (parameters.isEnable()) {
                        Log.i("onReceive", "Income call");
                        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        incomeCall = true;
                        DoRequest(context, number);
                    }
                }
            } else if(extra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                if (incomeCall) {
                    closeWindow();
                    incomeCall = false;
                }
            } else if(extra.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                if (incomeCall) {
                    closeWindow();
                    incomeCall = false;
                }
            }

        }
    }

    void DoRequest(final Context context, final String number){
        Log.i("DoRequest", "Do");
        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Log.i("Data", bundle.toString());
                String data = bundle.getString("data");
                String contact = data;

                try {
                    JSONObject json = new JSONObject(data);
                    contact = json.getString("Contact");
                    parameters.put(number, contact);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CallReceiver.ShowMessage(context, contact);
            }
        };
        Runnable runnable = () -> {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("data", authenticator.Request(number));
            msg.setData(bundle);
            handler.sendMessage(msg);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    static Toast mToastToShow;

    private static void ShowToast(Context context, String contact) {

            // Set the toast and duration
            int toastDurationInMilliSeconds = 1000;
            mToastToShow = Toast.makeText(context, contact, Toast.LENGTH_LONG);

            // Set the countdown to display the toast
            CountDownTimer toastCountDown;
            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 3500 /*Tick duration*/) {
                public void onTick(long millisUntilFinished) {
                    mToastToShow.show();
                }
                public void onFinish() {
                    mToastToShow.cancel();
                }
            };

            // Show the toast and starts the countdown
            mToastToShow.show();
            toastCountDown.start();
    }

    public static void ShowMessage(Context context, String phoneNumber) {
        Log.i("Call", phoneNumber);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;

        assert layoutInflater != null;
        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.info, null);

        TextView textViewNumber= windowLayout.findViewById(R.id.textViewNumber);
        Button buttonClose= windowLayout.findViewById(R.id.closeButton);
        textViewNumber.setText(phoneNumber);
        buttonClose.setOnClickListener(v -> closeWindow());

        windowManager.addView(windowLayout, params);

    }

    private static void closeWindow() {
        if (windowLayout !=null){
            windowManager.removeView(windowLayout);
            windowLayout =null;
        }
    }
}
