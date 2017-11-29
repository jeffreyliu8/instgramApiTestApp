package com.example.jeff.jeff23andme.endpoints;


import com.example.jeff.jeff23andme.model.DeleteLikeResponse;
import com.example.jeff.jeff23andme.model.LikeResponse;
import com.example.jeff.jeff23andme.model.Likes;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class LikesEndpoint extends BaseEndpoint {

    private static interface LikesService {

        @GET("/v1/media/{media_id}/likes")
        Likes getLikes(
                @Path("media_id") String mediaId,
                @Query("access_token") String accessToken);

        @POST("/v1/media/{media_id}/likes")
        Call<LikeResponse> postLike(
                @Path("media_id") String mediaId,
                @Query("access_token") String accessToken);

        @DELETE("/v1/media/{media_id}/likes")
        Call<DeleteLikeResponse> deleteLike(
                @Path("media_id") String mediaId,
                @Query("access_token") String accessToken);

    }

    private final LikesService likesService;

    public LikesEndpoint(final String accessToken) {
        super(accessToken);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        likesService = retrofit.create(LikesService.class);
    }

    public Likes getLikes(final String mediaId) {
        return likesService.getLikes(mediaId, accessToken);
    }

    public Call<LikeResponse> like(final String mediaId) {
        return likesService.postLike(mediaId, accessToken);
    }

    public Call<DeleteLikeResponse> unlike(final String mediaId) {
        return likesService.deleteLike(mediaId, accessToken);
    }

}
