package com.s22010170.heathtrakerapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.DbBitmapUtility;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

import java.io.InputStream;

public class UserFragment extends Fragment {
    DataBaseHelper authDataBaseHelper;
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    EditText email, username, password, confirmPassword;
    Button updateButton, deleteButton, logoutButton;
    ImageView profileImage, backgroundImage;
    TextView greetingText, emailText;
    CheckBox showFingerPrint;
    SwitchCompat themeModeSwitch;
    String oldPassword, newPassword;
    boolean isNightMode;
    byte[] imgAvatar, newImgAvatar;
    byte[] imgBackground, newImgBackground;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        // create database
        authDataBaseHelper = new DataBaseHelper(requireActivity());

        // create shared preferences manager
        prefsManager = new SharedPrefsManager(requireActivity());

        // create show message object
        showMessage = new ShowMessage();

        // TODO:define variables
        email = rootView.findViewById(R.id.update_user_email);
        username = rootView.findViewById(R.id.update_user_name);
        password = rootView.findViewById(R.id.update_user_password);
        confirmPassword = rootView.findViewById(R.id.update_user_confirm_password);
        updateButton = rootView.findViewById(R.id.update_button);
        deleteButton = rootView.findViewById(R.id.delete_button);
        logoutButton = rootView.findViewById(R.id.logout_button);
        profileImage = rootView.findViewById(R.id.user_avtar_img);
        backgroundImage = rootView.findViewById(R.id.user_background_img);
        greetingText = rootView.findViewById(R.id.user_greet_text);
        emailText = rootView.findViewById(R.id.user_email_text);
        showFingerPrint = rootView.findViewById(R.id.fingerprint_enable_checkbox);
        themeModeSwitch = rootView.findViewById(R.id.switch_theme);

        // TODO: get the shared preferences values
        String sharedPreferencesEmail = prefsManager.getString("email", "");
        showFingerPrint.setChecked(prefsManager.getBoolean("isFingerPrintAllowed", false));
        isNightMode = prefsManager.getBoolean("nightMode", true);

