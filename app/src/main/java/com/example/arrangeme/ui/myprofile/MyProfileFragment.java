package com.example.arrangeme.ui.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE = 1;
    private MyProfileViewModel myProfileViewModel;
    private ImageView avatarBtn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button pictureCircle;
    private Uri profileImage;
    private int[] tabIcons = { R.drawable.flagachive,  R.drawable.profiledetails};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        myProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatarBtn = view.findViewById(R.id.AvatarCircle);
        avatarBtn.setOnClickListener(this);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

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
        setUpIcons();
    }

    private void setUpIcons() {

        tabLayout.setInlineLabel(true);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri profileImage = data.getData();
                    try {
                        Context applicationContext = Homepage.getContextOfApplication();
                        InputStream inputStream =  applicationContext.getContentResolver().openInputStream(profileImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.circle_choose_tasks));
                        pictureCircle.setHint("");
                        pictureCircle.setCompoundDrawables(null,null,null,null);
                        pictureCircle.setBackground(d);

                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        pictureCircle.setBackground(d);
                    }
                    break;
            }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AvatarCircle:
                startActivity(new Intent(getActivity(), AvatarsPopup.class));
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            break;
            case R.id.pictureCircle:
                pickFromGallery();
                break;
        }

    }



}
