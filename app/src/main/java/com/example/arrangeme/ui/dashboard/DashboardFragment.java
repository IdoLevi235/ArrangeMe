package com.example.arrangeme.ui.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.ChooseTasks.ChooseTasks;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private Button calenderBtn;
    private Button addTaskBtn;
    private Button chooseTasksBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Here we need to change the text view of the name and more staff to change
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calenderBtn = view.findViewById(R.id.calenderBtn);
        addTaskBtn = view.findViewById(R.id.addTaskBtn);
        chooseTasksBtn = view.findViewById(R.id.chooseTasksBtn);
        chooseTasksBtn.setOnClickListener(this);
        addTaskBtn.setOnClickListener(this);
        calenderBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent ct;
        switch (v.getId()) {
            case (R.id.calenderBtn):
                ct = new Intent(getActivity(), Homepage.class);
                ct.putExtra("FromHomepage", "2");
                getActivity().startActivity(ct);
                break;
            case (R.id.addTaskBtn):
                 ct= new Intent(getActivity(), AddTasks.class);
                getActivity().startActivity(ct);
                break;
            case (R.id.chooseTasksBtn):
                 ct= new Intent(getActivity(), ChooseTasks.class);
                getActivity().startActivity(ct);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu_xml, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.action_logout){
            Toast.makeText(getContext(), "Logout clicked inside dashboard", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
