package com.example.swornim.musicnap.customAdapterPackage;

/**
 * Created by Swornim on 1/21/2017.
 */
public class UserDatabaseInformation {

    //just for the UI displaying of messages
    private String currentMessageTobeSent;


    private String currentAppUserName;
    private String phoneNumber;

    //friendslist
    private String friendName;
    private String favourite;

    //songslist
    private String songName;


    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getCurrentMessageTobeSent() {
        return currentMessageTobeSent;
    }

    public void setCurrentMessageTobeSent(String currentMessageTobeSent) {
        this.currentMessageTobeSent = currentMessageTobeSent;
    }



    public String getCurrentAppUserName() {
        return currentAppUserName;
    }

    public void setCurrentAppUserName(String currentAppUserName) {
        this.currentAppUserName = currentAppUserName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }


}
