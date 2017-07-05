package com.example.swornim.musicnap.customAdapterPackage;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.swornim.musicnap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SnapMusicHomePage extends AppCompatActivity {

    private ImageView sendImageView;
    private ImageView friends;
    private EditText actualMessageView;
    private ListView customessageListView;
    private ArrayAdapter<com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation> adapter;
    private Map<String,Object> userInstantMessage=new HashMap<String, Object>();
    private List<com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation> sourceBucket=new ArrayList<>();
    private DatabaseReference mdatabaseReference= FirebaseDatabase.getInstance().getReference("users/groupChat/");
    private int random;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custommessagelist);
        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(getApplication().INPUT_METHOD_SERVICE);
        new CustomSharedPreferance(getApplicationContext()).getSharedPref("message", "none");
        String userName=new CustomSharedPreferance(getApplicationContext()).getSharedPref("userName", "ananomous");

        if(userName=="ananomous" || userName==null)
            showAlertDialog("UserName");

        actualMessageView = (EditText) findViewById(R.id.actualMessageView);
        actualMessageView.setInputType(InputType.TYPE_NULL);
        customessageListView = (ListView) findViewById(R.id.customMessageListView);
        sendImageView = (ImageView) findViewById(R.id.sendImageView);
        friends = (ImageView) findViewById(R.id.friends);

        adapter = new customAdapterForChatInterface(getApplicationContext(), sourceBucket);
        customessageListView.setAdapter(adapter);


        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), friendlist.class));
            }
        });


        actualMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualMessageView.setInputType(InputType.TYPE_CLASS_TEXT);
                actualMessageView.requestFocus();
                inputMethodManager.showSoftInput(actualMessageView,InputMethodManager.RESULT_SHOWN);
            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherStuffs();//kind of makes the ui faster
            }

        });


    }


    public void otherStuffs(){
        String actualMessageTobeSent=actualMessageView.getText().toString();

        if(!actualMessageTobeSent.isEmpty()) { //dont send blank message
            random=randomNumber();
            com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation mapObject=new com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation();
            mapObject.setCurrentMessageTobeSent(actualMessageTobeSent);
            mapObject.setCurrentAppUserName(new CustomSharedPreferance(getApplicationContext()).getSharedPref("userName","ananomous"));
            new CustomSharedPreferance(getApplicationContext()).setSharedPref("message", "Message" + random);

            userInstantMessage.put("message"+random,mapObject);

            databaseListener(random);
            new com.example.swornim.musicnap.customAdapterPackage.FirebaseUserModel().new InstantMessaging(getApplicationContext(), userInstantMessage).execute();
        }
        actualMessageView.setText(null);
    }



    public  void databaseListener(int random){
        //when the user comes in the snap home page send presence status to everyone

        mdatabaseReference.child("message"+random).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               Map<String,Object> map=( Map<String, Object> ) dataSnapshot.getValue();


                String currentAppUserName=map.get("currentAppUserName").toString();
                String currentMessageToBeSent=map.get("currentMessageTobeSent").toString();

                com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation usersMessages=new com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation();
                usersMessages.setCurrentMessageTobeSent(currentMessageToBeSent);
                usersMessages.setCurrentAppUserName(currentAppUserName);
                sourceBucket.add(usersMessages);
                adapter.notifyDataSetChanged();
                customessageListView.setSelection(adapter.getCount()-1);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public int randomNumber(){ return new Random().nextInt(1000)+2; }


    @Override
    public void onBackPressed() {

        showAlertDialog("Do you want to Stop Snap Music Now?");

    }

    public void showAlertDialog(final String title){

        AlertDialog.Builder popBox = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        final View view=layoutInflater.inflate(R.layout.alertedit,null);
        popBox.setTitle(title);
        popBox.setCancelable(false);

        if(title=="UserName"){

            popBox.setView(view);
            popBox.setTitle("Enter the userName");

            popBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText=(EditText) view.findViewById(R.id.alertEditText);
                    if(editText.getText().toString().equals(""))
                        showAlertDialog(title);// call again if nothing is entered
                    else{
                        new CustomSharedPreferance(getApplicationContext()).setSharedPref("userName", editText.getText().toString());
                        dialog.dismiss();
                    }

                }
            });

        }else {

            popBox.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            popBox.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), friendlist.class));

                }
            });

        }

        popBox.show();

    }


    public void notificationBar(String message,DataSnapshot dataSnapshot){

        Intent intent=new Intent(getApplicationContext(),test.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager mNotificationManager=(NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Notification mNotification=new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("New Moments")
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .build();

        mNotification.flags=Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(100,mNotification);

        new CustomSharedPreferance(getApplicationContext()).setSharedPref("message","none");

    }


}
