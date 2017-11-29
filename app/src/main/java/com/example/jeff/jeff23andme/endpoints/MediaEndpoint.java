package com.example.jeff.jeff23andme.endpoints;


import com.example.jeff.jeff23andme.model.Media;
import com.example.jeff.jeff23andme.model.MediaResponse;
import com.example.jeff.jeff23andme.model.Popular;
import com.example.jeff.jeff23andme.model.SearchMediaResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MediaEndpoint extends BaseEndpoint {

    private static interface MediaService {

        @GET("/media/{media_id}")
        Call<MediaResponse>  getMedia(
                @Path("media_id") String mediaId,
                @Query("access_token") String accessToken);

        @GET("/media/search")
        public SearchMediaResponse search(
                @Query("access_token") String accessToken,
                @Query("distance") Integer distanceInKm,
                @Query("lat") Double latitude,
                @Query("lng") Double longitude,
                @Query("min_timestamp") Long minTimestamp,
                @Query("max_timestamp") Long maxTimestamp);

        @GET("/media/popular")
        public Popular getPopular(
                @Query("access_token") String accessToken);

    }

    private final MediaService mediaService;

    public MediaEndpoint(final String accessToken) {
        super(accessToken);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mediaService = retrofit.create(MediaService.class);
    }

    public Call<MediaResponse> getMedia(final String mediaId) {
        return mediaService.getMedia(mediaId, accessToken);
    }

    public SearchMediaResponse search(final Integer distance) {
        return mediaService.search(accessToken, distance, null, null, null, null);
    }

    public SearchMediaResponse search(final Double latitude, final Double longitude) {
        return mediaService.search(accessToken, null, latitude, longitude, null, null);
    }

    public SearchMediaResponse search(final Integer distance, final Double latitude, final Double longitude, final Long minTimestamp, final Long maxTimestamp) {
        return mediaService.search(accessToken, distance, latitude, longitude, minTimestamp, maxTimestamp);
    }

    public Popular getPopular() {
        return mediaService.getPopular(accessToken);
    }

}
