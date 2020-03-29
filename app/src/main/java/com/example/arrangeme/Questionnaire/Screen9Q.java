package com.example.arrangeme.Questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.arrangeme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen9Q extends Fragment {

    public Screen9Q() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController= Navigation.findNavController(view);
        Button button = view.findViewById(R.id.continue9);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                navController.navigate(R.id.action_screen9Q_to_startQ);
            }
        });
    }
}
