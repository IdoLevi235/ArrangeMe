package com.example.arrangeme.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.MainModel;
import com.example.arrangeme.R;
import com.example.arrangeme.tasks;

import java.util.ArrayList;
import java.util.List;

public class AdapterScheduleTab extends RecyclerView.Adapter<AdapterScheduleTab.MyViewHolder>  {
    private ArrayList<MainModelSchedule> mainModels;
    private Context context;

    public AdapterScheduleTab(Context context, ArrayList<MainModelSchedule> mainModels) {
        this.context=context;
        this.mainModels=mainModels;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Elements of the recycler
         Button button;
        public MyViewHolder(View v) {
            super(v);
            button=itemView.findViewById(R.id.buttonsche);
        }
    }

    @NonNull
    @Override
    public AdapterScheduleTab.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule,parent,false);
        //
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.button.setText(mainModels.get(position).getName());
        holder.button.setBackgroundResource(mainModels.get(position).getIcon());

    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }
}
