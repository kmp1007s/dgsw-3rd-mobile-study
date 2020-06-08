package com.codesample.withserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codesample.withserver.data.User;
import com.codesample.withserver.databinding.ActivityMainBinding;
import com.codesample.withserver.retrofit.Server;
import com.codesample.withserver.volley.SingleQueue;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Call<User> request;

    class SingleRequestTask extends AsyncTask<String, Void, User> {
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Log.i("Main", user.name);
        }

        @Override
        protected User doInBackground(String... strings) {
            String urlString = "https://01e987ee-74dc-42ef-b237-9dd88385e0f9.mock.pstmn.io/users/" + strings[0];
            String result;
            User user = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                while((line=br.readLine()) !=null) {
                    builder.append(line).append("\n");
                }
                result = builder.toString();
                Log.i("Main", result);
                Gson gson = new Gson();
                user = gson.fromJson(result, User.class);
            } catch(IOException e) {
                e.printStackTrace();
            }
            return user;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request.cancel();
        SingleQueue.getInstance(this).getRequestQueue().cancelAll("getUser");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request = Server.getInstance().getAPI().getUser("1000");
        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.i("Main-retrofit", user.name);

                binding.textViewUserId.setText(user.userid);
                binding.textViewUserName.setText(user.name);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

//        new SingleRequestTask().execute("1");

        String url = "https://01e987ee-74dc-42ef-b237-9dd88385e0f9.mock.pstmn.io/users/100";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.i("Main-Volley", response.toString());
                },
                error->{}
        );
        SingleQueue.getInstance(this).getRequestQueue().add(request);
    }
}
