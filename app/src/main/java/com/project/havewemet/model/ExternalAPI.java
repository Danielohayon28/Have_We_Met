package com.project.havewemet.model;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExternalAPI {
    @GET("/?&apikey=c86677ee")
    Call<ExtApiSearchResult>searchMovieByTitle(@Query("s")String title);

    @GET("/?&apikey=c86677ee")
    Call<ExtApiSearchResult>getMovieByTitle(@Query("t")String title);
}
