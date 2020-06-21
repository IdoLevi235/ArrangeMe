
package com.example.arrangeme.menu.tasks;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

/**
 * View holder of the recycler view in tasks tab
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    //Elements of the recycler
    Button button;

    /**
     * Inner class of the view holder
     * @param v
     */
    public MyViewHolder(View v) {
        super(v);
        button=itemView.findViewById(R.id.button235);
    }

    @Override
    public void onClick(View v) {
    }
}