package com.example.swornim.freecall;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swornim on 1/21/2017.
 * THIS CLASS IS USED FOR THE SERVER SIDE MINOR DETAILS BUT NOT IN THE INNER PROGRAMS
 */
public class UserDatabaseInformation {


    //note that if you make a int variable then by default the values are zero which means user
    //when sending firebase message will include this attributes by default

    private String mes;
    private String sen;

    private String numberOfMiscalls;


    //friendslist
    private String friendName;
    private String favourite;

    //songslist
    private String songName;


    //block for the new registering users
    private String userName;
    private String userUniqueId;
    private String chats;
    private String lastOnline;
    private String phoneNumber;
    private String receiverUserName;
    private String receiverPhoneNumber;

    private String myselfNumber;
    private String whichMessageToEdit;
    private String hiddenMessageUniqueKey;
    private String messagesUniqueKey;
    private String commonMessage;
    private String photoM;
    private String photoUrl;
    private String friensNumber;


    public String getFriensNumber() {
        return friensNumber;
    }

    public void setFriensNumber(String friensNumber) {
        this.friensNumber = friensNumber;
    }

    private List<UserDatabaseInformation> list;


    public List<UserDatabaseInformation> getList() {
        return list;
    }

    public void setList(List<UserDatabaseInformation> list) {
        this.list = list;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoM() {
        return photoM;
    }

    public void setPhotoM(String photoM) {
        this.photoM = photoM;
    }

    public String getMes() {
        return mes;
    }

    public String getSen() {
        return sen;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }

    //json data
    private String message1;
    private String message2;
    private String message3;
    private String message4;
    private String message5;
    private String message6;


    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public String getMessage3() {
        return message3;
    }

    public void setMessage3(String message3) {
        this.message3 = message3;
    }

    public String getMessage4() {
        return message4;
    }

    public void setMessage4(String message4) {
        this.message4 = message4;
    }

    public String getMessage5() {
        return message5;
    }

    public void setMessage5(String message5) {
        this.message5 = message5;
    }

    public String getMessage6() {
        return message6;
    }

    public void setMessage6(String message6) {
        this.message6 = message6;
    }



    public String getMessagesUniqueKey() {
        return messagesUniqueKey;
    }

    public void setMessagesUniqueKey(String messagesUniqueKey) {
        this.messagesUniqueKey = messagesUniqueKey;
    }

    public String getCommonMessage() {
        return commonMessage;
    }

    public void setCommonMessage(String commonMessage) {
        this.commonMessage = commonMessage;
    }

    public String getHiddenMessageUniqueKey() {
        return hiddenMessageUniqueKey;
    }

    public void setHiddenMessageUniqueKey(String hiddenMessageUniqueKey) {
        this.hiddenMessageUniqueKey = hiddenMessageUniqueKey;
    }

    public String getWhichMessageToEdit() {
        return whichMessageToEdit;
    }

    public void setWhichMessageToEdit(String whichMessageToEdit) {
        this.whichMessageToEdit = whichMessageToEdit;
    }

    public String getMyselfNumber() {
        return myselfNumber;
    }

    public void setMyselfNumber(String myselfNumber) {
        this.myselfNumber = myselfNumber;
    }


    public String getNumberOfMiscalls() {
        return numberOfMiscalls;
    }

    public void setNumberOfMiscalls(String numberOfMiscalls) {
        this.numberOfMiscalls = numberOfMiscalls;
    }



    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getChats() {
        return chats;
    }

    public void setChats(String chats) {
        this.chats = chats;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }


    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
