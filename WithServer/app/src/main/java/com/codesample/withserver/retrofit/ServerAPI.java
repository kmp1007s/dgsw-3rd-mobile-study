package com.codesample.withserver.retrofit;

import com.codesample.withserver.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServerAPI {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("/users/{userid}")
    Call<User> getUser(@Path("userid") String userid);
}
