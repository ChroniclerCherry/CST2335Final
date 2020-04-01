package com.example.cst2335final;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class NASADailyImageFragment extends Fragment {

    private Bundle dataFromActivity;
    private boolean isTablet;
    private AppCompatActivity parentActivity;

    private ImageView imageView;
    private TextView titleText;
    private TextView dateText;
    private TextView descriptionText;
    private Button unfavouriteButton;
    private Button closeButton;

    private String title;
    private String date;
    private String description;

    private Bitmap image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_nasa_daily_image, container, false);

        //get references to all the GUI components
        imageView = (ImageView)result.findViewById(R.id.NASADaily_image);
        titleText = (TextView)result.findViewById(R.id.NASADaily_title);
        dateText = (TextView)result.findViewById(R.id.NASADaily_imageDate);
        descriptionText = (TextView)result.findViewById(R.id.NASADaily_description);
        unfavouriteButton = (Button)result.findViewById(R.id.NASADaily_unfavouriteButton);
        closeButton = (Button)result.findViewById(R.id.NASADaily_closeImageButton);

        //make the description scrollable
        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        //get data from activity
        date = dataFromActivity.getString(NASADailyFavourites.IMAGE_DATE);
        title = dataFromActivity.getString(NASADailyFavourites.IMAGE_TITLE);
        description = dataFromActivity.getString(NASADailyFavourites.IMAGE_DESCRIPTION);

        //load the image
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput("NASADaily" + date + ".png");
            image = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e){
            //image might be null as some dates don't provide an image
            image = null;
        }

        //set the data to the GUI elements
        imageView.setImageBitmap(image);
        titleText.setText(title);
        dateText.setText(date);
        descriptionText.setText(description);

        //set behaviour for close button
        closeButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                NASADailyFavourites parent = (NASADailyFavourites)getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                NASADailyEmptyActivity parent = (NASADailyEmptyActivity) getActivity();
                Intent closeFragment = new Intent();
                closeFragment.putExtra(NASADailyFavourites.IMAGE_DATE, date);
                parent.setResult(Activity.RESULT_OK, closeFragment);
                parent.finish(); //go back
            }
        });

        unfavouriteButton.setOnClickListener(click -> {
            if(isTablet) { //both the list and details are on the screen:
                NASADailyFavourites parent = (NASADailyFavourites)getActivity();
                parent.removeImage(new NASAImage(date));
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                NASADailyEmptyActivity parent = (NASADailyEmptyActivity) getActivity();
                Intent closeFragment = new Intent();
                closeFragment.putExtra(NASADailyFavourites.IMAGE_DATE,date);
                parent.setResult(NASADailyFavourites.REMOVE_IMAGE, closeFragment);
                parent.finish(); //go back
            }
        });

        // Inflate the layout for this fragment
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be FragmentExample for a tablet, or NASADailyEmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    public void setTablet(boolean b) {
        this.isTablet = b;
    }
}