        //TODO: set the theme mode
        if(isNightMode) {
            themeModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        themeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                // update the theme mode in shared preferences
                prefsManager.saveBoolean("nightMode", isChecked);
            }
        });

        // TODO: get the data from the database and display it on the screen
        if(!sharedPreferencesEmail.isEmpty()){
            // get the data from the database
            Cursor cursor = authDataBaseHelper.getUserData(sharedPreferencesEmail);
            if(cursor.getCount() != 0){
                //display the data on the screen
                while (cursor.moveToNext()) {
                    username.setText(cursor.getString(1));
                    email.setText(cursor.getString(2));
                    oldPassword = cursor.getString(3);
                    imgAvatar = cursor.getBlob(4);
                    imgBackground = cursor.getBlob(5);
                    greetingText.setText("Hello, " + cursor.getString(1));
                    emailText.setText(cursor.getString(2));
                }
                // check if the avatar image is not null
                if(imgAvatar != null){
                    profileImage.setImageBitmap(DbBitmapUtility.getImage(imgAvatar));
                }else{
                    profileImage.setImageResource(R.drawable.avatarface);
                }
                // check if the background image is not null
                if(imgBackground != null){
                    backgroundImage.setImageBitmap(DbBitmapUtility.getImage(imgBackground));
                }else {
                    backgroundImage.setImageResource(R.drawable.avatarbg);
                }

            }else{
                showMessage.show("Error", "Unable to get the data from database.", getActivity());
            }
        }
        else {
            showMessage.show("Error", "Global Variable Email is empty.", getActivity());
        }

        // TODO: Working with images
        // avatar image result handler
        ActivityResultLauncher<Intent> mGetContentAvatar = registerForActivityResult(
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
                                        Bitmap BitmapAvatar = BitmapFactory.decodeStream(imageStream);
                                        // set the image to the image view
                                        profileImage.setImageBitmap(BitmapAvatar);
                                        // convert the image to byte array to store in the database
                                        newImgAvatar = DbBitmapUtility.getBytes(BitmapAvatar);
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

        // background image result handler
        ActivityResultLauncher<Intent> mGetContentBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // Handle the Intent for background image
                            if(data != null) {
                                try {
                                    Uri imageUri = data.getData();
                                    if (imageUri != null) {
                                        InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                                        // convert the image to bitmap
                                        Bitmap BitmapBackground = BitmapFactory.decodeStream(imageStream);
                                        // set the image to the image view
                                        backgroundImage.setImageBitmap(BitmapBackground);
                                        // convert the image to byte array to store in the database
                                        newImgBackground = DbBitmapUtility.getBytes(BitmapBackground);
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
        // get image from avatar
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                mGetContentAvatar.launch(gallery);
            }
        });
        // get image from background
        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                mGetContentBackground.launch(gallery);
            }
        });

        // TODO: enable fingerprint or disable fingerprint
        showFingerPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update the fingerprint status in shared preferences
                prefsManager.saveBoolean("isFingerPrintAllowed", isChecked);
            }
        });



        // TODO: update user details
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if password not provided then use the old password
                if(password.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()) {
                    newPassword = oldPassword;
                    updateDataWithImages(newImgAvatar, newImgBackground, sharedPreferencesEmail, email.getText().toString(), username.getText().toString(), newPassword, imgAvatar, imgBackground);
                }
                else{
                    // if password provided then use the new password
                    newPassword = password.getText().toString();
                    // check if the password is correct and password and confirm password are same
                    if(newPassword.equals(confirmPassword.getText().toString()) && newPassword.length() < 6){
                        updateDataWithImages(newImgAvatar, newImgBackground, sharedPreferencesEmail, email.getText().toString(), username.getText().toString(), newPassword, imgAvatar, imgBackground);
                    }else{
                        showMessage.show("Error", "Password and confirm password should be same and password should be at least 6 characters long.", getActivity());
                    }
                }
                return;

            }
        });

        // TODO:delete user account
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show confirm dialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete this account?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // delete the user account
                                boolean isDeleted = authDataBaseHelper.deleteAccount(sharedPreferencesEmail);
                                boolean isAllMedicationDeleted = authDataBaseHelper.deleteAllMedications();
                                boolean isAllNotificationDeleted = authDataBaseHelper.deleteAllNotifications();
                                if(isDeleted && isAllMedicationDeleted && isAllNotificationDeleted){
                                    showMessage.show("Success", "User account deleted successfully.", getActivity());
                                    // clear the shared preferences
                                    prefsManager.clearPreferences();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    showMessage.show("Error", "Unable to delete the user account.", getActivity());
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Handle the negative button action
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        // TODO:logout user
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show confirm dialog

                // update the login status
                boolean isUpdateLoginStatus = authDataBaseHelper.updateLoginStatus(sharedPreferencesEmail, "false");
                if(isUpdateLoginStatus){
                    showMessage.show("Success", "User logged out successfully.", getActivity());
                    // clear the shared preferences
                    String[] keysToRemove = {"email", "name"};
                    prefsManager.clearFields(keysToRemove);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }else{
                    showMessage.show("Error", "Unable to logout the user.", getActivity());
                }
            }
        });

        return rootView;
    }

    // TODO: check whether the user is selected the image or not
    public void updateDataWithImages(byte[] newImgAvatar, byte[] newImgBackground, String oldEmail,
                                     String email, String username, String password, byte[] imgAvatar, byte[] imgBackground){
        if(newImgAvatar != null && newImgBackground != null){
            updateUserData(oldEmail, email, username, password, newImgAvatar, newImgBackground);
        }else if(newImgAvatar != null){
            updateUserData(oldEmail, email, username, password, newImgAvatar, imgBackground);
        }else if(newImgBackground != null){
            updateUserData(oldEmail, email, username, password, imgAvatar, newImgBackground);
        }else{
            updateUserData(oldEmail, email, username, password, imgAvatar, imgBackground);
        }
    }
    // TODO:update user data method
    public void updateUserData(String oldEmail, String email, String username, String password, byte[] imgAvatar, byte[] imgBackground){
        // update the user data
        boolean isUpdated = authDataBaseHelper.updateUserData(oldEmail, email, username, password, imgAvatar, imgBackground);
        if(isUpdated){
            showMessage.show("Success", "User data updated successfully.", getActivity());
            prefsManager.updateString("email", email);
            prefsManager.updateString("name", username);
        }else{
            showMessage.show("Error", "Unable to update the user data.", getActivity());
        }
    }

}