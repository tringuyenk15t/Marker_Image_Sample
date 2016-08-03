package com.example.tringuyen.marker_image_load_sample.network;

import com.example.tringuyen.marker_image_load_sample.models.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tri Nguyen on 8/1/2016.
 */
public interface Google_direction {

    @GET("directions/json")
    Call<DirectionResponse> getDirections(@Query("origin") String originalCoordinates, @Query("destination") String destinationCoordinates);
}
