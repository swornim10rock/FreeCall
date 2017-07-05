package com.example.swornim.musicnap.customAdapterPackage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Swornim on 2/11/2017.
 */
public class FirebaseBackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
