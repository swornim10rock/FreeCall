package com.example.swornim.freecall;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swornim on 12/23/2016.
 */
public class customAdapterForChatInterface extends ArrayAdapter<UserDatabaseInformation> {


    private List<UserDatabaseInformation> sourceBucket = new ArrayList<>();//its an array list container
    private Context context;
    private String clicked = "nope";
    private String which;


    public customAdapterForChatInterface(Context context, List<UserDatabaseInformation> sourceBucket) {
        super(context, R.layout.custommessagelist, sourceBucket);
        this.context = context;
        this.sourceBucket = sourceBucket;
    }

//sourcebucket is now the list of arrays containing objects


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "Comfortaa-Light.ttf");
        View mView = convertView;


            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            if (sourceBucket.get(position).getPhotoM() != null) {
                if (sourceBucket.get(position).getPhotoM().equals("yup")) {
                    which = "photo";
                    mView = layoutInflater.inflate(R.layout.photolayout, parent, false);
                }
            } else if (sourceBucket.get(position).getSen() != null) {
                if (sourceBucket.get(position).getSen().equals("seen"))
                    which = "seen";
                mView = layoutInflater.inflate(R.layout.seenlayout, parent, false);

            } else { //id not null means sender message

                which = "lefttext";
                mView = layoutInflater.inflate(R.layout.left_message, parent, false);
            }


            if (which.equals("photo")) {
                final ImageView photoMessageView = (ImageView) mView.findViewById(R.id.imageMessasgeView);

                Glide.with(getContext()).
                        load(sourceBucket.get(position).getPhotoUrl())
                        .centerCrop()
//                    .skipMemoryCache(true)
                        .into(photoMessageView);
            }

            if (which.equals("lefttext")) {
                TextView leftMessage = (TextView) mView.findViewById(R.id.leftMessage);
                TextView username = (TextView) mView.findViewById(R.id.userName);
                leftMessage.setTypeface(typeface1);

                leftMessage.setText(sourceBucket.get(position).getMes());
                username.setText(sourceBucket.get(position).getUserName());
            }

            if (which.equals("seen")) {


                TextView status = (TextView) mView.findViewById(R.id.status);
                status.setText(sourceBucket.get(position).getSen());

            }

        return mView;

    }
}


