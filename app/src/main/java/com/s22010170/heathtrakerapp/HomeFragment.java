package com.s22010170.heathtrakerapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.DbBitmapUtility;
import com.s22010170.heathtrakerapp.dataClass.MedicationListModel;
import com.s22010170.heathtrakerapp.interfaces.MedicationListRecyclerViewInterface;
import com.s22010170.heathtrakerapp.adapters.MedicationListRecyclerviewAdapter;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MedicationListRecyclerViewInterface {
    DataBaseHelper dataBaseHelper;
    SharedPrefsManager prefsManager;
    ShowMessage showMessage;
    ArrayList<MedicationListModel> medicationList = new ArrayList<MedicationListModel>();
    RecyclerView medicationHomeRecyclerView;
    ImageView userImage;
    TextView greeting, hartRate, bloodPressure;
    CardView healthSummaryCardView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // create database
        dataBaseHelper = new DataBaseHelper(getActivity());

        // create shared preferences manager
        prefsManager = new SharedPrefsManager(requireActivity());

        // create show message object
        showMessage = new ShowMessage();

        // TODO:define variables
        greeting = rootView.findViewById(R.id.greet_user_text);
        userImage = rootView.findViewById(R.id.user_avtar_img_home);
        medicationHomeRecyclerView = rootView.findViewById(R.id.medication_home_recycler_view);
        healthSummaryCardView = rootView.findViewById(R.id.summary_card_view);
        hartRate = rootView.findViewById(R.id.heart_rate_text);
        bloodPressure = rootView.findViewById(R.id.blood_presser_text);


        // get the global variable
        String sharedPreferencesName = prefsManager.getString("name", null);
        String sharedPreferencesUserEmail = prefsManager.getString("email", null);

        //TODO: populate the medicationList Array with medication list data
        getMedicationListData();
        //TODO: NOTE- set the recycler view adapter after getMedicationListData() method called
        MedicationListRecyclerviewAdapter medicationListRecyclerviewAdapter = new MedicationListRecyclerviewAdapter(requireContext(),
                medicationList, true, this);
        medicationHomeRecyclerView.setAdapter(medicationListRecyclerviewAdapter);
        medicationHomeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        //TODO: setting the user image
        if(sharedPreferencesUserEmail != null) {
            // get the data from the database
            Cursor cursor = dataBaseHelper.getUserData(sharedPreferencesUserEmail);
            if(cursor.getCount() != 0){
                while(cursor.moveToNext()){
                    byte[] imgAvatar = cursor.getBlob(4);
                    if(imgAvatar != null) {
                        userImage.setImageBitmap(DbBitmapUtility.getImage(imgAvatar));
                    }else{
                        userImage.setImageResource(R.drawable.avatarface);
                    }
                }
            }else{
                userImage.setImageResource(R.drawable.avatarface);
            }
        }
        else {
            userImage.setImageResource(R.drawable.avatarface);
        }

        // check if the global variable is not null
        if(sharedPreferencesName != null) {
            greeting.setText("Hello, " + sharedPreferencesName);
        }else{
            greeting.setText("Hello, User");
        }

        //TODO: get the all the health summary from the database
        Cursor cursor = dataBaseHelper.getHealthSummaryData();
        // check if the health data is already present in the database
        if (cursor.getCount() == 0) {
            // show manual health summary
            hartRate.setText(0 + " bpm");
            bloodPressure.setText(0 +"/" +0 + " mmHg");
        }else{
            while(cursor.moveToNext()){
                // get the last health data from the database
                if(cursor.isLast()){
                    hartRate.setText(cursor.getString(1) + " bpm");
                    bloodPressure.setText(cursor.getString(2) + " mmHg");
                }
            }
        }
        
        
        //TODO: on click listener for the health summary card view
        healthSummaryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHealthInputDialogBox();
            }
        });


        return rootView;
    }

    private void openHealthInputDialogBox() {

        // create a dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        // inflate the dialog box layout
        View inputDialog = getLayoutInflater().inflate(R.layout.health_input_dialog_box, null);
        // set the dialog box view
        builder.setView(inputDialog);
        // show the dialog box
        builder.setCancelable(false);
        // set the dialog box buttons for positive case
        builder.setPositiveButton("Save", (dialog, which) -> {
            // get the input fields
            EditText hartRateInput = inputDialog.findViewById(R.id.add_hartRate);
            EditText bloodPressureInput = inputDialog.findViewById(R.id.add_bloodPressure);
            MaterialAutoCompleteTextView autoCompleteTextView = inputDialog.findViewById(R.id.add_health_date_text);
            String selectedDay = autoCompleteTextView.getText().toString().toLowerCase();
            // check if the input fields are empty
            if(hartRateInput.getText().toString().isEmpty() || bloodPressureInput.getText().toString().isEmpty() || selectedDay.isEmpty()) {
                showMessage.show("Error", "Please fill all the fields", requireContext());
            }else{
                addHealthDataToDatabase(hartRateInput.getText().toString(), bloodPressureInput.getText().toString(), selectedDay);
//                Toast.makeText(requireContext(), "Day: " + selectedDay, Toast.LENGTH_SHORT).show();
            }
        });
        // set the dialog box buttons for negative case
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        // show the dialog box
        builder.show();
    }

    private void addHealthDataToDatabase(String hartRate, String bloodPressure, String selectedDay) {
        // insert the health summary to the database
        boolean isInserted = dataBaseHelper.insertHealthSummary(hartRate, bloodPressure, selectedDay);
        if(isInserted){
            Toast.makeText(requireContext(), "Health data added successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            showMessage.show("Error", "Failed to add health data!. please try again.", requireContext());
        }
    }


    // get the all medication list data
    public void getMedicationListData() {
        Cursor cursor = dataBaseHelper.getAllMedications();
        if(cursor.getCount() == 0) {
//            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
            medicationHomeRecyclerView.setVisibility(View.GONE);
        } else {
            while(cursor.moveToNext()) {
                medicationHomeRecyclerView.setVisibility(View.VISIBLE);
                int medicationId = cursor.getInt(0);
                String medicationName = cursor.getString(1);
                String medicationDosage = cursor.getString(3);
                String medicationTime = cursor.getString(5);
                String medicationFrequency = cursor.getString(6);
                MedicationListModel medicationListModel = new MedicationListModel(medicationId, medicationName, medicationDosage, medicationTime, medicationFrequency);
                medicationList.add(medicationListModel);
            }
        }
    }

    // on medication item click for the recycler view
    @Override
    public void onMedicationItemClick(int position) {
        AboutMedicationFragment aboutMedicationFragment = new AboutMedicationFragment();
        // Create a new Bundle to hold arguments
        Bundle args = new Bundle();
        // Put the medication id into the Bundle
        args.putInt("medicationId", medicationList.get(position).getMedicationId());
        // Set the Bundle as arguments for the fragment
        aboutMedicationFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, aboutMedicationFragment)
                .setReorderingAllowed(true)
                .addToBackStack("fromListFragment")
                .commit();
    }
}