package com.pettrack.pettrack.activitypettrack.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiPerfilViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MiPerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mi Perfil");
    }

    public LiveData<String> getText() {
        return mText;
    }
}