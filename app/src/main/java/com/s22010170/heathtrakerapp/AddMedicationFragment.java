package com.s22010170.heathtrakerapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMedicationFragment extends Fragment {
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    AlarmManager alarmManager;
    DataBaseHelper medicationDataBaseHelper;
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    AlarmReserve alarmReserve;
    PendingIntent pendingIntent;

    Button addMedicationButton, cancelMedicationButton, addMedicationImageButton;
    EditText medicationName, medicationDescription, medicationDosage, medicationRepeatTime;
    TextView medicationTime, imageOverviewText;
    ImageView medicationDeleteImage, medicationImagePreview;
    SwitchCompat medicationNotificationSwitch;
    LinearLayout medicationTimeSetupButton;
    RelativeLayout addMedicationArea;
    byte[] medicationImage;
    boolean isUserSetTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_medication, container, false);
        // create database
        medicationDataBaseHelper = new DataBaseHelper(requireActivity());
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(requireActivity());
        // create show message object
        showMessage = new ShowMessage();

        // define variables
        addMedicationButton = rootView.findViewById(R.id.add_medication_button);
        cancelMedicationButton = rootView.findViewById(R.id.add_cancel_medication_button);
        addMedicationImageButton = rootView.findViewById(R.id.add_medication_picture_button);
        medicationName = rootView.findViewById(R.id.add_medication_name);
        medicationDescription = rootView.findViewById(R.id.add_medication_description);
        medicationDosage = rootView.findViewById(R.id.add_medication_dosage);
        medicationRepeatTime = rootView.findViewById(R.id.add_medication_hours);
        medicationTime = rootView.findViewById(R.id.add_medication_frequency_time);
        medicationDeleteImage = rootView.findViewById(R.id.delete_time);
        medicationNotificationSwitch = rootView.findViewById(R.id.add_notification_switch);
        medicationTimeSetupButton = rootView.findViewById(R.id.add_medication_frequency_button);
        addMedicationArea = rootView.findViewById(R.id.add_medication_frequency_area);
        imageOverviewText = rootView.findViewById(R.id.add_medication_picture_overview);
        medicationImagePreview = rootView.findViewById(R.id.add_medication_picture_preview);

        // create notification channel
        createNotificationChannel();

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
        ActivityResultLauncher<Intent> mGetContentMedicationImage = registerForActivityResult(
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
                mGetContentMedicationImage.launch(gallery);
            }
        });

        //TODO: add medication button click listener
        addMedicationButton.setOnClickListener(new View.OnClickListener() {
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
                    InsertMedication(name, description, dosage, medicationImage, time, repeatTime);
                    InsertNotification(name, dosage, time, repeatTime);
                    goTOListFragment();
                }
                else{
                    InsertMedication(name, description, dosage, medicationImage, "", "");
                    goTOListFragment();
                }
            }
        });

        //TODO: cancel button click listener
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
        // Check if the Android version is Oreo or higher since Notification Channels were introduced in API level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Define the name of the notification channel
            CharSequence name = "medicationReminderApp";
            // Define the description for the notification channel
            String desc = "Channel for medication Alarm Manager";
            // Define the importance level of notifications from this channel here it's set to high
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

    // insert medication to the database
    public void InsertMedication(String name, String description, String dosage, byte[] image, String time, String repeatTime){
        boolean isInserted = medicationDataBaseHelper.insertMedication(name, description, dosage, image, time, repeatTime);
        if (isInserted){
            showMessage.show("Success", "Medication added successfully", getActivity());
        } else {
            showMessage.show("Error", "Failed to add medication", getActivity());
        }
    }
    // insert notification to the database
    private void InsertNotification(String medicationName, String dosage, String notificationTime, String notificationRepeatTime){
        boolean isInserted = medicationDataBaseHelper.insertNotification(medicationName, dosage, notificationTime, notificationRepeatTime);
        if (!isInserted){
            showMessage.show("Error", "Failed to add notification", getActivity());
        }
    }

    // go to list fragment
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