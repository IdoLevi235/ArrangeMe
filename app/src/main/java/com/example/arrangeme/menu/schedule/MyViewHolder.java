package com.example.arrangeme.menu.schedule;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

/**
 * Schdule's view holder
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    //Elements of the recycler
    public Button anchorOrTask;
    public Button button;
    public TextView timeText;

    public MyViewHolder(View v) {
        super(v);
        button=itemView.findViewById(R.id.buttonsche);
        timeText = itemView.findViewById(R.id.timeText);
        anchorOrTask = itemView.findViewById(R.id.anchor_task);
    }
}
