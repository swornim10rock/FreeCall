package com.example.swornim.musicnap.customAdapterPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swornim on 11/29/2016.
 */
public class songList {

    private ArrayList<File> collectedSongsList=new ArrayList<File>();
    private List<com.example.swornim.musicnap.customAdapterPackage.songList> songs=new ArrayList<com.example.swornim.musicnap.customAdapterPackage.songList>();
    private String songName;

    public songList(ArrayList<File> collectedSongsList) {
        this.collectedSongsList = collectedSongsList;
    }

    public songList(){}
    public songList(String songName){
        this.songName=songName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public ArrayList<File> syncAllSongs(File root){

        File[] files= root.listFiles();//get all the files array from the root path
        for(File singleFile: files){
            if(singleFile.isDirectory()){
                //doesnot search through inner directories i.e no back flow
                syncAllSongs(singleFile);
            }else{
                if(singleFile.getName().endsWith(".mp3")
                        || singleFile.getName().endsWith("wmv")
                        || singleFile.getName().endsWith(".m4a"))


                            collectedSongsList.add(singleFile);
                    }
                }

     return collectedSongsList;
    }


    public ArrayList<String> getSongsName(ArrayList<File> collectedSongsList){
        ArrayList<String> nameOfSong= new ArrayList<String >();

        for(int i=0;i<collectedSongsList.size();i++){
            nameOfSong.add(collectedSongsList.get(i).getName());
        }

        return nameOfSong;
    }





}
