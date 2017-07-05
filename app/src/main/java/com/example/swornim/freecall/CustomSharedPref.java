package com.example.swornim.freecall;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Swornim on 2/8/2017.
 */
public class CustomSharedPref  {

    private Context context;
    private static SharedPreferences SHARED_PREFERENCE_NAME;


    public CustomSharedPref(Context context) {
        this.context = context;
        SHARED_PREFERENCE_NAME= PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public String getSharedPref(String name) {

        name= SHARED_PREFERENCE_NAME.getString(name,"none");
        return name;
    }

    public void setSharedPref(String key,String value){ //may be start or end time

        SHARED_PREFERENCE_NAME.getString(key,"none");
        SharedPreferences.Editor editor=SHARED_PREFERENCE_NAME.edit();
        editor.putString(key,value);
        editor.apply();

    }


    public int getSharedPrefInt(String key){
        int value=SHARED_PREFERENCE_NAME.getInt(key,0);
        return value;
    }

    public void setSharedPrefInt(String key,int value){
        SHARED_PREFERENCE_NAME.getInt(key,0);
        SharedPreferences.Editor editor=SHARED_PREFERENCE_NAME.edit();
        editor.putInt(key,value);
        editor.apply();
    }






}


