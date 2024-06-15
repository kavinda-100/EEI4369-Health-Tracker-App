package com.s22010170.heathtrakerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.s22010170.heathtrakerapp.adapters.EDUAdapter;

public class EducationalResourcesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    EDUAdapter eduAdapter;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_educational_resources, container, false);
        // define variables
        tabLayout = rootView.findViewById(R.id.edu_tab_layout);
        viewPager = rootView.findViewById(R.id.edu_view_pager);
        fab = rootView.findViewById(R.id.open_map_button);

        // set up the view pager and tab layout
        eduAdapter = new EDUAdapter(requireActivity());
        viewPager.setAdapter(eduAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void
            onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

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