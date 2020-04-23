package com.example.arrangeme.ui.schedule;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

class MyViewHolder extends RecyclerView.ViewHolder {
    //Elements of the recycler
    Button button;
    public MyViewHolder(View v) {
        super(v);
        button=itemView.findViewById(R.id.buttonsche);
    }
}
