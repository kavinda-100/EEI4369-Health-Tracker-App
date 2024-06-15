package com.s22010170.heathtrakerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s22010170.heathtrakerapp.R;
import com.s22010170.heathtrakerapp.dataClass.MedicationListModel;
import com.s22010170.heathtrakerapp.interfaces.MedicationListRecyclerViewInterface;

import java.util.ArrayList;

public class MedicationListRecyclerviewAdapter extends RecyclerView.Adapter<MedicationListRecyclerviewAdapter.ViewHolder> {
    private final MedicationListRecyclerViewInterface medicationListRecyclerViewInterface;
    Context context;
    ArrayList<MedicationListModel> medicationList;
    boolean isThisListFragment;

    public MedicationListRecyclerviewAdapter(Context context, ArrayList<MedicationListModel> medicationList,
                                             boolean isThisListFragment,
                                             MedicationListRecyclerViewInterface medicationListRecyclerViewInterface) {
        this.context = context;
        this.medicationList = medicationList;
        this.isThisListFragment = isThisListFragment;
        this.medicationListRecyclerViewInterface = medicationListRecyclerViewInterface;
    }

    @NonNull
    @Override
    public MedicationListRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.medication_list_row, parent, false);
        return new MedicationListRecyclerviewAdapter.ViewHolder(view, medicationListRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationListRecyclerviewAdapter.ViewHolder holder, int position) {
        holder.medicationName.setText(medicationList.get(position).getMedicationName());
        holder.medicationDosage.setText(medicationList.get(position).getMedicationDosage());
        holder.medicationTime.setText(medicationList.get(position).getMedicationTime());
        holder.medicationFrequency.setText(medicationList.get(position).getMedicationFrequency());

    }

    @Override
    public int getItemCount() {
        int size;
        if(isThisListFragment){
            size = medicationList.size();
        }
        else{
            size = 6;
        }
        return size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicationName, medicationTime, medicationDosage, medicationFrequency;
        public ViewHolder(@NonNull View itemView, MedicationListRecyclerViewInterface medicationListRecyclerViewInterface) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medication_name_text_list);
            medicationDosage = itemView.findViewById(R.id.medication_dosage_text_list);
            medicationTime = itemView.findViewById(R.id.medication_time_text_list);
            medicationFrequency = itemView.findViewById(R.id.medication_frequency_text_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(medicationListRecyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            medicationListRecyclerViewInterface.onMedicationItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
