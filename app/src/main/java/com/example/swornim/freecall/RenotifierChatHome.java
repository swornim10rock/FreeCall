package com.example.swornim.freecall;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Query;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RenotifierChatHome extends AppCompatActivity {

    private ImageView sendImageView;
    private ImageView addImage;
    private TextView statusReport;
    private EditText actualMessageView;
    private ListView customessageListView;
    private ArrayAdapter<UserDatabaseInformation> adapter;
    private MinorDetails minorDetails;
    private List<UserDatabaseInformation> userUniqueIdContainer=new ArrayList<>();
    private List<UserDatabaseInformation> sourceBucket=new ArrayList<>();
    private DatabaseReference mdatabaseReference;
    private DatabaseReference mdatabaseReferenceForSeen;
    private DatabaseReference mdatabaseReferenceForImage;
    private InputMethodManager inputMethodManager;
    private String userSeen="nope";//
    private String newMessageCame="nope";
    private String userPhoneNumber ;
    private String lastUserMessaegKey;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String[] staticFriends={"9813847444","9813054341","9841001504","9860569432","981339287"};


    //get contacht number is of friends



    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custommessagelist);

        Intent intent = getIntent();
        if ((intent.getSerializableExtra("minorDetails") != null)) {
            this.minorDetails = (MinorDetails) intent.getSerializableExtra("minorDetails");
        }

        userPhoneNumber= new CustomSharedPref(getApplicationContext()).getSharedPref("userPhoneNumber");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("users/freecall/users/9813847444/friends/"+minorDetails.getContactNumber()+"/chats/");

        mdatabaseReference.limitToLast(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                sourceBucket.clear();
//                adapter.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.i("mytag","Value "+dataSnapshot1.getValue().toString());
                    UserDatabaseInformation messageObject=dataSnapshot1.getValue(UserDatabaseInformation.class);
                    sourceBucket.add(messageObject);
                    newMessageCame="yup";
                    lastUserMessaegKey=dataSnapshot1.getKey();
                }



                Log.i("mytag","response "+dataSnapshot.toString());
                Log.i("mytag","last key  "+lastUserMessaegKey);

                adapter.notifyDataSetChanged();
                customessageListView.setSelection(adapter.getCount()-1);//scrolls ups

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(getApplication().INPUT_METHOD_SERVICE);
        String userName = new CustomSharedPref(getApplicationContext()).getSharedPref("userName");


        getSupportActionBar().setTitle(minorDetails.getContactName());

        if (userName.equals("none")) {
            showUserName();
            showPhoneNumber();
        }
        //in android the process is aynschronous for the dialog like ui thread soo the program doesnpt stops when the pop up dialog box appears


        actualMessageView = (EditText) findViewById(R.id.actualMessageView);
        actualMessageView.setInputType(InputType.TYPE_NULL);
        statusReport=(TextView)findViewById(R.id.report);

        customessageListView = (ListView) findViewById(R.id.customMessageListView);
        sendImageView = (ImageView) findViewById(R.id.sendImageView);
        addImage = (ImageView) findViewById(R.id.addImage);
//        friends = (ImageView) findViewById(R.id.friends);

        adapter = new customAdapterForChatInterface(getApplicationContext(), sourceBucket);
        customessageListView.setAdapter(adapter);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
            }
        });






        actualMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualMessageView.setInputType(InputType.TYPE_CLASS_TEXT);
                actualMessageView.requestFocus();
                inputMethodManager.showSoftInput(actualMessageView, InputMethodManager.RESULT_SHOWN);

                if(newMessageCame.equals("yup")){
                    //call firabase and sent the seen message to the friend that messaged me
                    mdatabaseReferenceForSeen = FirebaseDatabase.getInstance().getReference("users/"  + "/friends/" + minorDetails.getContactNumber() + "/chats/");

                    mdatabaseReferenceForSeen.child(lastUserMessaegKey).child("sen").setValue("Seen");
                    newMessageCame="nope";
                }

            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherStuffs();//kind of makes the ui faster
            }

        });

    }




//        String firstCall= new CustomSharedPref(getApplicationContext()).getSharedPref("firstCall");
//        if(firstCall.equals("none")) {
//            //do nothing first time but later change the value
//            new CustomSharedPref(getApplicationContext()).setSharedPref("firstCall","called");
//
//        }else{
//            addPushKeysToFirebaseRef.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    getApplicationContext().deleteDatabase("FREE_CALLS");
//                    final MSQLiteDatabase msqLiteDatabase = new MSQLiteDatabase(getApplicationContext());
//
//                    userUniqueIdContainer.clear();
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        // Log.i("mytag","asdas"+dataSnapshot1.getValue());
//                        msqLiteDatabase.insertUserContacts(dataSnapshot1.getKey(), dataSnapshot1.getValue().toString());
//                    }
//                    Map<String, Object> databasemap = msqLiteDatabase.readAllContacts();
//                    for (Map.Entry<String, Object> singleEntry : databasemap.entrySet()) {
//                        Log.i("mytag", singleEntry.getKey() + " : " + singleEntry.getValue());
//                    }
//                    new CustomSharedPref(getApplicationContext()).setSharedPref("firstCall","none");
//
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }





