package com.example.swornim.freecall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
This is called by the misscallinterface class for getting the actual single message

 */

public class ReadHiddenMessages {

    private Context context;
    public ReadHiddenMessages(Context context) {
        this.context=context;
    }

    public UserDatabaseInformation readHiddenOfflineFromFriends(String phoneNumberStandartFormat, int numberOfMiscalls){
        //this is the method which gets called by the misscallinterface class at the end of showing messages
        //on the basis of the misscalls numbers

        UserDatabaseInformation messageObject=new UserDatabaseInformation();
        messageObject.setPhoneNumber(phoneNumberStandartFormat);

        MShiddenMessage msHiddenDatabase = new MShiddenMessage(context,messageObject);
        List<String> hiddenMap = msHiddenDatabase.readAllContacts();
        Log.i("mytag", "HiddenMessage Is " + hiddenMap.toString());

        if (numberOfMiscalls==1) {
//            messageObject.setMessages1(hiddenMap.get(numberOfMiscalls - 1));
//            messageObject.setNumberOfMiscalls(1);
//
//        }
//        else if (numberOfMiscalls==2) {
//            messageObject.setMessages2(hiddenMap.get(numberOfMiscalls-1));
//            messageObject.setNumberOfMiscalls(2);
//        }
//        else if (numberOfMiscalls==3) {
//            messageObject.setMessages3(hiddenMap.get(numberOfMiscalls - 1));
//            messageObject.setNumberOfMiscalls(3);
//
//        }
//        else if (numberOfMiscalls==4) {
//            messageObject.setMessages4(hiddenMap.get(numberOfMiscalls - 1));
//            messageObject.setNumberOfMiscalls(4);
//
//        }
//        else if (numberOfMiscalls==5) {
//            messageObject.setMessages5(hiddenMap.get(numberOfMiscalls - 1));
//            messageObject.setNumberOfMiscalls(5);
//
//        }
//        else if (numberOfMiscalls==6) {
//            messageObject.setMessages6(hiddenMap.get(numberOfMiscalls - 1));
//            messageObject.setNumberOfMiscalls(6);
        }


        return messageObject;
    }


    public class MShiddenMessage extends SQLiteOpenHelper {

        private UserDatabaseInformation messageObject;//sent from the friends
        private List<String> contactList=new ArrayList<>();


        public MShiddenMessage(Context context, UserDatabaseInformation messageObject) {
            super(context,"OFF_HID_MES"+messageObject.getPhoneNumber()+".db", null, 1);
            this.messageObject=messageObject;

        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS OFF_HID_MES_TABLE;");
            db.execSQL("CREATE TABLE OFF_HID_MES_TABLE ( ID INTEGER PRIMARY KEY AUTOINCREMENT,ms0 TEXT,ms1 TEXT,ms2 TEXT,ms3 TEXT,ms4 TEXT,ms5 TEXT ); ");
        }
        //USER MESSAGE IS IN JSON FORMAT

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS OFF_HID_MES_TABLE ;");
            onCreate(db);
        }


        public void insertUserContacts(List<String> map ){

            //TODO This method is only called one time during updates of the template messages
            ContentValues contentValues=new ContentValues();

            contentValues.put("ms0", map.get(0));
            contentValues.put("ms1", map.get(1));
            contentValues.put("ms2", map.get(2));
            contentValues.put("ms3", map.get(3));
            contentValues.put("ms4", map.get(4));
            contentValues.put("ms5", map.get(5));
            Log.i("mytag",contentValues.toString());
            this.getWritableDatabase().insertOrThrow("OFF_HID_MES_TABLE", null, contentValues);//adds new rows
        }




        public List<String> readAllContacts(){

            contactList.clear();
            Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM OFF_HID_MES_TABLE",null);
            while(cursor.moveToNext()){

                Log.i("mytag","Cursor values  "+ cursor.getString(1));
                Log.i("mytag","Cursor values  "+ cursor.getString(2));
                Log.i("mytag","Contact values "+ cursor.getString(3));
                Log.i("mytag","Contact values "+ cursor.getString(4));
                Log.i("mytag","Contact values "+ cursor.getString(5));
                Log.i("mytag","Contact values "+ cursor.getString(6));

                contactList.add(cursor.getString(1));
                contactList.add(cursor.getString(2));
                contactList.add(cursor.getString(3));
                contactList.add(cursor.getString(4));
                contactList.add(cursor.getString(5));
                contactList.add(cursor.getString(6));
            }
            cursor.close();

            return contactList;
        }

    }



}
