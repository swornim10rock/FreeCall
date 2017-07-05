package com.example.swornim.musicnap.customAdapterPackage;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Swornim on 1/26/2017.
 */
public class FirebaseUserModel {

    DatabaseReference mdatabaseReference= FirebaseDatabase.getInstance().getReference("users");
    //songs details variables
    private static String S0NGNAME;

    //instant status variables
    private static String ONLINE_OFFLINE="ONLINE_OFFLINE";
    private static String LISTENING="LISTENING";
    private static String EAR_PLUGGED_OR_NOT="EAR_PLUGGED_OR_NOT";
    private static String WHO_IS_ONLINE="WHO_IS_ONLINE";


    //general variables
    private Context context;
    private static String talkWithHim;
    private static String mMessage;//actual message



    public static String getWhoIsOnline() {
        return WHO_IS_ONLINE;
    }

    public static void setWhoIsOnline(String whoIsOnline) {
        WHO_IS_ONLINE = whoIsOnline;
    }

    public static String getS0NGNAME() {
        return S0NGNAME;
    }

    public static void setS0NGNAME(String s0NGNAME) {
        S0NGNAME = s0NGNAME;
    }

    public static String getOnlineOffline() {
        return ONLINE_OFFLINE;
    }

    public static void setOnlineOffline(String onlineOffline) {
        ONLINE_OFFLINE = onlineOffline;
    }

    public static String getLISTENING() {
        return LISTENING;
    }

    public static void setLISTENING(String LISTENING) {
        com.example.swornim.musicnap.customAdapterPackage.FirebaseUserModel.LISTENING = LISTENING;
    }

    public static String getEarPluggedOrNot() {
        return EAR_PLUGGED_OR_NOT;
    }

    public static void setEarPluggedOrNot(String earPluggedOrNot) {
        EAR_PLUGGED_OR_NOT = earPluggedOrNot;
    }

    public String getTalkWithHim() {
        return talkWithHim;
    }

    public void setTalkWithHim(String talkWithHim) {
        this.talkWithHim = talkWithHim;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }




    public class InstantMessaging extends AsyncTask<Void,Void,Void>{

        private Context context;
        private Map<String,Object> map=new HashMap<>();


        public InstantMessaging(Context context,Map<String,Object> map) {

            this.map=map;
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void...params){

                mdatabaseReference.
                        child("groupChat")
                        .updateChildren(map);


           return null;
        }

    }


}






