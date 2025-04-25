package com.pettrack.pettrack.activitypettrack.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisMascotasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MisMascotasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mis Mascotas");
    }

    public LiveData<String> getText() {
        return mText;
    }
}