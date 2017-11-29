package com.example.jeff.jeff23andme.endpoints;

import com.example.jeff.jeff23andme.model.Feed;
import com.example.jeff.jeff23andme.model.Liked;
import com.example.jeff.jeff23andme.model.Profile;
import com.example.jeff.jeff23andme.model.Recent;
import com.example.jeff.jeff23andme.model.SearchUserResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public final class UsersEndpoint extends BaseEndpoint {

    private static interface UserService {

        @GET("v1/users/self")
        Call<Profile> getSelf(@Query("access_token") String accessToken);

        @GET("v1/users/{user_id}")
        Call<Profile> getUser(
                @Path("user_id") String userId,
                @Query("access_token") String accessToken);

        @GET("v1/users/self/feed")
        Feed getFeed(
                @Query("access_token") String accessToken,
                @Query("count") Integer count,
                @Query("min_id") String minId,
                @Query("max_id") String maxId);

        @GET("v1/users/{user_id}/media/recent")
        Call<Recent> getRecent(
                @Path("user_id") String userId,
                @Query("access_token") String accessToken,
                @Query("count") Integer count,
                @Query("min_id") String minId,
                @Query("max_id") String maxId);

        @GET("v1/users/self/media/liked")
        Liked getLiked(
                @Query("access_token") String accessToken,
                @Query("count") Integer count,
                @Query("max_like_id") String maxLikeId);

        @GET("v1/users/search")
        SearchUserResponse search(
                @Query("access_token") String accessToken,
                @Query("q") String query,
                @Query("count") Integer count);

    }

    private final UserService userService;

    public UsersEndpoint(final String accessToken) {
        super(accessToken);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
    }

    public Call<Profile> getSelf() {
        return userService.getUser("self", accessToken);
    }

    public Feed getFeed() {
        return userService.getFeed(accessToken, null, null, null);
    }

    public Feed getFeed(final Integer count, final String minId, final String maxId) {
        return userService.getFeed(accessToken, count, minId, maxId);
    }

    public Call<Recent> getRecent() {
        return userService.getRecent("self", accessToken, null, null, null);
    }

    public Call<Recent> getRecent( final Integer count, final String minId, final String maxId) {
        return userService.getRecent("self", accessToken, count, minId, maxId);
    }

    public Liked getLiked() {
        return userService.getLiked(accessToken, null, null);
    }

    public Liked getLiked(final Integer count, final String maxLikeId) {
        return userService.getLiked(accessToken, count, maxLikeId);
    }

    public SearchUserResponse search(final String query) {
        return userService.search(accessToken, query, null);
    }

    public SearchUserResponse search(final String query, final Integer count) {
        return userService.search(accessToken, query, count);
    }

}
