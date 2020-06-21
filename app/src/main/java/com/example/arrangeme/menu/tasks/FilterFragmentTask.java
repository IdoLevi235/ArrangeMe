package com.example.arrangeme.menu.tasks;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.arrangeme.R;
import com.example.arrangeme.menu.calendar.FilterFragment;

/**
 * This class contorls the filter in tasks tab
 */
public class FilterFragmentTask extends FilterFragment {
    private TextView chooseText1;
    private TextView chooseText2;
    private RelativeLayout rl;
    private View view2;
    private View view3;
    private RadioGroup rg1;
    private AppCompatImageView applyBtn;

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chooseText1=view.findViewById(R.id.chooseText);
        chooseText1.setVisibility(View.GONE);
        rl=view.findViewById(R.id.relativeLayout3);
        rl.setVisibility(View.GONE);
        view2=view.findViewById(R.id.view2);
        view2.setVisibility(View.GONE);
        view3=view.findViewById(R.id.view3);
        view3.setVisibility(View.GONE);
        chooseText2=view.findViewById(R.id.ChooseText);
        chooseText2.setVisibility(View.GONE);
        rg1=view.findViewById(R.id.rg1);
        rg1.setVisibility(View.GONE);
        applyBtn=view.findViewById(R.id.buttonApply);
        applyBtn.setOnClickListener(this);
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
        }
    }
}
