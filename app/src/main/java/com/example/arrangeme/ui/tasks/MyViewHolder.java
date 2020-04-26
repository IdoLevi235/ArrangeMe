
package com.example.arrangeme.ui.tasks;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    //Elements of the recycler
    Button button;
    public MyViewHolder(View v) {
        super(v);
        button=itemView.findViewById(R.id.button235);
    }
}