package com.codesample.withserver.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    private static Server instance;
    private ServerAPI api;

    private Server() {
        String url = "https://01e987ee-74dc-42ef-b237-9dd88385e0f9.mock.pstmn.io";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerAPI.class);
    }
    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }
    public ServerAPI getAPI() { return api; }
}
