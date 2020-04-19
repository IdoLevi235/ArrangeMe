package com.example.arrangeme.AddTasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<MainModel> mainModels;
    Context context;

public MainAdapter(Context context, ArrayList<MainModel> mainModels){
    this.context=context;
    this.mainModels=mainModels;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks,parent,false);
    return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.button.setImageResource(mainModels.get(position).getCatLogo());
        //holder.textView.setText(mainModels.get(position).getCatName());
        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,mainModels.get(position).getCatLogo(),0,0);
        holder.button.setBackgroundResource(mainModels.get(position).getCatBackground());
        holder.button.setLayoutParams (new LinearLayout.LayoutParams(210, 225));
        holder.button.setText(mainModels.get(position).getCatName());
        holder.button.setTextSize(12);
        holder.button.setTextColor(Color.parseColor("#ffffff"));
        holder.button.setTypeface(Typeface.create("montserrat", Typeface.NORMAL));
        holder.button.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    Button button;
   // TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.button);
            //textView=itemView.findViewById(R.id.text_view);
        }
    }
}
