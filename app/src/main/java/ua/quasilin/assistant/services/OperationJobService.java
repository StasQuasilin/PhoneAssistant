package ua.quasilin.assistant.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import ua.quasilin.assistant.utils.ServiceStarter;

/**
 * Created by szpt_user045 on 17.12.2018.
 */

public class OperationJobService extends JobIntentService {

    private static final int JOB_ID = 0;

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, OperationJobService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        ServiceStarter.Start(getApplicationContext(), new Intent(getApplicationContext(), MainService.class));
    }
}
