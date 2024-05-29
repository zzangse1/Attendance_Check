package com.zzangse.attendance_check.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GroupViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<String> groupName = new MutableLiveData<>();

    public LiveData<String> getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        groupName.setValue(name);
    }

    @Override
    public String toString() {
        return "GroupViewModel{" +
                "groupName=" + groupName +
                '}';
    }
}
