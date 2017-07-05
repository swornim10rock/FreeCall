package com.example.swornim.freecall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swornim on 1/27/2017.
 */
public class MSQLiteDatabase extends SQLiteOpenHelper {

    private static Context CONTEXT;
    private Map<String,Object> contactListMap=new HashMap<>();
    private List<MinorDetails> contactList=new ArrayList<>();


    public MSQLiteDatabase(Context context) {
        super(context,"FREE_CALLS.db", null, 1);
        CONTEXT=context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FREE_USER( ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,USERNUMBER TEXT); ");
    }
    //USER MESSAGE IS IN JSON FORMAT

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FREE_USER;");
        onCreate(db);
    }


    public void insertUserContacts(String USERNAME, String USERNUMBER){

        //TODO This method is only called one time during updates of the template messages
        ContentValues contentValues=new ContentValues();
        contentValues.put("USERNAME",USERNAME);
        contentValues.put("USERNUMBER",USERNUMBER);
        this.getWritableDatabase().insertOrThrow("FREE_USER",null,contentValues);//adds new rows
    }


    public Map<String,Object> readAllContacts(){

        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM FREE_USER",null);
        while(cursor.moveToNext()){

            String id=cursor.getString(1);
            String number=cursor.getString(2);
            contactListMap.put(id,number);
        }
        cursor.close();
        return contactListMap;
    }

    public List<MinorDetails> readAllContactsFreecall(){

        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM FREE_USER",null);
        while(cursor.moveToNext()){
            MinorDetails minorDetails=new MinorDetails();
            minorDetails.setUserContactName(cursor.getString(1));
            minorDetails.setUserContactNumber(cursor.getString(2));
            minorDetails.setUserMessage(cursor.getString(3));
           contactList.add(minorDetails);
        }
        return contactList;
    }


}



