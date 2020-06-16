package com.example.mycafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycafe.data.CafeMenu;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<String> userId = new MutableLiveData<>();
    private final MutableLiveData<CafeMenu> selected = new MutableLiveData<>();

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }
    public LiveData<String> getUserId() {
        return userId;
    }

    public void setSelected(CafeMenu menu) {
        this.selected.setValue(menu);
    }
    public LiveData<CafeMenu> getSelected() {
        return selected;
    }
}
