package com.example.swornim.musicnap.customAdapterPackage;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Swornim on 11/29/2016.
 */
public class MediaPlayerClass extends AppCompatActivity {

    private static MediaPlayer mediaPlayerOnPause;
    private final int VIDEO_REQUEST_CODE = 100;
    private Button recordButton;

    private String filepath;
    private Uri uri;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }


    public static void setUpMediaPlayer(Context context, String filepath,MediaPlayer mediaPlayer) throws IOException {


        if(mediaPlayerOnPause==null) {


            mediaPlayerOnPause=mediaPlayer;
            mediaPlayer.setDataSource(context, Uri.parse(filepath));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();//plays songs
        }
        else if(mediaPlayerOnPause.isPlaying()){
            //TODO create one and send to the snapPlayer class

            mediaPlayerOnPause.reset();
            mediaPlayerOnPause.stop();

            //if its playing then stop and play the new request from user

            mediaPlayerOnPause=mediaPlayer;
            mediaPlayer.setDataSource(context, Uri.parse(filepath));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();//plays songs

        }else{}

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!mediaPlayerOnPause.isPlaying())
            mediaPlayerOnPause.start();


    }
}
