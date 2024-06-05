package com.zzangse.attendance_check.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DateViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<Long> selectedDate = new MutableLiveData<>();

    public void setSelectedDate(Long date) {
        selectedDate.setValue(date);
    }

    public LiveData<Long> getSelectedDate() {
        return selectedDate;
    }


    @Override
    public String toString() {
        return "DateViewModel{" +
                "selectedDate=" + selectedDate +
                '}';
    }
}
