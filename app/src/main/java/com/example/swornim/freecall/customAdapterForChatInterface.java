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


    private List<UserDatabaseInformation> sourceBucket=new ArrayList<>();//its an array list container
    private Context context;
    private String clicked="nope";


    public customAdapterForChatInterface(Context context,List<UserDatabaseInformation> sourceBucket) {
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
        TextView leftMessage=(TextView) mView.findViewById(R.id.leftMessage);
        leftMessage.setTypeface(typeface1);

        if(sourceBucket.get(position).getMes()!=null){
            leftMessage.setText(sourceBucket.get(position).getMes());
        }


        TextView seentextview=(TextView) mView.findViewById(R.id.statusReport);
        final ImageView photoMessageView=(ImageView) mView.findViewById(R.id.imageMessasgeView);

        if(sourceBucket.get(position).getPhotoM()==null){
            photoMessageView.getLayoutParams().width=0;
            photoMessageView.getLayoutParams().height=0;
        }

        else if(sourceBucket.get(position).getPhotoM()!=null){
            photoMessageView.getLayoutParams().width=200;
            photoMessageView.getLayoutParams().height=200;
        }

        if(sourceBucket.get(position).getSen()!=null)
        seentextview.setText(sourceBucket.get(position).getSen());


            if (sourceBucket.get(position).getPhotoM()!=null ) {

                if(sourceBucket.get(position).getPhotoM().equals("yup")) {

                    Glide.with(getContext()).
                            load(sourceBucket.get(position).getPhotoUrl())
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(photoMessageView);
                }
            }

            photoMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Toast.makeText(getContext(), "Full screen not available broo", Toast.LENGTH_LONG).show();

                }
            });

       // whoCameToChat.setText(currentAppUserName);

        return mView;

    }



}
