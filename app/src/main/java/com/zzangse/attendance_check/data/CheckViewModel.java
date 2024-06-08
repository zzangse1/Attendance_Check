package com.zzangse.attendance_check.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class CheckViewModel extends ViewModel {
    private static final String CHECKED_KEY = "checked_key";
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<Boolean> isChecked = new MutableLiveData<>();

    public CheckViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public LiveData<Boolean> getIsChecked() {
        return savedStateHandle.getLiveData(CHECKED_KEY);
    }

    public void setIsChecked(boolean checked) {
        savedStateHandle.set(CHECKED_KEY, checked);
    }
}
