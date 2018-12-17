package ua.quasilin.assistant.receivers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresPermission;
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

import ua.quasilin.assistant.R;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.CustomAuthenticator;
import ua.quasilin.assistant.utils.Notificator;
import ua.quasilin.assistant.utils.RequestWorker;
import ua.quasilin.assistant.utils.ShowType;

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
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String extra = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (extra.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if (!incomeCall) {
                    if (parameters.isEnable()) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (incomeCall) {
                    CallReceiver.ShowToast(context, contact);
                }
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

    static Toast toast;
    static CountDownTimer toastCountDown;
    private static void ShowToast(Context context, String contact) {

            int toastDurationInMilliSeconds = 60000;
            toast = Toast.makeText(context, contact, Toast.LENGTH_SHORT);

            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 ) {
                public void onTick(long millisUntilFinished) {
                    toast.show();
                }
                public void onFinish() {
                    toast.cancel();
                }
            };

            TextView view = toast.getView().findViewById(android.R.id.message);
            view.setTextSize(16);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.LTGRAY);
            toast.show();
            toastCountDown.start();
    }

    private static void ShowNotification(Context context, String contact) {
        Notificator.show(context, contact, 2);
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
        params.gravity = Gravity.BOTTOM;

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

        if (toastCountDown != null) {
            toastCountDown.cancel();
        }
    }
}
