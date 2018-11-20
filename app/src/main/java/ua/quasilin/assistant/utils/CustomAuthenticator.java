package ua.quasilin.assistant.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class CustomAuthenticator extends AsyncTask<String, Void, String>{

    private ApplicationParameters parameters;

    public CustomAuthenticator(ApplicationParameters parameters) {
        this.parameters = parameters;
    }

    public String Request(String body){
        return doInBackground(body);
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        OutputStream out;

        try {
            Log.i("!!Login", parameters.getLogin());
            Log.i("!!Password", parameters.getPassword());
            Authenticator.setDefault(new BasicAuthenticator(parameters.getLogin(), parameters.getPassword()));
            URL u = new URL(parameters.getUrl());
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
//            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(strings[0]);
            writer.flush();
            writer.close();
            out.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK){
                result.append("answer: ").append(responseCode);
            } else {
                try {
                    Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream(), "UTF-8");
                    while (httpResponseScanner.hasNextLine()) {
                        result.append(httpResponseScanner.nextLine());
                    }
                    httpResponseScanner.close();
                } catch (Exception e){
                    Log.i("Error", e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
