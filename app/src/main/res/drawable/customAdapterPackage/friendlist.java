package com.example.swornim.musicnap.customAdapterPackage;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swornim.musicnap.MainActivity;
import com.example.swornim.musicnap.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class friendlist extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> containerForAdapter=new ArrayList<>();
    private ListView friendchatList;
    private TextView snapCallUserName;
    private TextView snap_profile_timer;
    private TextView musicStatus;
    private TextView lastOnline;
    private TextView snapCallSongName;
    private Button btnmessageHimHer;
    private Button btnSnapMusic;
    private ImageView userProfilePic;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        friendchatList =(ListView) findViewById(R.id.friendchatlist);
        snapCallUserName=(TextView) findViewById(R.id.snapCallUserName);

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,containerForAdapter);
        friendchatList.setAdapter(adapter);
        InitializeContainer();

        friendchatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                setContentView(R.layout.snap_profile_for_chat);
                notifyAllOnclickListener();

            }
        });

        friendchatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
/*
                Dialog snapDialog=new Dialog(friendlist.this);//instead of getapplicationcontext rather use this
                snapDialog.setContentView(R.layout.snapcall);

                ImageView imageView=(ImageView) snapDialog.findViewById(R.id.imageSnapcall);
                imageView.setImageResource(R.mipmap.snapview);

                TextView snapCallUserName=(TextView) snapDialog.findViewById(R.id.snapCallUserName);
                snapCallUserName.setText(friendchatList.getItemAtPosition(position).toString());

                TextView snapCallSongName =(TextView) snapDialog.findViewById(R.id.snapCallSongName);
                snapCallSongName.setText("Timberlake - Raining");

                snapDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                snapDialog.setTitle("Snap Music Request");

                snapDialog.show();

*/

                return false;


            }
        });


    }



    public void notifyAllOnclickListener(){

        musicStatus=(TextView) findViewById(R.id.snap_profile_status);
        lastOnline=(TextView) findViewById(R.id.lastOnline);
        snap_profile_timer=(TextView) findViewById(R.id.snap_profile_timer);
        userProfilePic=(ImageView) findViewById(R.id.snap_profile_userImage);
        btnmessageHimHer=(Button) findViewById(R.id.btnmessageHimHer);
        btnSnapMusic=(Button) findViewById(R.id.btnSnapMusicProf);

        btnmessageHimHer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), com.example.swornim.musicnap.customAdapterPackage.SnapMusicHomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        btnSnapMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userSongFilePath= new com.example.swornim.musicnap.customAdapterPackage.CustomSharedPreferance(getApplicationContext()).getSharedPref("userSongFilePath","none");
                final ValueAnimator animator=new ValueAnimator();
                animator.setObjectValues(6,1);
                animator.setDuration(6000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        snap_profile_timer.setText(""+animator.getAnimatedValue());
                    }
                });

                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        snap_profile_timer.setText("LETS'S MUSIC SNAP");


                        Thread thread=new Thread(){
                            @Override
                            public void run() {

                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }finally {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run(){

                                            try {
                                                com.example.swornim.musicnap.customAdapterPackage.MediaPlayerClass.setUpMediaPlayer(getApplicationContext(),userSongFilePath,new MediaPlayer());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(new Intent(getApplicationContext(), com.example.swornim.musicnap.customAdapterPackage.SnapMusicHomePage.class));



                                        }
                                    });
                                }

                            }
                        };

                        thread.start();




                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                animator.start();





            }
        });

    }


    public void InitializeContainer(){

        com.example.swornim.musicnap.customAdapterPackage.MSQLiteDatabase msqLiteDatabase=new com.example.swornim.musicnap.customAdapterPackage.MSQLiteDatabase(getApplicationContext());
        Map<String,Object> contactListMap=msqLiteDatabase.readAllContacts();

        for(Map.Entry<String,Object> singleEntry : contactListMap.entrySet()) {
            containerForAdapter.add(singleEntry.getKey().toLowerCase());
        }
        adapter.notifyDataSetChanged();//it notifies the source which the adapter is using

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
}
