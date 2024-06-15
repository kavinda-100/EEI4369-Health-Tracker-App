package com.s22010170.heathtrakerapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.interfaces.NotificationRecyclerViewInterface;
import com.s22010170.heathtrakerapp.dataClass.ResentNotificationModel;
import com.s22010170.heathtrakerapp.adapters.ResentNotificationRecyclerViewAdapter;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationFragment extends Fragment implements NotificationRecyclerViewInterface {
    ArrayList<ResentNotificationModel> notificationList = new ArrayList<ResentNotificationModel>();
    ResentNotificationRecyclerViewAdapter resentNotificationRecyclerViewAdapter;
    RecyclerView notificationListRecyclerView;
    DataBaseHelper dataBaseHelper;
    ShowMessage showMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        dataBaseHelper = new DataBaseHelper(requireContext());
        showMessage = new ShowMessage();

        notificationListRecyclerView = rootView.findViewById(R.id.resent_notification_list_recycler_view);


        //TODO: populate the notificationList Array with resent notification data
        addAllResentNotificationToTheList();
        //TODO: NOTE- set the recycler view adapter after addAllResentNotificationToTheList() method called
        resentNotificationRecyclerViewAdapter = new ResentNotificationRecyclerViewAdapter(requireContext(), notificationList, this);
        notificationListRecyclerView.setAdapter(resentNotificationRecyclerViewAdapter);
        notificationListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return rootView;
    }

    // get the current time
    private String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void addAllResentNotificationToTheList(){
        String currentTime = getCurrentTime();
        System.out.println("Current Time: " + currentTime);
        Cursor cursor = dataBaseHelper.getAllResentNotifications(currentTime);
//        Cursor cursor = dataBaseHelper.getAllNotifications();
        if(cursor.getCount() == 0){
            notificationListRecyclerView.setVisibility(View.GONE);
        }
        else{
            while (cursor.moveToNext()){
                notificationListRecyclerView.setVisibility(View.VISIBLE);
                int medicationID = cursor.getInt(0);
                String medicationName = cursor.getString(1);
                String medicationDosage = cursor.getString(2);
                String medicationTime = cursor.getString(3);
                String medicationFrequency = cursor.getString(4);
                ResentNotificationModel resentNotificationModel = new ResentNotificationModel(medicationID, medicationName, medicationDosage, medicationTime, medicationFrequency);
                notificationList.add(resentNotificationModel);
            }
        }
    }

    @Override
    public void onNotificationLongClick(int position) {
        boolean isDeleted = dataBaseHelper.deleteNotification(notificationList.get(position).getMedicationId());
        if(!isDeleted){
            showMessage.show("Error", "Failed to delete the notification", requireContext());
        }else{
            notificationList.remove(position);
            resentNotificationRecyclerViewAdapter.notifyItemRemoved(position);
        }
    }
}