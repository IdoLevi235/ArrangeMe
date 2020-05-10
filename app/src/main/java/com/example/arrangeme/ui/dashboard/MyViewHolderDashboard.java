package com.example.arrangeme.ui.dashboard;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

public class MyViewHolderDashboard extends RecyclerView.ViewHolder{
    Button anchorOrTask;
    Button button;
    TextView timeText;

    public MyViewHolderDashboard(View v) {
        super(v);
        button=itemView.findViewById(R.id.buttonsche);
        timeText = itemView.findViewById(R.id.timeText);
        anchorOrTask = itemView.findViewById(R.id.anchor_task);
    }

}