//                        sourceBucket.add(map);
//                        adapter.notifyDataSetChanged();
//                        customessageListView.setSelection(adapter.getCount());

            //}




    public void otherStuffs() {

        String mes = actualMessageView.getText().toString();

        if (!mes.isEmpty()) { //dont send blank message

            UserDatabaseInformation messageObject1=new UserDatabaseInformation();
            messageObject1.setMes(mes);

            sourceBucket.add(messageObject1);
            adapter.notifyDataSetChanged();

            UserDatabaseInformation messageObject=new UserDatabaseInformation();
            messageObject.setMes(mes);
            messageObject.setSen("");
//            messageObject.setReceiverPhoneNumber(minorDetails.getContactNumber());
//            messageObject.setReceiverUserName(minorDetails.getContactName());
            String MyNumber=new CustomSharedPref(getApplicationContext()).getSharedPref("userPhoneNumber");
            UserDatabaseInformation myselfInfo=new UserDatabaseInformation();
            myselfInfo.setPhoneNumber(MyNumber);
            myselfInfo.setUserName("swornim10rock");

            customessageListView.setSelection(adapter.getCount()-1);//scrolls ups

            new FirebaseUserModel().new InstantMessaging(getApplicationContext(),messageObject,myselfInfo).execute();
        }

        actualMessageView.setText("");
    }

    public int randomNumber(){ return new Random().nextInt(1000)+2; }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(RenotifierChatHome.this,FreeCallHomePage.class));

        //whenever users clicks back, implmenet your method

    }

    public void showUserName(){

        AlertDialog.Builder popBox = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        final View view=layoutInflater.inflate(R.layout.alertedit,null);
        popBox.setCancelable(false);

            popBox.setView(view);
            popBox.setTitle("Enter the userName");

            popBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText=(EditText) view.findViewById(R.id.alertEditText);
                    if(editText.getText().toString().equals(""))
                        showUserName();// call again if nothing is entered
                    else{
                        new CustomSharedPref(getApplicationContext()).setSharedPref("userName",editText.getText().toString());
                        registeringProcess();
                        dialog.dismiss();
                    }

                }
            });

        popBox.show();

    }

    public void showPhoneNumber(){

        AlertDialog.Builder popBox = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        final View view=layoutInflater.inflate(R.layout.alertedit,null);
        popBox.setCancelable(false);

            popBox.setView(view);
            popBox.setTitle("Enter the PhoneNumber");

            popBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText=(EditText) view.findViewById(R.id.alertEditText);
                    if(editText.getText().toString().equals(""))
                        showPhoneNumber();// call again if nothing is entered
                    else{
                        new CustomSharedPref(getApplicationContext()).setSharedPref("userPhoneNumber",editText.getText().toString());
                        dialog.dismiss();
                    }

                }
            });
        popBox.show();
        }


    public void registeringProcess(){

        UserDatabaseInformation messageObject=new UserDatabaseInformation();
        messageObject.setPhoneNumber(new CustomSharedPref(getApplicationContext()).getSharedPref("userPhoneNumber"));
        messageObject.setUserName(new CustomSharedPref(getApplicationContext()).getSharedPref("userName"));

        List<UserDatabaseInformation> list=new ArrayList<>();


        for(int i=0;i<staticFriends.length;i++){
            UserDatabaseInformation message=new UserDatabaseInformation();
            message.setFriensNumber(staticFriends[i]);
            list.add(message);

        }
        messageObject.setList(list);

        new FirebaseUserModel().new AddContactsToFirebase(getApplicationContext(),messageObject).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mdatabaseReference=null;
        //call this whenever the user will start the app instance but not the resume instance of app
        //save all the users genereated pushedkey to the database and first drop the previous table

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mdatabaseReference=null;

    }

    private void updateFriendsOfflineMessage(MinorDetails minorDetails){

        new CustomSharedPref(getApplicationContext()).setSharedPref(minorDetails.getUserContactNumber()+"OFFLINE",minorDetails.getMessages());

    }

    private void makeSeenInterprcess(){
        mdatabaseReferenceForSeen = FirebaseDatabase.getInstance().getReference("users/" + userPhoneNumber + "/friends/" + minorDetails.getContactNumber() + "/chats/");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();

            String[] projection = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED
            };
            Cursor cursor = managedQuery(targetUri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                String imageName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                Log.i("mytag", path);
                Log.i("mytag", imageName);
            firebaseStorage= FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReferenceFromUrl("gs://fir-cloudmessage-ac7af.appspot.com/").child(imageName);

                ImageView imageView = (ImageView) findViewById(R.id.addImage);

                Glide.with(getApplicationContext()).load(targetUri)
                        .centerCrop()
                        .crossFade()
                        .into(imageView);

                UserDatabaseInformation photoMessageforme=new UserDatabaseInformation();
                photoMessageforme.setPhotoM("yup");
                photoMessageforme.setPhotoUrl(targetUri.toString());
                sourceBucket.add(photoMessageforme);
                adapter.notifyDataSetChanged();


//            send this to the friends as message
            UploadTask uploadTask = storageReference.putFile(targetUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Errror: " + e.toString(), Toast.LENGTH_LONG).show();
                    statusReport.setText("not sent");
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //call firebase and sent the data as chat message with message included tag
                    mdatabaseReferenceForImage = FirebaseDatabase.getInstance().getReference("users/freecall/users/"+new CustomSharedPref(getApplicationContext()).getSharedPref("userPhoneNumber")+"/friends/"+minorDetails.getContactNumber()+"/chats/");

                    UserDatabaseInformation messageObject=new UserDatabaseInformation();
                    messageObject.setMes("Photo Message");
                    messageObject.setPhotoM("yup");
                    messageObject.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                    mdatabaseReferenceForImage.push().setValue(messageObject);
                    statusReport.setText("Delivered");

                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onProgress(final UploadTask.TaskSnapshot taskSnapshot) {
                    statusReport.setText("Sending...");
                }
            });


            }
        }
    }



}
