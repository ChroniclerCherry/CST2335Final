package com.example.cst2335final;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class NewsReaderFragment extends Fragment {

    private Bundle dataFromActivity;
    private String title;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false);
        dataFromActivity = getArguments();
        title = dataFromActivity.getString(NewsReaderSearch.TITLE );

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.news_reader_fragment, container, false);

        //show the title
        TextView title = result.findViewById(R.id.textViewTitle);
        title.setText("test");
        //title.setText(dataFromActivity.getString(String.valueOf(title)));
//        //show the date
//        TextView date = (TextView) result.findViewById(R.id.textViewDate);
//        date.setText(dataFromActivity.get());
//        //show the description
//        TextView desc = (TextView) result.findViewById(R.id.textViewDesc);
//        desc.setText(dataFromActivity.getString());
//        //show the date
//        TextView link = (TextView) result.findViewById(R.id.textViewLink);
//        link.setText(dataFromActivity.get());


        // get the fave button, and add a click listener:
        Button faveBtn = result.findViewById(R.id.buttonFave);
        faveBtn.setOnClickListener(clk -> {

        });
        return result;

//        // get the back button, and add a click listener:
//        Button backBtn = result.findViewById(R.id.buttonBack);
//        backBtn.OnClickListener( clk -> {
//        //Tell the parent activity to remove
//            parentActivity.finish();
//            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
//
//        });
    }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            parentActivity = (AppCompatActivity) context;
        }
    }

