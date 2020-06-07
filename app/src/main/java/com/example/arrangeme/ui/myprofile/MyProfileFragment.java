package com.example.arrangeme.ui.myprofile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE = 1;
    private MyProfileViewModel myProfileViewModel;
    private ImageView avatarBtn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DatabaseReference mDatabase;
    private RoundedImageView pictureCircle;
    //private Button pictureCircle;
    private FrameLayout containerFilter;
    private Uri profileImage;
    private StorageReference mStorageRef;
    private int[] tabIcons = { R.drawable.flagachive1,  R.drawable.card1};
    int flag=0;
    private Uri profileImage2;
    CropImageView cropImageView = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        containerFilter = root.findViewById(R.id.filter_container);
        myProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatarBtn = view.findViewById(R.id.AvatarCircle);
        avatarBtn.setOnClickListener(this);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        pictureCircle = view.findViewById(R.id.pictureCircle);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        TabItem achievementsTab = view.findViewById(R.id.achievementsTab);
        TabItem infoTab = view.findViewById(R.id.infoTab);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getParentFragmentManager());
        //Adding fragments to the viewpager
        adapter.AddFragment(new Achievements(), "Achievements");
        adapter.AddFragment(new ProfileInfo(), "info");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpIcons();
        setUpImages();
        pictureCircle.setOnClickListener(this);
        //sendImage(profileImage);
    }



    public void setUpImages() {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info").child("profile_photo");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String imageURL = (String) dataSnapshot.getValue();
                    try {
                        Transformation transformation = new RoundedTransformationBuilder().oval(true).scaleType(ImageView.ScaleType.FIT_XY).build();
                        //   Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.BLACK).borderWidthDp(0).cornerRadiusDp(0).oval(true).build();
                        Picasso.get().load(imageURL).noFade().fit().centerCrop().transform(transformation).into(pictureCircle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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

    /**
     * add photo link to the correct anchor in DB
     * @param downloadUri
     */
    private void addPhotoUriToDB(Uri downloadUri) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");
        mDatabase.child("profile_photo").setValue(downloadUri.toString());
        ///////////////////////////////////////////////////////////////////////refreshing fragment-works but ViewModel of the tabs wont work
//        FragmentTransaction ftr = getFragmentManager().beginTransaction();
//        ftr.detach(MyProfileFragment.this).commitNowAllowingStateLoss();
//        ftr.attach(MyProfileFragment.this).commitNowAllowingStateLoss();
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri profileImage = data.getData();
                    if (profileImage!=null){
                        sendImage(profileImage); //HERE we send it to storage
                    }

                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(profileImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.add_task_round));
                        //pictureCircle.setBackground(d);
                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        //pictureCircle.setBackground(d);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == Activity.RESULT_OK) {
                        Uri resultUri = result.getUri();
                        sendImage(resultUri); //HERE we send it to storage
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;
            }
    }


    private void pickFromGallery() {
        CropImage.activity().start(getContext(), this);

    }




    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.settingsIcon){
            Intent ct= new Intent(getActivity(), Settings.class);
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            getActivity().startActivity(ct);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Send image to our storage
     *
     * @param profileImage
     */
    private void sendImage(Uri profileImage) {
        String uniqueID = UUID.randomUUID().toString();
        StorageReference imgRef = mStorageRef.child("images/profile_pic/"+Globals.UID+"/"+uniqueID+".jpg");
        try {
            UploadTask uploadTask = imgRef.putFile(profileImage);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return imgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        addPhotoUriToDB(downloadUri);
                        Log.d("TAG8", "onComplete: down: " + downloadUri);
                    } else {
                        Log.d("TAG8", "onComplete: Download link generation failed");
                    }
                }
            });
        }    catch (Exception e) {
            e.printStackTrace();
        }

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

    public ViewPager getViewPager() {
        return viewPager;
    }



}
