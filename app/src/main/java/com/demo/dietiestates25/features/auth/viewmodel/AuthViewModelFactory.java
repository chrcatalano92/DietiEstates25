package com.demo.dietiestates25.features.auth.viewmodel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.dietiestates25.data.source_api_auth.AuthApiService;
import com.demo.dietiestates25.features.auth.data_repository.AuthRepository;
import com.demo.dietiestates25.features.auth.data_repository.AuthRepositoryImpl;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private final Activity activity;

    public AuthViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AuthViewModel.class)) {
            Retrofit retrofit = RetrofitClient.getInstance(activity.getApplicationContext(), 10, GsonConverterFactory.create());
            AuthApiService apiService = retrofit.create(AuthApiService.class);
            AuthRepository repository = new AuthRepositoryImpl(apiService);

            return (T) new AuthViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
