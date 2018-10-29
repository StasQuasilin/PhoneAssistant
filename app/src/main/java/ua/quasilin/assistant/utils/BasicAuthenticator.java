package ua.quasilin.assistant.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Created by szpt_user045 on 29.10.2018.
 */

public class BasicAuthenticator extends Authenticator{

    private String login;
    private String password;

    BasicAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password.toCharArray());
    }
}
