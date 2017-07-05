package com.example.swornim.freecall;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.util.Calendar;

/**
 * Created by Swornim on 2/2/2017.
 */
public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c = Calendar.getInstance();
        String startTimeCall = Integer.toString(c.get(Calendar.SECOND));
        CustomSharedPref customSharedPref=new CustomSharedPref(context);

        customSharedPref.getSharedPref("startTime");//create if not created for startime
        customSharedPref.setSharedPref("startTime",startTimeCall);//default none

        TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        MyphoneStateListener myphoneStateListener=new MyphoneStateListener(context);
        telephonyManager.listen(myphoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }


}
