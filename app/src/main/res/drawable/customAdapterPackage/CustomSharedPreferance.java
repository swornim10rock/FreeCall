package com.example.swornim.musicnap.customAdapterPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Swornim on 1/31/2017.
 */
public class CustomSharedPreferance {

    private Context context;

    //GET method creates a global variable if not exists
    private static SharedPreferences SHARED_PREFERENCE_NAME;
    private static String STRING_KEY_1;
    private static String STRING_KEY_2;
    private static boolean BOOLEAN_KEY_1;
    private static boolean BOOLEAN_KEY_2;

    public CustomSharedPreferance(Context context) {
        this.context = context;
        SHARED_PREFERENCE_NAME=PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public String getSharedPref(String key,String value) {

        key= SHARED_PREFERENCE_NAME.getString(key,value);
        return key;
    }

    public void setSharedPref(String key,String value){

        SHARED_PREFERENCE_NAME.getString(key,value);
        SharedPreferences.Editor editor=SHARED_PREFERENCE_NAME.edit();
        editor.putString(key,value);
        editor.apply();

        }

    }


