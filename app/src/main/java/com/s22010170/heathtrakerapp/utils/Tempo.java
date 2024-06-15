package com.s22010170.heathtrakerapp.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;

public class Tempo {

//    TODO: 1. This is for HomeFragment. It is used to get the data from the database and display it on the screen. It get extra data from the intent (from sign-in activity) and use it to get the data from the database.
//    Intent intent = getActivity().getIntent();
//    // check if the intent is not null
//        if(intent != null){
//        String emailText = intent.getStringExtra("email");
//        // get the data from the database
//        Cursor cursor = authDataBaseHelper.getUserData(emailText);
//        if(cursor.getCount() != 0){
//            //showMessage.show("success", "able to get the data from database.", getActivity());
//            while (cursor.moveToNext()) {
//                username.setText(cursor.getString(1));
//                email.setText(cursor.getString(2));
//                String isLoggedIn = cursor.getString(5);
//                if(isLoggedIn.equals("true")){
//                    showMessage.show("success", "User is logged in." + isLoggedIn, getActivity());
//                }
//            }
//        }else{
//            showMessage.show("Error", "Unable to get the data from database.", getActivity());
//        }
//    }

//    TODO: 2. This is for ListFragment. It is used to send the email from HomeFragment and display the email on the screen(ListFragment). It get the email from the bundle and display it on the screen.
//    goToListButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Bundle bundle = new Bundle();
//            bundle.putString("email", email.getText().toString());
//            ListFragment listFragment = new ListFragment();
//            listFragment.setArguments(bundle);
//            getParentFragmentManager().beginTransaction().replace(R.id.home_container, listFragment).commit();
//        }
//    });

//    TODO: 3. This is for ListFragment. It is used to get the email from the bundle and display it on the screen.
//    Bundle bundle = this.getArguments();
//        if(bundle != null){
//        String emailText = bundle.getString("email");
//        email.setText(emailText);
//    }

//    TODO:picture from gallery
//Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);

    //TODO: for permission  to access the gallery (bit work not completely done)
//    // check the run time permission
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
//        // permission already granted
////                    showMessage.show("Success", "Permission already granted", MainActivity.this);
//        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//        startActivity(intent);
//    } else {
//        // permission not granted, request for permission
//        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_CODE);
//    }
}
