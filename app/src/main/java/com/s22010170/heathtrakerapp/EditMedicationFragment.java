package com.s22010170.heathtrakerapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.s22010170.heathtrakerapp.utils.AlarmReserve;
import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.DbBitmapUtility;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

import java.io.InputStream;
import java.util.Calendar;

public class EditMedicationFragment extends Fragment {
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    DataBaseHelper medicationDataBaseHelper;
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    EditText medicationName, medicationDescription, medicationDosage, medicationRepeatTime;
    Button editMedicationButton, cancelMedicationButton, addMedicationImageButton;
    SwitchCompat medicationNotificationSwitch;
    ImageView medicationDeleteImage, medicationImagePreview;
    TextView medicationTime, imageOverviewText;
    RelativeLayout addMedicationArea;
    LinearLayout medicationTimeSetupButton;
    byte[] medicationImage;
    boolean isUserSetTime;
    int medicationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_medication, container, false);

        // create database
        medicationDataBaseHelper = new DataBaseHelper(requireActivity());
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(requireActivity());
        // create show message object
        showMessage = new ShowMessage();

        // Create a notification channel for the app
        createNotificationChannel();

        medicationName = rootView.findViewById(R.id.edit_medication_name);
        medicationDescription = rootView.findViewById(R.id.edit_medication_description);
        medicationDosage = rootView.findViewById(R.id.edit_medication_dosage);
        addMedicationImageButton = rootView.findViewById(R.id.edit_medication_picture_button);
        medicationImagePreview = rootView.findViewById(R.id.edit_medication_picture_preview);
        imageOverviewText = rootView.findViewById(R.id.edit_medication_picture_overview);
        medicationNotificationSwitch = rootView.findViewById(R.id.edit_notification_switch);
        addMedicationArea = rootView.findViewById(R.id.edit_medication_frequency_area);
        medicationTimeSetupButton = rootView.findViewById(R.id.edit_medication_frequency_button);
        medicationTime = rootView.findViewById(R.id.edit_medication_frequency_time);
        medicationDeleteImage = rootView.findViewById(R.id.edit_delete_time);
        medicationRepeatTime = rootView.findViewById(R.id.edit_medication_hours);
        editMedicationButton = rootView.findViewById(R.id.edit_medication_button);
        cancelMedicationButton = rootView.findViewById(R.id.edit_cancel_medication_button);

        // Get the medication id from the arguments
        Bundle args = getArguments();
        medicationId = -1; // Default value

        if (args != null) medicationId = args.getInt("medicationId", -1);
        if(medicationId != -1){
            getMedicationDataByID(medicationId);
        } else {
            showMessage.show("Error", "No medication found please try again!.", requireContext());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // go back to medication list
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ListFragment()).commit();
                }
            }, 2000);
        }

        //TODO: add switch listener
        medicationNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                addMedicationArea.setVisibility(View.VISIBLE);
            } else {
                addMedicationArea.setVisibility(View.GONE);
            }
        });
        //TODO: set time click listener
        medicationTimeSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
                isUserSetTime = true;
            }
        });
        //TODO: delete time click listener
        medicationDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
                isUserSetTime = false;
            }
        });
        //TODO: handle medication image
        ActivityResultLauncher<Intent> mGetContentMedicationImageInEditMedication = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // Handle the Intent for avatar image
                            if(data != null){
                                try {
                                    Uri imageUri = data.getData();
                                    if (imageUri != null) {
                                        InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                                        // convert the image to bitmap
                                        Bitmap BitmapMedicationImage = BitmapFactory.decodeStream(imageStream);
                                        // set the image to the image view
                                        medicationImagePreview.setImageBitmap(BitmapMedicationImage);
                                        medicationImagePreview.setVisibility(View.VISIBLE);
                                        imageOverviewText.setVisibility(View.VISIBLE);
                                        // convert the image to byte array to store in the database
                                        medicationImage = DbBitmapUtility.getBytes(BitmapMedicationImage);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showMessage.show("Error", "Some thing went wrong!. please try again!", getActivity());
                                }
                            }else{
                                showMessage.show("Error", "Unable to get the image from the gallery.", getActivity());
                            }
                        }
                    }
                });
        //TODO: add medication image click listener
        addMedicationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                mGetContentMedicationImageInEditMedication.launch(gallery);
            }
        });

        //TODO: edit medication button click listener
        editMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values from the edit text
                String name = medicationName.getText().toString();
                String description = medicationDescription.getText().toString();
                String dosage = medicationDosage.getText().toString();
                String time = medicationTime.getText().toString();
                String repeatTime = medicationRepeatTime.getText().toString();
                // check if the fields are empty
                if (name.isEmpty() || description.isEmpty() || dosage.isEmpty()){
                    showMessage.show("Error", "Please fill all the fields", getActivity());
                    return;
                }
                if (medicationNotificationSwitch.isChecked() && isUserSetTime){
                    setAlarm();
                    updateMedication(medicationId, name, description, dosage, medicationImage, time, repeatTime);
                    updateNotification(medicationId, name, dosage, time, repeatTime);
                    goTOListFragment();
                }
                else{
                    updateMedication(medicationId, name, description, dosage, medicationImage, "", "");
                    goTOListFragment();
                }
            }
        });


        // cancel medication
        cancelMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to medication list
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ListFragment()).commit();
            }
        });

        return rootView;
    }

    private void cancelAlarm() {
        // Create an Intent that identifies the AlarmReserve BroadcastReceiver
        Intent intent = new Intent(requireActivity(), AlarmReserve.class);
        // Create a PendingIntent that will perform a broadcast, using a unique request code (0) and immutable flag
        pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Check if the alarmManager is null, if so, get the system's Alarm Service
        if (alarmManager == null){
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }
        // Cancel any alarms with a matching Intent defined by the pendingIntent
        alarmManager.cancel(pendingIntent);
        // Show a toast message to the user indicating the alarm has been canceled
        Toast.makeText(requireActivity(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        // Obtain the AlarmManager system service to schedule alarms
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        // Create an Intent that identifies the target broadcast receiver (AlarmReserve.class)
        Intent intent = new Intent(getActivity(), AlarmReserve.class);
        // Create a PendingIntent that will perform a broadcast, marked as immutable for security best practices
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Parse the repeat time from the medicationRepeatTime EditText, convert it to milliseconds
        String time = medicationRepeatTime.getText().toString();
        long sixHoursInMillis = (long) Integer.parseInt(time) * 60 * 60 * 1000;
        // Schedule a repeating alarm that wakes up the device to fire the pendingIntent at the specified interval
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sixHoursInMillis, pendingIntent);
        // Display a toast message to inform the user that the alarm has been set
        Toast.makeText(requireActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
    }


    private void setTime() {
        // Initialize the MaterialTimePicker.Builder
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // Set the time format to 12-hour clock
                .setHour(12) // Set the initial hour to 12
                .setMinute(0) // Set the initial minute to 0
                .setTitleText("Select Alarm Time") // Set the title of the picker
                .build(); // Build the MaterialTimePicker instance
        // Display the time picker dialog
        timePicker.show(requireActivity().getSupportFragmentManager(), "medicationReminder");
        // Set a listener for the positive button click event
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the selected hour is greater than 12 to format the time string as PM
                if (timePicker.getHour() > 12){
                    // Format and set the time string to medicationTime TextView as PM
                    String time = String.format("%02d",(timePicker.getHour()-12)) +":"+ String.format("%02d", timePicker.getMinute())+"PM";
                    medicationTime.setText(time);
                } else {
                    // Format and set the time string to medicationTime TextView as AM
                    medicationTime.setText(timePicker.getHour()+":" + timePicker.getMinute()+ "AM");
                }
                // Initialize the calendar instance to the current time
                calendar = Calendar.getInstance();
                // Set the HOUR_OF_DAY of the calendar to the selected hour
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                // Set the MINUTE of the calendar to the selected minute
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                // Set the SECOND of the calendar to 0
                calendar.set(Calendar.SECOND, 0);
                // Set the MILLISECOND of the calendar to 0
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    // create notification channel
    private void createNotificationChannel(){
        // Check if the Android version is Oreo or higher since Notification Channels were introduced in API level 26 (Android O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Define the name of the notification channel
            CharSequence name = "medicationReminderApp";
            // Define the description for the notification channel
            String desc = "Channel for medication Alarm Manager";
            // Define the importance level of notifications from this channel; here it's set to high
            int imp = NotificationManager.IMPORTANCE_HIGH;
            // Create a NotificationChannel object with a unique ID, name, and importance level
            NotificationChannel channel = new NotificationChannel("medicationReminder", name, imp);
            // Set the description of the notification channel
            channel.setDescription(desc);
            // Get the NotificationManager from the system services
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            // Register the notification channel with the system
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void getMedicationDataByID(int medicationID){
        Cursor medicationData = medicationDataBaseHelper.getMedicationById(medicationID);
        if (medicationData.getCount() == 0){
            showMessage.show("Error", "No medication found", requireContext());
        } else {
            while (medicationData.moveToNext()){
                medicationName.setText(medicationData.getString(1));
                medicationDescription.setText(medicationData.getString(2));
                medicationDosage.setText(medicationData.getString(3));
                medicationImage = medicationData.getBlob(4);
                medicationTime.setText(medicationData.getString(5));
                medicationRepeatTime.setText(medicationData.getString(6));
            }
            if (medicationImage != null){
                Bitmap bitmap = DbBitmapUtility.getImage(medicationImage);
                medicationImagePreview.setImageBitmap(bitmap);
                medicationImagePreview.setVisibility(View.VISIBLE);
                imageOverviewText.setVisibility(View.VISIBLE);
            }
            if(medicationTime.getText().length() == 0 && medicationRepeatTime.getText().length() == 0){
                medicationNotificationSwitch.setChecked(false);
                addMedicationArea.setVisibility(View.GONE);
            } else {
                medicationNotificationSwitch.setChecked(true);
                addMedicationArea.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateMedication(int medicationID, String name, String description, String dosage, byte[] image, String time, String repeatTime){
        boolean isUpdated = medicationDataBaseHelper.updateMedication(medicationID, name, description, dosage, image, time, repeatTime);
        if (isUpdated){
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ListFragment()).commit();
            showMessage.show("Medication Updated", "The medication has been updated successfully", requireContext());
        } else {
            showMessage.show("Error", "Something went wrong! medication not updated. please try again!.", requireContext());
        }
    }

    private void updateNotification(int id, String medicationName, String dosage, String notificationTime, String notificationRepeatTime){
        boolean isUpdated = medicationDataBaseHelper.updateNotification(id, medicationName, dosage, notificationTime, notificationRepeatTime);
        if (!isUpdated){
            showMessage.show("Error", "Something went wrong! notification not updated. please try again!.", requireContext());
        }
    }

    private  void goTOListFragment(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // go back to medication list
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ListFragment()).commit();
            }
        }, 2000);
    }
}