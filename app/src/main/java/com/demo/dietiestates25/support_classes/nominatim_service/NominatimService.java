package com.demo.dietiestates25.support_classes.nominatim_service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimService {
    @GET("search")
    Call<List<NominatimResult>> search(
            @Query("q") String query,
            @Query("format") String format,
            @Query("limit") int limit
    );
}
