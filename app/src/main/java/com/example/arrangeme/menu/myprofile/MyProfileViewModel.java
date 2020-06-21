package com.example.arrangeme.menu.myprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 *
 */
public class MyProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     *
     */
    public MyProfileViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("This is myProfile fragment");
    }

    /**
     * @return
     */
    public LiveData<String> getText() {
        return mText;
    }
}