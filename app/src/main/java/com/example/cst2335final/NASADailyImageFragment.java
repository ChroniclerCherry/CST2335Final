package com.example.cst2335final;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NASADailyImageFragment extends Fragment {

    private Bundle dataFromActivity;
    private boolean isTablet;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_nasa_daily_image, container, false);

        if(isTablet) { //both the list and details are on the screen:
            NASADailyFavourites parent = (NASADailyFavourites)getActivity();
            parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else //You are only looking at the details, you need to go back to the previous list page
        {
            EmptyActivity parent = (EmptyActivity) getActivity();
            Intent backToFragmentExample = new Intent();

            parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
            parent.finish(); //go back
        }

        // Inflate the layout for this fragment
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    public void setTablet(boolean b) {
        this.isTablet = b;
    }
}
