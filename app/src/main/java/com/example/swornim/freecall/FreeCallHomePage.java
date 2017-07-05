package com.example.swornim.freecall;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeCallHomePage extends AppCompatActivity  {

    private ListView listView;
    private ArrayAdapter<MinorDetails> adapter;
    private List<MinorDetails> postdata=new ArrayList<>();//for unique number filtered for database
    private List<MinorDetails> postdata1=new ArrayList<>();//for contacts actual uri format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_call_home_page);

        listView = (ListView) findViewById(R.id.phoneContactsList);
        //load the static contacts to the postdata
        getstaticContacts();

        adapter = new PhoneCallTemplateAdapter(getApplicationContext(), postdata);
        listView.setAdapter(adapter);
        new CustomSharedPref(getApplicationContext()).getSharedPrefInt("NMC");
        String contactAlreadyUpdate = new CustomSharedPref(getApplicationContext()).getSharedPref("alreadyContacts");
        new UpdateContacts().execute();

        String status = new CustomSharedPref(getApplicationContext()).getSharedPref("updatefriendsfirebase");
        if (status.equals("none")) {
            //update the friends on firebase
        }

    }

    public class UpdateContacts extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            //TODO UPDATE AND ALERT THE ADAPTER

//            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//
//            if (cursor != null) {
//                if (cursor.getCount() > 0) {
//
//                    while (cursor.moveToNext()) {
//
//                        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                        MinorDetails minorDetails1 = new MinorDetails();
//                        minorDetails1.setContactNumber(phoneNumber);
//                        postdata1.add(minorDetails1);
//
//                        String filteredNumber = phoneNumber.replaceAll("[-=+()/!~|*&^#@$:'{}_abcdefghijklmnopqrstuvwxyz;%`.,]", "");
//                        if(filteredNumber.length()>10)
//                        filteredNumber=filteredNumber.substring(filteredNumber.length() - 10);
////
//
//                        MinorDetails minorDetails = new MinorDetails();
//
//                        minorDetails.setContactName(username);
//
//                            //avoid other except the 10 digit number
//                            minorDetails.setContactNumber(filteredNumber);
//                        postdata.add(minorDetails);
//                    }
//                    MinorDetails minorDetails = new MinorDetails();
//                    minorDetails.setContactName("home");
//                    minorDetails.setContactNumber("016632570");
//                    postdata.add(minorDetails);
//                    postdata1.add(minorDetails);
//
//                }
//                cursor.close();


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            new CustomSharedPref(getApplicationContext()).setSharedPref("alreadyContacts","true");
            adapter.notifyDataSetChanged();

        }
    }

    public class PhoneCallTemplateAdapter extends ArrayAdapter<MinorDetails> {

        private List<MinorDetails> postdata=new ArrayList<>();

        public PhoneCallTemplateAdapter( Context context,List<MinorDetails> postdata) {
            super(context, R.layout.phonecalltemplate,postdata);
            this.postdata=postdata;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View mView=convertView;
            if(convertView==null){

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                mView = layoutInflater.inflate(R.layout.phonecalltemplate, parent, false);
            }


            TextView name=(TextView) mView.findViewById(R.id.phonecalltemplatename);
            TextView number=(TextView) mView.findViewById(R.id.phonecalltemplatenumber);
            final ImageView messageimage=(ImageView) mView.findViewById(R.id.phonecalltemplatemessimage);
            ImageView phonecallimage=(ImageView) mView.findViewById(R.id.phonecalltemplatecallimage);
            ImageView offlineMessage=(ImageView) mView.findViewById(R.id.phonecalltemplateofflineMessage);

            name.setText(postdata.get(position).getContactName());
            number.setText(postdata.get(position).getContactNumber());

            messageimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send all the information related to that node to the chat interface logic

                    MinorDetails minorDetails=new MinorDetails();
                    minorDetails.setContactName(postdata.get(position).getContactName());
                    minorDetails.setContactNumber(postdata.get(position).getContactNumber());
                    minorDetails.setPostdata(postdata);
                    Log.i("mytag",minorDetails.getContactNumber());

                    Intent intent=new Intent(FreeCallHomePage.this,RenotifierChatHome.class);
                    intent.putExtra("minorDetails",minorDetails);
                    startActivity(intent);
                }
            });

            phonecallimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tel = "tel:" + postdata.get(position).getContactNumber();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                    startActivity(callIntent);

                }
            });

            offlineMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MinorDetails minorDetails=new MinorDetails();
                    minorDetails.setContactName(postdata.get(position).getContactName());
                    minorDetails.setContactNumber(postdata.get(position).getContactNumber());
                    minorDetails.setPostdata(postdata);

                    Intent intent=new Intent(FreeCallHomePage.this,OfflineMessage.class);
                    intent.putExtra("offlineMessageObj",minorDetails);
                    startActivity(intent);
                }
            });



            return mView;
        }
    }


    @Override
    public void onBackPressed(){

      finishAffinity();

    }

    public void getstaticContacts(){
        MinorDetails minorDetails=new MinorDetails();
        minorDetails.setContactName("Swornim Bikram Shah");
        minorDetails.setContactNumber("9813847444");
        postdata.add(minorDetails);

        MinorDetails minorDetails1=new MinorDetails();

        minorDetails1.setContactName("Bikesh Man Chipalu");
        minorDetails1.setContactNumber("9813054341");
        postdata.add(minorDetails1);

        MinorDetails minorDetails2=new MinorDetails();

        minorDetails2.setContactName("Sushant Malla");
        minorDetails2.setContactNumber("9860569432");
        postdata.add(minorDetails2);

        MinorDetails minorDetails3=new MinorDetails();
        minorDetails3.setContactName("Girish Chanda");
        minorDetails3.setContactNumber("9841001504");
        postdata.add(minorDetails3);

        MinorDetails minorDetails4=new MinorDetails();
        minorDetails4.setContactName("Girish Chanda");
        minorDetails4.setContactNumber("9841001504");
        postdata.add(minorDetails4);

        MinorDetails minorDetails5=new MinorDetails();
        minorDetails5.setContactName("Manish Shrestha ");
        minorDetails5.setContactNumber("9841441504");
        postdata.add(minorDetails5);

        MinorDetails minorDetails6=new MinorDetails();
        minorDetails6.setContactName("Animesh Thapa");
        minorDetails6.setContactNumber("9841333504");
        postdata.add(minorDetails6);

    }



}
