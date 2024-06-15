package com.s22010170.heathtrakerapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class MapFragment extends Fragment {
    ShowMessage showMessage;
    EditText originText, destinationText;
    Button btnDirection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        // define the views
        originText = rootView.findViewById(R.id.startText);
        destinationText = rootView.findViewById(R.id.EndText);
        btnDirection = rootView.findViewById(R.id.go_to_map_button);

        showMessage = new ShowMessage();

        // set the click listener
        btnDirection.setOnClickListener(v -> {
            String startPoint = originText.getText().toString();
            String endPoint = destinationText.getText().toString();

            if(startPoint.isEmpty() || endPoint.isEmpty()){
                // show a toast message if the fields are empty
                showMessage.show("Error", "Please enter starting point and destination", getActivity());
            }
            else{
                // call the getPath method
                getPath(startPoint,endPoint);
            }
        });

        return rootView;
    }

    private void getPath(String startPoint, String endPoint) {
        try{
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + startPoint + "/" + endPoint);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        catch (ActivityNotFoundException exception){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }
}