package com.example.swornim.freecall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by Swornim on 5/3/2017.
 */

public class ProcessMissCall {
    private List<MinorDetails> todaysContainer=new ArrayList<>();//filter2
    private List<MinorDetails> processContainer=new ArrayList<>();//contains the fresh items to be processed
    private Context context;
    private String phoneNumber;
    private int countMessageID=1;

    public ProcessMissCall(Context context,String phoneNumber) {
        this.context = context;
        this.phoneNumber=phoneNumber;
    }

    public void startProcess(){
        String[] projection=new String[]{

                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE

        };

            Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,projection,null, null, CallLog.Calls.DATE + " DESC");

            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    String callerID = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                    String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                    String DATE = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
                    String callType = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));

                    long datelong = Long.parseLong(DATE);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                String hhmmss= String.format("%02d:%02d:%02d",
//                        TimeUnit.MILLISECONDS.toHours(datelong),
//                        TimeUnit.MILLISECONDS.toMinutes(datelong)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(datelong)),
//                TimeUnit.MILLISECONDS.toSeconds(datelong)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(datelong)));
//
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:SS");
                    String hhmmss = simpleDateFormat1.format(datelong);

                    Calendar calender = Calendar.getInstance();
                    String todaysDate = simpleDateFormat.format(calender.getTimeInMillis());

                    if (todaysDate.equals(simpleDateFormat.format(datelong)) && Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE && callerNumber.equals(phoneNumber)) {

                        MinorDetails minorDetails = new MinorDetails();
                        minorDetails.setUserContactID(callerID);
                        minorDetails.setHhmmss(hhmmss);
                        minorDetails.setUserContactNumber(callerNumber);
                        todaysContainer.add(minorDetails);//filter2
                        Log.i("mytag ", callerID + " " + callerNumber + " " + simpleDateFormat.format(datelong) + " " + hhmmss);
                    }

                } while (c.moveToNext());
            }
            c.close();
            //send to validate the contacts and show the required  schdeular interface
            callThreeProcess();
        }

    public void callThreeProcess(){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:SS");
        String todays_hhmmss = simpleDateFormat.format(System.currentTimeMillis());
        String Stodays_mm=String.valueOf(todays_hhmmss.charAt(3)).concat(String.valueOf(todays_hhmmss.charAt(4)));
        int Itodays_mm=Integer.parseInt(Stodays_mm);
       // Log.i("mytag","counter todays mm " + Itodays_mm);

        //filter the call logs on the basis of hhmmss and count the number of calls
        for(int i=0;i<todaysContainer.size();i++){

            String callogs_hhmmss=todaysContainer.get(i).getHhmmss();
            String Scallogs_mm=String.valueOf(callogs_hhmmss.charAt(3))+String.valueOf(callogs_hhmmss.charAt(4));
            int Icallogs_mm=Integer.parseInt(Scallogs_mm);
         //   Log.i("mytag","counter calllogs mm " +Icallogs_mm);

            if(todays_hhmmss.charAt(0)==callogs_hhmmss.charAt(0) &&  //it checks the hour
                    todays_hhmmss.charAt(1)==callogs_hhmmss.charAt(1)){
//                Log.i("mytag","counter " +i);

                    if(abs(Icallogs_mm-Itodays_mm)==0 || abs(Icallogs_mm-Itodays_mm)==1  ){
                        countMessageID=  new CustomSharedPref(context).getSharedPrefInt("countMessageID");
                        ++countMessageID;
                    }

            }

        }
        Log.i("mytag","Total Consecutive Misscalls "+this.countMessageID);

        new CustomSharedPref(context).setSharedPrefInt("countMessageID",this.countMessageID);
       // Log.i("mytag","counter message value "+new CustomSharedPref(context).getSharedPrefInt("countMessageID"));

        //scheduler block
        Intent newIntent=new Intent(context,MisscallInterface.class);
        MinorDetails minorDetails=new MinorDetails();
        minorDetails.setContactNumber(phoneNumber);
        newIntent.putExtra("messageObject",minorDetails);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,MinorDetails.uniqueAlarmPendingId,newIntent,PendingIntent.FLAG_UPDATE_CURRENT);//pending updates available
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+20000,pendingIntent);

    }

}
