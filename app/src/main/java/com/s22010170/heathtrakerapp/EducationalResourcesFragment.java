package com.s22010170.heathtrakerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EducationalResourcesFragment extends Fragment {

    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_educational_resources, container, false);
        // define variables
        fab = rootView.findViewById(R.id.open_map_button);

        // set on click listener for the floating action button to open the map fragment
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment mapFragment = new MapFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, mapFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("fromEDUFragment")
                        .commit();
            }
        });

        return rootView;
    }
}