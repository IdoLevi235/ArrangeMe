package com.example.arrangeme.menu.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 *
 */
public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     *
     */
    public DashboardViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("This is dashboard fragment");
    }

    /**
     * @return
     */
    public LiveData<String> getText() {
        return mText;
    }
}