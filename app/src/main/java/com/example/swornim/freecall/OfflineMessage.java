package com.example.swornim.freecall;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OfflineMessage extends AppCompatActivity {

   private DatabaseReference editDatabaseReference= FirebaseDatabase.getInstance().getReference("users/");
    private DatabaseReference mHiddenDatabaseReference;
    private TextView offlineUserName;
    private MinorDetails minorDetails;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<String> staticMessagesForMalla=new ArrayList<>();
    private List<String> staticMessagesForSwornim=new ArrayList<>();
    private List<String> postdata=new ArrayList<>();
    private String userPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_message);
        offlineUserName = (TextView) findViewById(R.id.offlineMessageUserName);
        mListView = (ListView) findViewById(R.id.offlineMessageListView);
        adapter=new updateOfflineMessage(getApplicationContext(),postdata);
        mListView.setAdapter(adapter);

        Intent intent = getIntent();
        this.minorDetails = (MinorDetails) intent.getSerializableExtra("offlineMessageObj");


        userPhoneNumber=new CustomSharedPref(getApplicationContext()).getSharedPref("userPhoneNumber");
        String contactNumber=minorDetails.getContactNumber();

        mHiddenDatabaseReference = FirebaseDatabase.getInstance().getReference("users/");
        mHiddenDatabaseReference.child("freecall/users/"+userPhoneNumber+"/friends/"+contactNumber).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //convert datasnapshot to json and store in shared preference
                Log.i("mytag", "datasnaphot value " + dataSnapshot.getValue());
                JsonData messageObject=dataSnapshot.getValue(JsonData.class);

                Toast.makeText(getApplicationContext(),"You have new Hidden Message",Toast.LENGTH_LONG).show();

                try {
                    saveFriendsOfflineMessages(messageObject);
                    loadOfflineMessageForUser();//for first time call it shows the interdace of nessages

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            loadOfflineMessageForUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public class updateOfflineMessage extends ArrayAdapter<String> {

        private List<String> postdata;

        public updateOfflineMessage(Context context, List<String> postdata) {
            super(context, R.layout.offlinemessagetemplate, postdata);
            this.postdata = postdata;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View mView = convertView;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                mView = layoutInflater.inflate(R.layout.offlinemessagetemplate, parent, false);
            }

            TextView offlineMessages = (TextView) mView.findViewById(R.id.offlineMessages);
            TextView offlineHowManyMisscalls = (TextView) mView.findViewById(R.id.offlineHowManyMisscalls);
            ImageView offlinemessageEditMessage = (ImageView) mView.findViewById(R.id.offlinemessageEditMessage);

            offlineMessages.setText(postdata.get(position));
            int count= position;
            ++count;
            offlineHowManyMisscalls.setText("Misscall index "+ count);

            offlinemessageEditMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDialogBox("Edit Message",position);

                }
            });

            return mView;
        }
    }


    public Bitmap retrieveContactPhoto(Context context, String number) {

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back6);

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // querying contact data store
        Cursor phones = context.getContentResolver().query(contactUri, null, null, null, null);

        while (phones.moveToNext()) {
            String image_uri = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if (image_uri != null) {

                try {
                    photo = MediaStore.Images.Media
                            .getBitmap(context.getContentResolver(),
                                    Uri.parse(image_uri));

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return photo;
    }

    //Database to hold the messages for offline rendering

//    public class MSQLiteDatabase extends SQLiteOpenHelper {
//
//        private Context CONTEXT;
//        private String OFFLINEJSON;
//        private List<String> contactList = new ArrayList<>();
//
//
//        public MSQLiteDatabase(Context context) {
//            super(context, "OFFLINE_MESSAGE"+minorDetails.getContactNumber()+".db", null, 1);
//            CONTEXT = context;
//
//        }
//
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("DROP TABLE IF EXISTS OFFLINE_MESSAGE_TABLE;");
//            db.execSQL("CREATE TABLE OFFLINE_MESSAGE_TABLE( ID INTEGER PRIMARY KEY AUTOINCREMENT,ms0 TEXT,ms1 TEXT,ms2 TEXT,ms3 TEXT,ms4 TEXT,ms5 TEXT ); ");
//        }
//        //USER MESSAGE IS IN JSON FORMAT
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS OFFLINE_MESSAGE_TABLE;");
//            onCreate(db);
//        }
//
//
//        public void insertUserContacts(List<String> map) {
//
//            //TODO This method is only called one time during updates of the template messages
//            ContentValues contentValues = new ContentValues();
//
//            contentValues.put("ms0", map.get(0));
//            contentValues.put("ms1", map.get(1));
//            contentValues.put("ms2", map.get(2));
//            contentValues.put("ms3", map.get(3));
//            contentValues.put("ms4", map.get(4));
//            contentValues.put("ms5", map.get(5));
////               Log.i("mytag","CONTENTVALUES this"+ contentValues.toString());
//            this.getWritableDatabase().execSQL("delete from OFFLINE_MESSAGE_TABLE");
//
//            this.getWritableDatabase().insertOrThrow("OFFLINE_MESSAGE_TABLE", null, contentValues);//adds new rows
//        }
//
//
//        public List<String> readAllContacts() {
//
//            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM OFFLINE_MESSAGE_TABLE", null);
//            while (cursor.moveToNext()) {
//
//                contactList.add(cursor.getString(1));
//                contactList.add(cursor.getString(2));
//                contactList.add(cursor.getString(3));
//                contactList.add(cursor.getString(4));
//                contactList.add(cursor.getString(5));
//                contactList.add(cursor.getString(6));
//            }
//            cursor.close();
//
//            Log.i("mutag","ofline messages db:"+contactList.get(0));
//            return contactList;
//        }
//
//    }





//    public void updateHiddenMessagesFromFriends(UserDatabaseInformation messageObject) {
//
//        //This is called whenever friends sends you a updated hidden messages when you are online,So update the whole hidden messages
//        ReadHiddenMessages.MShiddenMessage readHiddenMessages = new ReadHiddenMessages(getApplicationContext()).new MShiddenMessage(getApplicationContext(), messageObject);
//        getApplicationContext().deleteDatabase("OFF_HID_MES"+messageObject.getPhoneNumber());//friends number who has sent because messageobject is sent from a friend
//
//        List<String> mMessages = new ArrayList<>();
//        mMessages.add(messageObject.getMessages1());
//        mMessages.add(messageObject.getMessages2());
//        mMessages.add(messageObject.getMessages3());
//        mMessages.add(messageObject.getMessages4());
//        mMessages.add(messageObject.getMessages5());
//        mMessages.add(messageObject.getMessages6());
//        readHiddenMessages.insertUserContacts(mMessages);
//
//    }


//    public void updateOfflineDatabase(UserDatabaseInformation messageObject) {
//
//        //delete the old database to avoid the same data many times
//        getApplicationContext().deleteDatabase("OFFLINE_MESSAGE"+minorDetails.getContactNumber());
//        MSQLiteDatabase msqLiteDatabase = new MSQLiteDatabase(getApplicationContext());
//
//        List<String> mMessages = new ArrayList<>();
//        mMessages.add(messageObject.getMessages1());
//        mMessages.add(messageObject.getMessages2());
//        mMessages.add(messageObject.getMessages3());
//        mMessages.add(messageObject.getMessages4());
//        mMessages.add(messageObject.getMessages5());
//        mMessages.add(messageObject.getMessages6());
//        Log.i("mytag", "inserted data" + mMessages.toString());
//        msqLiteDatabase.insertUserContacts(mMessages);
//    }


//    public List<String> readOfflineMessages() throws JSONException {
//        //this gets called by the misscallinterface class soo it has to pass the incoming caller number
//
//        MSQLiteDatabase msqLiteDatabase = new MSQLiteDatabase(getApplicationContext());
//        List<String> offlineMap = msqLiteDatabase.readAllContacts();
//       if(postdata!=null || !(adapter.isEmpty())) {
//           postdata.clear();
//           adapter.clear();
//       }
//        if (offlineMap.isEmpty()) {
//        } else {
//            postdata.add(offlineMap.get(0));
//            postdata.add(offlineMap.get(1));
//            postdata.add(offlineMap.get(2));
//            postdata.add(offlineMap.get(3));
//            postdata.add(offlineMap.get(4));
//            postdata.add(offlineMap.get(5));
//            adapter.notifyDataSetChanged();
//        }
//        return  postdata;
//    }

    public void editDialogBox(final String title, final int whichMessageToEdit) {

        AlertDialog.Builder popBox = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View view = layoutInflater.inflate(R.layout.alertedit, null);
        popBox.setTitle(title);
        popBox.setCancelable(false);


        popBox.setView(view);
        popBox.setTitle("Enter Custom Message");

        popBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) view.findViewById(R.id.alertEditText);
                if (editText.getText().toString().equals(""))
                    editDialogBox(title,whichMessageToEdit);// call again if nothing is entered
                else {
                    int count=whichMessageToEdit;
                    ++count;
                    new FirebaseUserModel().new editOfflineMessagesForFreinds(getApplicationContext(),editText.getText().toString(),count).execute();

                    new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+count,editText.getText().toString());
                    try {
                        loadOfflineMessageForUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Message index "+count+" Sent",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            }
        });
        popBox.show();


    }

    private void saveFriendsOfflineMessages(JsonData messageObject) throws JSONException{

            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+1,messageObject.getMessage1());
            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+2,messageObject.getMessage2());
            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+3,messageObject.getMessage3());
            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+4,messageObject.getMessage4());
            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+5,messageObject.getMessage5());
            new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getContactNumber()+"message"+6,messageObject.getMessage6());

    }

    private void loadOfflineMessageForUser() throws JSONException {

        if(!postdata.isEmpty())
        postdata.clear();

        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+1));
        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+2));
        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+3));
        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+4));
        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+5));
        postdata.add(new CustomSharedPref(getApplicationContext()).getSharedPref(minorDetails.getContactNumber()+"message"+6));

        adapter.notifyDataSetChanged();

    }



}




