package ua.quasilin.assistant.utils.connection;

import java.io.IOException;

import ua.quasilin.assistant.utils.ApplicationParameters;

/**
 * Created by szpt_user045 on 11.01.2019.
 */

public interface IConnector {

    String Request(String number) throws IOException;
}
