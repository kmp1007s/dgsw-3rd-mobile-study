package com.codesample.whatid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.codesample.whatid.data.AppDatabase;
import com.codesample.whatid.data.User;
import com.codesample.whatid.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(getApplicationContext());

        binding.buttonLogin.setOnClickListener(v -> {
            String userId = binding.editTextUserId.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            User user = new User(userId, password);

            new LoginTask().execute(user);
        });

        binding.buttonRegister.setOnClickListener(v -> {
            String userId = binding.editTextUserId.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            User user = new User(userId, password);

            new RegisterTask().execute(user);
            new LoginTask().execute(user);
        });
    }

    class LoginTask extends AsyncTask<User, Void, User> {
        @Override
        protected void onPostExecute(User aUser) {
            super.onPostExecute(aUser);

            if(aUser == null) {
                Toast.makeText(getApplicationContext(),"인증에 실패했습니다",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FolderActivity.class);
                intent.putExtra("userId", aUser.userId);
                startActivity(intent);
            }
        }

        @Override
        protected User doInBackground(User... users) {
            User user = db.getUserDAO().getUserByUserId(users[0].userId);

            if(user != null && users[0].password.equals(user.password))
                return users[0];

            return null;
        }
    }

    class RegisterTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean)
                Toast.makeText(getApplicationContext(),"회원가입에 성공했습니다",Toast.LENGTH_LONG).show();

            else
                Toast.makeText(getApplicationContext(),"이미 아이디가 존재합니다",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(User... users) {
            User exist = db.getUserDAO().getUserByUserId(users[0].userId);

            if(exist != null)
                return false;

            db.getUserDAO().addUser(users[0]);
            return true;
        }
    }
}
