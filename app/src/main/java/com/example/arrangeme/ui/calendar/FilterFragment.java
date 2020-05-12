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

import java.util.HashSet;
import java.util.Set;

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
    protected Set<String> Category_Set = new HashSet<String>();

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
                if(!Category_Set.contains("Study"))
                {
                    Category_Set.add("Study");
                    setBtnFocus(studyBtn);
                }
                else {
                    Category_Set.remove("Study");
                    setBtnUnFocus(studyBtn);
                }
                break;
            case (R.id.friendsBtn):
                if(!Category_Set.contains("Friends"))
                {
                    Category_Set.add("Friends");
                    setBtnFocus(friendsBtn);
                }
                else {
                    Category_Set.remove("Friends");
                    setBtnUnFocus(friendsBtn);
                }
                break;
            case (R.id.familyBtn):
                if(!Category_Set.contains("Family"))
                {
                    Category_Set.add("Family");
                    setBtnFocus(familyBtn);
                }
                else {
                    Category_Set.remove("Family");
                    setBtnUnFocus(familyBtn);
                }
                break;
            case (R.id.workBtn):
                if(!Category_Set.contains("Work"))
                {
                    Category_Set.add("Work");
                    setBtnFocus(workBtn);
                }
                else {
                    Category_Set.remove("Work");
                    setBtnUnFocus(workBtn);
                }
                break;
            case (R.id.relaxBtn):
                if(!Category_Set.contains("Relax"))
                {
                    Category_Set.add("Relax");
                    setBtnFocus(relaxBtn);
                }
                else {
                    Category_Set.remove("Relax");
                    setBtnUnFocus(relaxBtn);
                }
                break;
            case (R.id.sportBtn):
                if(!Category_Set.contains("Sport"))
                {
                    Category_Set.add("Sport");
                    setBtnFocus(sportBtn);
                }
                else {
                    Category_Set.remove("Sport");
                    setBtnUnFocus(sportBtn);
                }
                break;
            case (R.id.nutritionBtn):
                if(!Category_Set.contains("Nutrition"))
                {
                    Category_Set.add("Nutrition");
                    setBtnFocus(nutritionBtn);
                }
                else {
                    Category_Set.remove("Nutrition");
                    setBtnUnFocus(nutritionBtn);
                }
                break;
            case (R.id.choresBtn):
                if(!Category_Set.contains("Chores"))
                {
                    Category_Set.add("Chores");
                    setBtnFocus(choresBtn);
                }
                else {
                    Category_Set.remove("Chores");
                    setBtnUnFocus(choresBtn);
                }
                break;
            case (R.id.otherBtn):
                if(!Category_Set.contains("Other"))
                {
                    Category_Set.add("Other");
                    setBtnFocus(otherBtn);
                }
                else {
                    Category_Set.remove("Other");
                    setBtnUnFocus(otherBtn);
                }
                break;

            case (R.id.checkBoxAll):
                if(!checkBoxAll.isChecked())
                {
                    setBtnUnFocus(studyBtn);
                    Category_Set.remove("Study");
                    setBtnUnFocus(friendsBtn);
                    Category_Set.remove("Friends");
                    setBtnUnFocus(familyBtn);
                    Category_Set.remove("Family");
                    setBtnUnFocus(workBtn);
                    Category_Set.remove("Work");
                    setBtnUnFocus(relaxBtn);
                    Category_Set.remove("Relax");
                    setBtnUnFocus(nutritionBtn);
                    Category_Set.remove("Nutrition");
                    setBtnUnFocus(sportBtn);
                    Category_Set.remove("Sport");
                    setBtnUnFocus(choresBtn);
                    Category_Set.remove("Chores");
                    setBtnUnFocus(otherBtn);
                    Category_Set.remove("Other");
                }
                else {
                    setBtnFocus(studyBtn);
                    Category_Set.add("Study");
                    setBtnFocus(friendsBtn);
                    Category_Set.add("Friends");
                    setBtnFocus(familyBtn);
                    Category_Set.add("Family");
                    setBtnFocus(workBtn);
                    Category_Set.add("Work");
                    setBtnFocus(relaxBtn);
                    Category_Set.add("Relax");
                    setBtnFocus(nutritionBtn);
                    Category_Set.add("Nutrition");
                    setBtnFocus(sportBtn);
                    Category_Set.add("Sport");
                    setBtnFocus(choresBtn);
                    Category_Set.add("Chores");
                    setBtnFocus(otherBtn);
                    Category_Set.add("Other");
                }
                break;
            default:
                break;
        }

    }

    public void setBtnFocus(Button btn){
        switch (btn.getId()) {
            case (R.id.studyBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_study_nostroke);
            }
            break;
            case (R.id.friendsBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_friends_nostroke);
            }
            break;
            case (R.id.familyBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_family_nostroke);
            }
            break;
            case (R.id.workBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_work_nostroke);
            }
            break;
            case (R.id.relaxBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_relax_nostroke);
            }
            break;
            case (R.id.sportBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_sport_nostroke);
            }
            break;
            case (R.id.nutritionBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_nutrition_nostroke);
            }
            break;
            case (R.id.choresBtn):
            {
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_chores_nostroke);
            }
            break;
            case (R.id.otherBtn):
            {
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setPadding(0,6,0,0);
                btn.setBackgroundResource(R.drawable.rounded_rec_other_nostroke);
            }
            break;
            default:
                break;
        }
    }


       public void setBtnUnFocus(Button btn){
           switch (btn.getId()) {
               case (R.id.studyBtn):
               {
                   Log.d("TAG", "setBtnUnFocus: btn");
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FF8A65")));
                   btn.setTextColor(Color.parseColor("#FF8A65"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_study);
               }
               break;
               case (R.id.friendsBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFD54F")));
                   btn.setTextColor(Color.parseColor("#FFD54F"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_friends);
               }
               break;
               case (R.id.familyBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#F06292")));
                   btn.setTextColor(Color.parseColor("#F06292"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_family);
               }
               break;
               case (R.id.workBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#E57373")));
                   btn.setTextColor(Color.parseColor("#E57373"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_work);
               }
               break;
               case (R.id.relaxBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#9575CD")));
                   btn.setTextColor(Color.parseColor("#9575CD"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_relax);
               }
               break;
               case (R.id.sportBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#4DD0E1")));
                   btn.setTextColor(Color.parseColor("#4DD0E1"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_sport);
               }
               break;
               case (R.id.nutritionBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                   btn.setTextColor(Color.parseColor("#81C784"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_nutrition);
               }
               break;
               case (R.id.choresBtn):
               {
                   btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")));
                   btn.setTextColor(Color.parseColor("#64B5F6"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_chores);
               }
               break;
               case (R.id.otherBtn):
               {
                   btn.setTextColor(Color.parseColor("#A1887F"));
                   btn.setPadding(0,6,0,0);
                   btn.setBackgroundResource(R.drawable.category_btn_other);
               }
               break;
               default:
                   break;
           }
    }


    //TODO: set padding isn't working here




}
