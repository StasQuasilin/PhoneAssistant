package ua.quasilin.assistant.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import ua.quasilin.assistant.utils.NoSSL.NoSSLv3SocketFactory;
import ua.quasilin.assistant.utils.connection.IConnector;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class CustomAuthenticator extends AsyncTask<String, Void, String> implements IConnector {

    private ApplicationParameters parameters;
    private static final String REQUEST_METHOD = "POST";
    private static final String CHARSET_ENCODING = "UTF-8";

    public CustomAuthenticator(ApplicationParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String Request(String body){
        return doInBackground(body);
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        OutputStream out;

        try {
            Authenticator.setDefault(new BasicAuthenticator(parameters.getLogin(), parameters.getPassword()));
            URL u = new URL(parameters.getUrl());

            HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3SocketFactory());
            HttpURLConnection urlConnection = (HttpsURLConnection) u.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            for(String s : strings) {
                if (s != null) {
                    writer.write(s);
                }
            }

            writer.close();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.i("Response Code", String.valueOf(responseCode));


            if (responseCode != HttpURLConnection.HTTP_OK){
                result.append("Response code: ").append(responseCode);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Wrong login or password";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return result.toString();
    }
}
