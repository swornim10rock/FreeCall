package com.example.swornim.freecall;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//THIS IS THE CLASS THAT SHOWS THE UI FOR THE UER WHEN THE SCHEDULER CALLS

public class MisscallInterface extends AppCompatActivity {

    private int countMessageID;
    private ValueAnimator animators=new ValueAnimator();
    private TextView actualPopMessage;
    private TextView misscallInterfaceCaller;
    private Typeface typeface;
    private MinorDetails messageObject;
    private ImageView callerImageIcon;
    private List<String> staticMessagesForMalla=new ArrayList<>();
    private List<String> staticMessagesForSwornim=new ArrayList<>();
    private static MediaPlayer mediaPlayer;
    private String filteredNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misscall_interface);

        callerImageIcon=(ImageView) findViewById(R.id.callerImageIcon);
        actualPopMessage = (TextView) findViewById(R.id.actualPopMessage);
        misscallInterfaceCaller = (TextView) findViewById(R.id.misscallInterfaceCaller);
        typeface=Typeface.createFromAsset(getAssets(),"ArchitectsDaughter.ttf");
        actualPopMessage.setTypeface(typeface);



        countMessageID=new CustomSharedPref(getApplicationContext()).getSharedPrefInt("countMessageID");//restore back to zero
        new CustomSharedPref(getApplicationContext()).setSharedPrefInt("countMessageID",0);//restore back to zero

        if(getIntent()!=null) {
            Intent getIntent = getIntent();
            MinorDetails messageObject = (MinorDetails) getIntent.getSerializableExtra("messageObject");
        }

        if(messageObject!=null) {



                 filteredNumber = messageObject.getContactNumber().replaceAll("[-=+()/!~|*&^#@$:'{}_abcdefghijklmnopqrstuvwxyz;%`.,]", "");
                filteredNumber.substring(filteredNumber.length() - 10);
                if (filteredNumber.length() > 10) {
                    //remove the first n characters
                    filteredNumber = filteredNumber.substring(filteredNumber.length() - 10);
                }
                Log.i("mytag","filtered number"+filteredNumber);


                this.messageObject = messageObject;

//                Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), messageObject.getContactNumber());
//                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//                roundedBitmapDrawable.setCircular(true);
//                callerImageIcon.setImageDrawable(roundedBitmapDrawable);
            }

        showMeTheMessage(filteredNumber);
    }


//    /adding the osngs at the background during the misscall user can ge the friends songs just to see and getting their phoen location



    public void showMeTheMessage(String filteredNumber){

        if(countMessageID<7){
            mediaPlayer= MediaPlayer.create(this, R.raw.ringtone);
            mediaPlayer.start();
            String message=new CustomSharedPref(getApplicationContext()).getSharedPref(filteredNumber+"message"+countMessageID);
            actualPopMessage.setText(message);
            actualPopMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                }
            });
            notificationBar(filteredNumber);

       }else{
            mediaPlayer= MediaPlayer.create(this, R.raw.ringtone);
            mediaPlayer.start();
            actualPopMessage.setText("hello");
            Toast.makeText(getApplicationContext(),filteredNumber+ " misscalled you",Toast.LENGTH_LONG).show();
        }
    }






    public  Bitmap retrieveContactPhoto(Context context,String number) {

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

    public void notificationBar(String filteredNumber){
        Intent newIntent=new Intent(getApplicationContext(),MisscallInterface.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,newIntent,0);

        String message=new CustomSharedPref(getApplicationContext()).getSharedPref(filteredNumber+"message"+countMessageID);

        NotificationManager mNotificationManager=(NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Notification mNotification=new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_notifications_black_48dp)
               .setContentTitle("Offline Message")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .build();

        mNotification.flags=Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(100,mNotification);

    }


    public class UpdateContacts extends AsyncTask<Void,Void,String> {
        private String callerNumber;
        private String callerName;


        public UpdateContacts(String callerNumber) {
            this.callerNumber=callerNumber;
        }

        @Override
        protected String doInBackground(Void ... voids) {

            //TODO UPDATE AND ALERT THE ADAPTER

            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    while (cursor.moveToNext()) {

                        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        MinorDetails minorDetails1 = new MinorDetails();
                        minorDetails1.setContactNumber(phoneNumber);

                        String filteredNumber = phoneNumber.replaceAll("[-=+()/!~|*&^$#@:'{}.,/n]", "");
                        if (filteredNumber.length() > 10) {
                            //remove the first n characters
                            filteredNumber = filteredNumber.substring(filteredNumber.length() - 10);

                        }

                         callerNumber = callerNumber.replaceAll("[-=+()/!~|*&^$#@:'{}.,/n]", "");
                        callerNumber.substring(callerNumber.length() - 10);
                        if (callerNumber.length() > 10) {
                            //remove the first n characters
                            callerNumber = callerNumber.substring(callerNumber.length() - 10);
                        }

                        if(filteredNumber.equals(callerNumber)){
                            callerName=username;
                            break;
                        }
                    }

                }
                cursor.close();
            }
            return callerName;

        }



        @Override
        protected void onPostExecute(String numbersResUserName) {
            misscallInterfaceCaller.setText(numbersResUserName);
        }
    }

}
