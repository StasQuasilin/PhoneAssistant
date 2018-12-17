package ua.quasilin.assistant.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ua.quasilin.assistant.receivers.CallReceiver;

/**
 * Created by szpt_user045 on 28.11.2018.
 */

public class RequestWorker {

    CustomAuthenticator authenticator;

    public RequestWorker(CustomAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void DoRequest(final Context context, final String number, ShowType show){
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

                switch (show) {
                    case toast:
                        ShowToast(context, contact);
                        break;

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

    private static Toast toast;
    private static CountDownTimer toastCountDown;

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
}
