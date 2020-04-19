package com.example.arrangeme.ui.calendar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;

public class FilterFragment extends Fragment implements View.OnClickListener {


    private Button studyBtn;
    private Button friendsBtn;
    private Button familyBtn;
    private Button workBtn;
    private Button relaxBtn;
    private Button sportBtn;
    private Button nutritionBtn;
    private Button choresBtn;
    private Button otherBtn;
    private CheckBox checkBoxAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studyBtn =view.findViewById(R.id.studyBtn);
        studyBtn.setOnClickListener(this);

        friendsBtn =view.findViewById(R.id.friendsBtn);
        friendsBtn.setOnClickListener(this);

        familyBtn =view.findViewById(R.id.familyBtn);
        familyBtn.setOnClickListener(this);

        workBtn =view.findViewById(R.id.workBtn);
        workBtn.setOnClickListener(this);

        relaxBtn =view.findViewById(R.id.relaxBtn);
        relaxBtn.setOnClickListener(this);

        sportBtn =view.findViewById(R.id.sportBtn);
        sportBtn.setOnClickListener(this);

        nutritionBtn =view.findViewById(R.id.nutritionBtn);
        nutritionBtn.setOnClickListener(this);

        choresBtn =view.findViewById(R.id.choresBtn);
        choresBtn.setOnClickListener(this);

        otherBtn =view.findViewById(R.id.otherBtn);
        otherBtn.setOnClickListener(this);

        checkBoxAll =view.findViewById(R.id.checkBoxAll);
        checkBoxAll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case (R.id.studyBtn):
                if(studyBtn.isPressed()==true)
                {
                    setBtnUnFocus(studyBtn);
                }
                else {
                    setBtnFocus(studyBtn);
                }
                break;
            case (R.id.friendsBtn):
                if(friendsBtn.isPressed()==true)
                {
                    setBtnUnFocus(friendsBtn);
                }
                else {
                    setBtnFocus(friendsBtn);
                }
                break;
            case (R.id.familyBtn):
                if(familyBtn.isPressed()==true)
                {
                    setBtnUnFocus(familyBtn);
                }
                else {
                    setBtnFocus(familyBtn);
                }
                break;
            case (R.id.workBtn):
                if(workBtn.isPressed()==true)
                {
                    setBtnUnFocus(workBtn);
                }
                else {
                    setBtnFocus(workBtn);
                }
                break;
            case (R.id.relaxBtn):
                if(relaxBtn.isPressed()==true)
                {
                    setBtnUnFocus(relaxBtn);
                }
                else {
                    setBtnFocus(relaxBtn);
                }
                break;
            case (R.id.sportBtn):
                if(sportBtn.isPressed()==true)
                {
                    setBtnUnFocus(sportBtn);
                }
                else {
                    setBtnFocus(sportBtn);
                }
                break;
            case (R.id.nutritionBtn):
                if(nutritionBtn.isPressed()==true)
                {
                    setBtnUnFocus(nutritionBtn);
                }
                else {
                    setBtnFocus(nutritionBtn);
                }
                break;
            case (R.id.choresBtn):
                if(choresBtn.isPressed()==true)
                {
                    setBtnUnFocus(choresBtn);
                }
                else {
                    setBtnFocus(choresBtn);
                }
                break;
            case (R.id.otherBtn):
                if(otherBtn.isPressed()==true)
                {
                    setBtnUnFocus(otherBtn);
                }
                else {
                    setBtnFocus(otherBtn);
                }
                break;

            case (R.id.checkBoxAll):
                if(checkBoxAll.isChecked()==true)
                {
                    setBtnUnFocus(studyBtn);
                    setBtnUnFocus(friendsBtn);
                    setBtnUnFocus(familyBtn);
                    setBtnUnFocus(workBtn);
                    setBtnUnFocus(relaxBtn);
                    setBtnUnFocus(nutritionBtn);
                    setBtnUnFocus(sportBtn);
                    setBtnUnFocus(otherBtn);
                }
                else {
                    setBtnUnFocus(studyBtn);
                    setBtnUnFocus(friendsBtn);
                    setBtnUnFocus(familyBtn);
                    setBtnUnFocus(workBtn);
                    setBtnUnFocus(relaxBtn);
                    setBtnUnFocus(nutritionBtn);
                    setBtnUnFocus(sportBtn);
                    setBtnUnFocus(otherBtn);
                }
                break;
            default:
                break;
        }

    }

       public void setBtnFocus(Button btn){
           btn.setPressed(true);
           Log.d("TAG", "setBtnFocus: btn");
           btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
           btn.setTextColor(Color.parseColor("#FFFFFF"));
           btn.setBackgroundResource(R.drawable.rounded_rec_blue_nostroke);
        }

       public void setBtnUnFocus(Button btn){
           btn.setPressed(false);
           switch (btn.getId()) {
               case (R.id.studyBtn):
               {
                   Log.d("TAG", "setBtnUnFocus: btn");
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FF8A65")));
                   btn.setTextColor(Color.parseColor("#FF8A65"));
                   btn.setBackgroundResource(R.drawable.category_btn_study);
               }
               break;
               case (R.id.friendsBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFD54F")));
                   btn.setTextColor(Color.parseColor("#FFD54F"));
                   btn.setBackgroundResource(R.drawable.category_btn_friends);
               }
               break;
               case (R.id.familyBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#F06292")));
                   btn.setTextColor(Color.parseColor("#F06292"));
                   btn.setBackgroundResource(R.drawable.category_btn_family);
               }
               break;
               case (R.id.workBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#E57373")));
                   btn.setTextColor(Color.parseColor("#E57373"));
                   btn.setBackgroundResource(R.drawable.category_btn_work);
               }
               break;
               case (R.id.relaxBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#9575CD")));
                   btn.setTextColor(Color.parseColor("#9575CD"));
                   btn.setBackgroundResource(R.drawable.category_btn_relax);
               }
               break;
               case (R.id.sportBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#4DD0E1")));
                   btn.setTextColor(Color.parseColor("#4DD0E1"));
                   btn.setBackgroundResource(R.drawable.category_btn_sport);
               }
               break;
               case (R.id.nutritionBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                   btn.setTextColor(Color.parseColor("#81C784"));
                   btn.setBackgroundResource(R.drawable.category_btn_nutrition);
               }
               break;
               case (R.id.choresBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")));
                   btn.setTextColor(Color.parseColor("#64B5F6"));
                   btn.setBackgroundResource(R.drawable.category_btn_chores);
               }
               break;
               case (R.id.otherBtn):
               {
                   btn.setTextColor(Color.parseColor("#A1887F"));
                   btn.setBackgroundResource(R.drawable.category_btn_other);
               }
               break;
               default:
                   break;
           }
    }




}
