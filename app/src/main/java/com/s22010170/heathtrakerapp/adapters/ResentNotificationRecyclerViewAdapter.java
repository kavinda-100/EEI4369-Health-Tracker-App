package com.s22010170.heathtrakerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s22010170.heathtrakerapp.R;
import com.s22010170.heathtrakerapp.interfaces.NotificationRecyclerViewInterface;
import com.s22010170.heathtrakerapp.dataClass.ResentNotificationModel;

import java.util.ArrayList;

public class ResentNotificationRecyclerViewAdapter extends RecyclerView.Adapter<ResentNotificationRecyclerViewAdapter.ViewHolder>{
    private final NotificationRecyclerViewInterface notificationRecyclerViewInterface;
    Context context;
    ArrayList<ResentNotificationModel> resentNotificationList;

    public ResentNotificationRecyclerViewAdapter(Context context, ArrayList<ResentNotificationModel> resentNotificationList, NotificationRecyclerViewInterface notificationRecyclerViewInterface) {
        this.context = context;
        this.resentNotificationList = resentNotificationList;
        this.notificationRecyclerViewInterface = notificationRecyclerViewInterface;
    }

    @NonNull
    @Override
    public ResentNotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.notification_row, parent, false);
        return new ResentNotificationRecyclerViewAdapter.ViewHolder(view, notificationRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ResentNotificationRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.medicationName.setText(resentNotificationList.get(position).getMedicationName());
        holder.medicationDosage.setText(resentNotificationList.get(position).getMedicationDosage());
        holder.medicationTime.setText(resentNotificationList.get(position).getMedicationTime());
        holder.medicationFrequency.setText(resentNotificationList.get(position).getMedicationFrequency());
    }

    @Override
    public int getItemCount() {
        return resentNotificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicationName, medicationTime, medicationDosage, medicationFrequency;
        public ViewHolder(@NonNull View itemView, NotificationRecyclerViewInterface notificationRecyclerViewInterface) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medication_name_text_notification);
            medicationDosage = itemView.findViewById(R.id.medication_dosage_text_notification);
            medicationTime = itemView.findViewById(R.id.medication_time_text_notification);
            medicationFrequency = itemView.findViewById(R.id.medication_date_text_notification);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(notificationRecyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            notificationRecyclerViewInterface.onNotificationLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
