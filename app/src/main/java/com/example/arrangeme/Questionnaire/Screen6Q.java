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
public class Screen6Q extends Fragment {

    public Screen6Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen6_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController= Navigation.findNavController(view);
        Button button = view.findViewById(R.id.continue6);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                navController.navigate(R.id.action_screen6Q_to_screen7Q);
            }
        });
    }
}
