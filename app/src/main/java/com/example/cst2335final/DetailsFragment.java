package com.example.cst2335final;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    public DetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the fragment_details view and storing result
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        //Getting data from Favorites_List activity
        Bundle dataFromActivity = getArguments();

        //Widgets from fragment_details view
        TextView latitudeTextView = view.findViewById(R.id.fd_latitude);
        TextView longitudeTextView = view.findViewById(R.id.fd_longitude);
        TextView dateTextView = view.findViewById(R.id.fd_date);

        //Setting new values for the view
        latitudeTextView.setText(dataFromActivity.getString(Favorites_List.LATITUDE));
        longitudeTextView.setText(dataFromActivity.getString(Favorites_List.LONGITUDE));
        dateTextView.setText(dataFromActivity.getString(Favorites_List.DATE));

        return view;
    }
}
