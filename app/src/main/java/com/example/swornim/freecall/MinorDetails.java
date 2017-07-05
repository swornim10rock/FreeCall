package com.example.swornim.freecall;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Swornim on 5/8/2017.
 */

public class MinorDetails implements Serializable {

    //callogs varaibles
    private String userContactID;
    private String userContactName;
    private String userContactNumber;
    private String userMessage;
    private String hhmmss;
    private List<MinorDetails> postdata;


    //phonecalls adapter variables
    private String contactName;
    private String contactNumber;


    //OFFLINE JSON MESSAGES
    private String offlineJsonMessages;
    private String messages;





    public static int getUniqueAlarmPendingId() {
        return uniqueAlarmPendingId;
    }

    public static void setUniqueAlarmPendingId(int uniqueAlarmPendingId) {
        MinorDetails.uniqueAlarmPendingId = uniqueAlarmPendingId;
    }


    //misscall interfaces variables


    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getOfflineJsonMessages() {
        return offlineJsonMessages;
    }

    public void setOfflineJsonMessages(String offlineJsonMessages) {
        this.offlineJsonMessages = offlineJsonMessages;
    }

    public static int uniqueAlarmPendingId=111;


    public List<MinorDetails> getPostdata() {
        return postdata;
    }

    public void setPostdata(List<MinorDetails> postdata) {
        this.postdata = postdata;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getHhmmss() {
        return hhmmss;
    }

    public void setHhmmss(String hhmmss) {
        this.hhmmss = hhmmss;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserContactName() {
        return userContactName;
    }

    public void setUserContactName(String userContactName) {
        this.userContactName = userContactName;
    }

    public String getUserContactID() {
        return userContactID;
    }

    public void setUserContactID(String userContactID) {
        this.userContactID = userContactID;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }
}
