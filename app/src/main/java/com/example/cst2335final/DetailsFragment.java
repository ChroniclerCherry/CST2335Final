/**Course Name: CST2335
 * Class Name: Nasa_Earthy_Image_Db
 * Date: 03/26/2020
 *
 * Class for inflating a view to be used in the Fragment
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */

package com.example.cst2335final;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {
    /**
     * Default constructor
     */
    public DetailsFragment() {}

    /**
     * Method will inflate a view, get data passed from a Bundle object, and set new values for the widgets in the view.
     * @param inflater LayoutInflater object to inflate our view
     * @param container ViewGroup object
     * @param savedInstanceState Bundle Object for passing data to the Fragment
     * @return View object
     */
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
        TextView urlPathTextView = view.findViewById(R.id.fd_url);

        //Setting new values for the view
        latitudeTextView.setText(dataFromActivity.getString(Favorites_List.LATITUDE));
        longitudeTextView.setText(dataFromActivity.getString(Favorites_List.LONGITUDE));
        dateTextView.setText(dataFromActivity.getString(Favorites_List.DATE));
        urlPathTextView.setText(dataFromActivity.getString(Favorites_List.URL_PATH));

        return view;
    }
}
