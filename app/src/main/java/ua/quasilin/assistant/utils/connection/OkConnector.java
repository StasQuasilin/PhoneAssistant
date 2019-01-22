package ua.quasilin.assistant.utils.connection;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.Authenticator;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import ua.quasilin.assistant.utils.ApplicationParameters;
import ua.quasilin.assistant.utils.BasicAuthenticator;

/**
 * Created by szpt_user045 on 11.01.2019.
 */

public class OkConnector implements IConnector {

    private ApplicationParameters parameters;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient okHttp;

    public OkConnector(ApplicationParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String Request(String number) throws IOException {

        RequestBody body = RequestBody.create(JSON, number);
        Request request = new Request.Builder()
                .url(parameters.getUrl())
                .post(body)
                .build();
        okHttp = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(parameters.getLogin(), parameters.getPassword()))
                .build();
        try (Response response = okHttp.newCall(request).execute()){
            return response.body().string();
        }
    }

    private static OkHttpClient createAuthenticatedClient(final String login, final String password) {
        return new OkHttpClient.Builder().authenticator((route, response) -> {
            String credential = Credentials.basic(login, password);
            if (responseCount(response) >= 3) {
                return null;
            }
            return response.request().newBuilder().header("Authorization", credential).build();
        }).build();
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
