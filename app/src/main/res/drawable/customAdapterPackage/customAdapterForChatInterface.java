package com.example.swornim.musicnap.customAdapterPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.swornim.musicnap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swornim on 12/23/2016.
 */
public class customAdapterForChatInterface extends ArrayAdapter<com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation> {


    private List<com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation> sourceBucket=new ArrayList<>();//its an array list container
    private Context context;


    public customAdapterForChatInterface(Context context,List<com.example.swornim.musicnap.customAdapterPackage.UserDatabaseInformation> sourceBucket) {
        super(context,R.layout.custommessagelist,sourceBucket);
        this.context=context;
        this.sourceBucket=sourceBucket;
    }

//sourcebucket is now the list of arrays containing objects


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Comfortaa-Light.ttf");
        View mView = convertView;
        if(convertView==null) {

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            mView = layoutInflater.inflate(R.layout.left_message, parent, false);
        }
        String message=sourceBucket.get(position).getCurrentMessageTobeSent();
        String currentAppUserName=sourceBucket.get(position).getCurrentAppUserName();


        TextView leftMessage=(TextView) mView.findViewById(R.id.leftMessage);
        TextView whoCameToChat=(TextView) mView.findViewById(R.id.whoCameToChat);

        leftMessage.setTypeface(typeface1);

        leftMessage.setText(message);
        whoCameToChat.setText(currentAppUserName);

        return mView;

    }



}
