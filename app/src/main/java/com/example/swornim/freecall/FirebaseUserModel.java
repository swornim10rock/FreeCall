package com.example.swornim.freecall;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swornim on 1/26/2017.
 */
public class FirebaseUserModel {

    private DatabaseReference mdatabaseReference= FirebaseDatabase.getInstance().getReference("users/freecall/");
    private DatabaseReference editDatabaseReference= FirebaseDatabase.getInstance().getReference("users/");
    private DatabaseReference registerYourselfRef = FirebaseDatabase.getInstance().getReference("users/");
    //songs details variables
    private static String S0NGNAME;

    //instant status variables
    private static String ONLINE_OFFLINE="ONLINE_OFFLINE";
    private static String LISTENING="LISTENING";
    private static String EAR_PLUGGED_OR_NOT="EAR_PLUGGED_OR_NOT";
    private static String WHO_IS_ONLINE="WHO_IS_ONLINE";


    //general variables
    private Context context;
    private static String talkWithHim;
    private static String mMessage;//actual message



    public static String getWhoIsOnline() {
        return WHO_IS_ONLINE;
    }

    public static void setWhoIsOnline(String whoIsOnline) {
        WHO_IS_ONLINE = whoIsOnline;
    }

    public static String getS0NGNAME() {
        return S0NGNAME;
    }

    public static void setS0NGNAME(String s0NGNAME) {
        S0NGNAME = s0NGNAME;
    }

    public static String getOnlineOffline() {
        return ONLINE_OFFLINE;
    }

    public static void setOnlineOffline(String onlineOffline) {
        ONLINE_OFFLINE = onlineOffline;
    }

    public static String getLISTENING() {
        return LISTENING;
    }

    public static void setLISTENING(String LISTENING) {
        FirebaseUserModel.LISTENING = LISTENING;
    }

    public static String getEarPluggedOrNot() {
        return EAR_PLUGGED_OR_NOT;
    }

    public static void setEarPluggedOrNot(String earPluggedOrNot) {
        EAR_PLUGGED_OR_NOT = earPluggedOrNot;
    }

    public String getTalkWithHim() {
        return talkWithHim;
    }

    public void setTalkWithHim(String talkWithHim) {
        this.talkWithHim = talkWithHim;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }




    public class InstantMessaging extends AsyncTask<Void,Void,Void>{

        private Context context;
        private Map<String,Object> map=new HashMap<>();
        private UserDatabaseInformation messageObject;
        private UserDatabaseInformation myselfInfo;



        public InstantMessaging(Context context,UserDatabaseInformation messageObject,UserDatabaseInformation myselfInfo) {
            this.context=context;
            this.messageObject=messageObject;
            this.myselfInfo=myselfInfo;//it contains itself and receiver  numbere

        }

        @Override
        protected Void doInBackground(Void...params){

                mdatabaseReference
                        .child("users/"+myselfInfo.getReceiverPhoneNumber()+"/friends/"+myselfInfo.getPhoneNumber()+"/chats/")
                        .push()
                        .setValue(messageObject);
                 return null;
        }

    }

    public class editOfflineMessagesForFreinds extends AsyncTask<Void,Void,Void>{

        private String messageObject;
        private int whichMessageToEdit;




        public editOfflineMessagesForFreinds(Context context,String messageObject,int whichMessageToEdit) {
            this.messageObject=messageObject;
            this.whichMessageToEdit=whichMessageToEdit;
//
        }

        @Override
        protected Void doInBackground(Void...params){

            editDatabaseReference.
                    child("freecall/users/9813847444/friends/9860569432/offlineMessages/message"+whichMessageToEdit+"/").
                    setValue(messageObject);


            //also update ur own ui for that new messages



            return null;
        }

    }

    //whenever users updates the offline messages the ui should also be synced
public class updateMessageSycn extends AsyncTask<Void,Void,Void>{

        private Context context;
        private Map<String,Object> map=new HashMap<>();
        private UserDatabaseInformation messageObject;





        public updateMessageSycn(Context context,UserDatabaseInformation messageObject) {
            this.context=context;
            this.messageObject=messageObject;
//            this.myselfInfo=messageObject.getMyselfNumber();
//            this.yourFriendNode=messageObject.getPhoneNumber();

        }

        @Override
        protected Void doInBackground(Void...params){

            switch (messageObject.getWhichMessageToEdit()){
//                case  0:
//                    whichMessageToEdit=messageObject.getMessages1();
//                    messageObject.setMessages1(messageObject.getCommonMessage());
//                    break;
//                case  1:
//                    whichMessageToEdit=messageObject.getMessages2();
//                    messageObject.setMessages2(messageObject.getCommonMessage());
//
//                    break;
//                case  2:
//                    whichMessageToEdit=messageObject.getMessages3();
//                    messageObject.setMessages3(messageObject.getCommonMessage());
//
//                    break;
//                case  3:
//                    whichMessageToEdit=messageObject.getMessages4();
//                    messageObject.setMessages4(messageObject.getCommonMessage());
//
//                    break;
//
//                case  4:
//                    whichMessageToEdit=messageObject.getMessages5();
//                    messageObject.setMessages5(messageObject.getCommonMessage());
//
//                    break;
//
//                case  5:
//                    whichMessageToEdit=messageObject.getMessages6();
//                    messageObject.setMessages6(messageObject.getCommonMessage());

                   // break;
                default:
                    //do nothing

            }

            Log.i("mytag","messages key"+messageObject.getMessagesUniqueKey());
            String myPhoneNumber=new CustomSharedPref(context).getSharedPref("userPhoneNumber");

            editDatabaseReference.child(messageObject.getReceiverPhoneNumber()+"/friends/"+messageObject.getPhoneNumber()+"/messages/").child(messageObject.getMessagesUniqueKey())
                       .setValue(messageObject);
            //listening to my nodes friends messages that i updated




            return null;
        }

    }

    //  registering process first time call
    public class AddContactsToFirebase extends AsyncTask<Void,Void,Void>{
        private Context context;
        private List<MinorDetails> registerObject;
        private UserDatabaseInformation messageObject;

        public AddContactsToFirebase(Context context,UserDatabaseInformation messageObject) {
            this.context=context;
            this.messageObject =messageObject;
        }

        @Override
        protected Void doInBackground(Void...params){

            UserDatabaseInformation chatsFormat=new UserDatabaseInformation();
            chatsFormat.setMes("WelCome !!Start Your First Conversation");
            chatsFormat.setReceiverPhoneNumber("userphonnumber");
            chatsFormat.setReceiverUserName("username");


            UserDatabaseInformation chatMessaege=new UserDatabaseInformation();
            chatMessaege.setMes("Welcome");


          for(int i=0;i<messageObject.getList().size();i++){
              registerYourselfRef.child("freecall/users/"+messageObject.getPhoneNumber()+"/friends/"+messageObject.getList().get(i).getFriensNumber()+"/chats/").push().setValue(chatMessaege);
          }
           return null;
        }

    }


}


