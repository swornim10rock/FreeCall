package com.example.swornim.freecall;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Swornim on 2/4/2017.
 */
public class MyphoneStateListener extends PhoneStateListener{

    private Context context;
    private String startTime;
    private String endTimel;

    public MyphoneStateListener(Context context) {
        this.context=context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state){

            case TelephonyManager.CALL_STATE_RINGING:
                int stopLooperCalls=new CustomSharedPref(context).getSharedPrefInt("stopLooperCalls");
                if(stopLooperCalls==0){
                    new CustomSharedPref(context).setSharedPrefInt("stopLooperCalls",++stopLooperCalls);

                    Intent newIntent=new Intent(context,MisscallInterface.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);//this is the same intent type

                    PendingIntent pendingIntent=PendingIntent.getActivity(context,MinorDetails.uniqueAlarmPendingId,newIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager=(AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);

                    new ProcessMissCall(context,incomingNumber).startProcess();

                }
                break;

            case TelephonyManager.CALL_STATE_IDLE://when the connection is ended
               // new ProcessMissCall(context).startProcess();

                new CustomSharedPref(context).setSharedPrefInt("stopLooperCalls",0);
                break;


            case TelephonyManager.CALL_STATE_OFFHOOK:

                break;


        }

    }


}
