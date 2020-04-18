package com.example.arrangeme.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.MainAdapter;
import com.example.arrangeme.AddTasks.MainModel;
import com.example.arrangeme.R;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        tasksViewModel.getText().observe(getViewLifecycleOwner(), s -> new Observer<String>() {
            @Override

            public void onChanged(@Nullable String s) {
                //Here we need to change the text view of the name and more staff to change
            }
        });
        return root;
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
