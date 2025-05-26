package com.pettrack.pettrack.activitypettrack.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final ApiService apiService = ApiClient.getClient().create(ApiService.class);

    public MiPerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mi Perfil");
        isLoading.setValue(false);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<User> getUserData() {
        return userData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadUserData(int userId) {
        isLoading.setValue(true);
        apiService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    userData.setValue(response.body());
                } else {
                    errorMessage.setValue("Error al cargar datos del usuario");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}