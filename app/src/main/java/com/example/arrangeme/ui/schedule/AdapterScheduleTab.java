package com.example.arrangeme.ui.schedule;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

import java.util.List;

public class AdapterScheduleTab extends RecyclerView.Adapter<AdapterScheduleTab.MyViewHolder>  {
    private List<String> tasks;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }


    public AdapterScheduleTab(List<String> myDataset) {
        this.tasks = myDataset;
    }

    @NonNull
    @Override
    public AdapterScheduleTab.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks, parent, false);
        //
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
