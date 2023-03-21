package com.project.havewemet.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExternalModel {

    final public static ExternalModel instance = new ExternalModel();
    public static ExternalModel instance(){
        return instance;
    }

    final String BASE_URL = "https://www.omdbapi.com/";
    Retrofit retrofit;
    ExternalAPI externalAPI;

    private ExternalModel(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        externalAPI = retrofit.create(ExternalAPI.class);

    }


    public LiveData<List<External>> searchEducationByTitle(String title) {
        MutableLiveData<List<External>> data = new MutableLiveData<>();
        Call<ExtApiSearchResult> call = externalAPI.searchMovieByTitle(title);

        call.enqueue(new Callback<ExtApiSearchResult>() {
            @Override
            public void onResponse(Call<ExtApiSearchResult> call, Response<ExtApiSearchResult> response) {
                if (response.isSuccessful()) {
                    ExtApiSearchResult res = response.body();
                    data.setValue(res.getSearch());
                } else {
                    Log.d("TAG", " error");
                }
            }

            @Override
            public void onFailure(Call<ExtApiSearchResult> call, Throwable t) {
                Log.d("TAG", "error");
            }


        });
        return data;


    }
}
