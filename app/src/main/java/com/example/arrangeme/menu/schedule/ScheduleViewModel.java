package com.example.arrangeme.menu.schedule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 *
 */
public class ScheduleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     *
     */
    public ScheduleViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("This is Schedule fragment");
    }

    /**
     * @return
     */
    public LiveData<String> getText() {
        return mText;
    }
}
