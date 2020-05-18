package com.example.arrangeme.ChooseTasks;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arrangeme.R;

/**
 * View holder of this recycler
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    //Elements of the recycler
    Button button;
    public MyViewHolder(View v) {
        super(v);
        button=itemView.findViewById(R.id.button235);
    }

    @Override
    public void onClick(View v) {
    }
}