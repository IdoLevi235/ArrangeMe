package com.example.arrangeme.menu.calendar.month;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

class MyViewHolder extends RecyclerView.ViewHolder {
    //Elements of the recycler
    Button anchorOrTask;
    Button button;
    TextView timeText;

    public MyViewHolder(View v) {
        super(v);
         button=itemView.findViewById(R.id.buttonmonth);
         timeText = itemView.findViewById(R.id.timeTextMonth);
         anchorOrTask = itemView.findViewById(R.id.anchor_task_month);
    }


}
