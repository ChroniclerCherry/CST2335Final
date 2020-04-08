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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BingDetailsFragment extends Fragment {
    /**
     * Default constructor
     */
    public BingDetailsFragment() {}

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

        //Inflate the bing_fragment_details view and storing result
        View view = inflater.inflate(R.layout.bing_fragment_details, container, false);

        //Getting data from Favorites_List activity
        Bundle dataFromActivity = getArguments();

        //Widgets from bing_fragment_details view
        TextView latitudeTextView = view.findViewById(R.id.favDetails_latitude);
        TextView longitudeTextView = view.findViewById(R.id.favDetails_longitude);
        TextView urlPathTextView = view.findViewById(R.id.favDetails_imageurl);
        TextView filePathTextView = view.findViewById(R.id.favDetails_filePath);

        //Setting new values for the view
        latitudeTextView.setText(getResources().getString(R.string.latitude) + " " + dataFromActivity.getString(BingFavoritesList.LATITUDE));
        longitudeTextView.setText(getResources().getString(R.string.longitude) + " " + dataFromActivity.getString(BingFavoritesList.LONGITUDE));
        urlPathTextView.setText(getResources().getString(R.string.view_online) + " " + dataFromActivity.getString(BingFavoritesList.URL_PATH));
        filePathTextView.setText(getResources().getString(R.string.saved_on_device) + " " + dataFromActivity.getString(BingFavoritesList.FILE_PATH));

        return view;
    }
}
