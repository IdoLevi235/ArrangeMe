package com.example.arrangeme.AddTasks;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.R;
import java.util.ArrayList;

/**
 * This is the main adapter of the recycler view of AddTasks page
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String TAG = "MainAdapter";
    private ArrayList<MainModel> mainModels;
    private Context context;
    private int row_index = -1;
    private TaskCategory currentCategory;

    /**
     * Constructor
     * @param context
     * @param mainModels
     */
    public MainAdapter(Context context, ArrayList<MainModel> mainModels) {
        this.context = context;
        this.mainModels = mainModels;
    }

    /**
     * Inflates row_tasks to the view holder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks, parent, false);
        return new ViewHolder(view);
    }

    /**
     * setting the holder's components attributes
     * @param holder
     * @param position
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.button.setImageResource(mainModels.get(position).getCatLogo());
        //holder.textView.setText(mainModels.get(position).getCatName());
        Log.d(TAG, "onBindViewHolder: " + mainModels.get(position).getCatLogo());
        holder.button.setCompoundDrawablesWithIntrinsicBounds(0, mainModels.get(position).getCatLogo(), 0, 0);
        holder.button.setBackgroundResource(mainModels.get(position).getCatBackground());
        holder.button.setLayoutParams(new LinearLayout.LayoutParams(210, 225));
        holder.button.setText(mainModels.get(position).getCatName());
        holder.button.setTextSize(12);
        holder.button.setTextColor(ContextCompat.getColor
                (context, mainModels.get(position).getCatColor()));
        holder.button.setTypeface(Typeface.create("montserrat", Typeface.NORMAL));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                currentCategory = TaskCategory.fromInt(position);
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.button.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            holder.button.setTextColor(Color.parseColor("#FFFFFF"));
            holder.button.setBackgroundResource(mainModels.get(position).getCatColorFull());
            holder.button.setPadding(0, 25, 0, 25);

        } else {
            holder.button.setCompoundDrawableTintList
                    (ColorStateList.valueOf(ContextCompat.getColor
                            (context, mainModels.get(position).getCatColor())));
        }

    }


    /**
     * count how many items in the recycler
     * @return int
     */
    @Override
    public int getItemCount() {
        return mainModels.size();
    }


    /**
     * get current category
     * @return TaskCategory
     */
    public TaskCategory getCurrentCategory() {
        return currentCategory;
    }


    /**
     * Inner class extends ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        Button button;

        /**
         * Constructor of inner class
         * @param itemView
         */
        // TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.buttonTasksTab);
            //textView=itemView.findViewById(R.id.text_view);
        }
    }


}
