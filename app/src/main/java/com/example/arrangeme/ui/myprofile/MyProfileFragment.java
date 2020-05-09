package com.example.arrangeme.ui.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private MyProfileViewModel myProfileViewModel;
    private ImageView avatarBtn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button pictureCircle;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatarBtn = view.findViewById(R.id.AvatarCircle);
        avatarBtn.setOnClickListener(this);


        pictureCircle = view.findViewById(R.id.pictureCircle);
        pictureCircle.setOnClickListener(this);

        TabItem achievementsTab = view.findViewById(R.id.achievementsTab);
        TabItem infoTab = view.findViewById(R.id.infoTab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getParentFragmentManager());
        //Adding fragments to the viewpager
        adapter.AddFragment(new Achievements(), "Achievements");
        adapter.AddFragment(new ProfileInfo(), "info");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AvatarCircle:
                startActivity(new Intent(getActivity(), AvatarsPopup.class));
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
    }

    //TODO: problem- when come back from another tab the that won't open the Tabs properly
}
