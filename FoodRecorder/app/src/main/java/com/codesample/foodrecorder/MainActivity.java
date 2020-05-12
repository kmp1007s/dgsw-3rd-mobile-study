package com.codesample.foodrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codesample.foodrecorder.data.FoodRecord;
import com.codesample.foodrecorder.data.FoodRecordOpenHelper;
import com.codesample.foodrecorder.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    private FoodRecordOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new FoodRecordOpenHelper(this, "db", null, 1);
        ArrayList<FoodRecord> list = helper.getRecords();
        for(FoodRecord r: list) Log.i("Main", r.getFood() + r.getTime());

        preferences = getSharedPreferences("food", Context.MODE_PRIVATE);
        String lastFood = preferences.getString("food", null);
        String lastTime = preferences.getString("time", null);

        displayRecord(lastFood, lastTime);
        binding.buttonRecord.setOnClickListener(onSave);
    }

    private void displayRecord(String lastFood, String lastTime) {

        if(lastFood == null) {
            binding.textViewRecord.setText("저장된 기록이 없습니다");
            binding.textViewElapsed.setText("경과 시간이 없습니다");
            return;
        }

        LocalDateTime startTime = LocalDateTime.parse(lastTime);
        LocalDateTime endTime = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREA);
        String timeStr = startTime.format(formatter);

        long hour = ChronoUnit.HOURS.between(startTime, endTime);
        long minute = ChronoUnit.MINUTES.between(startTime, endTime);
        minute -= hour * 60;

        binding.textViewRecord.setText(timeStr + " - " + lastFood);
        binding.textViewElapsed.setText(
                    String.format(Locale.KOREA, "%d시간 %02d분 경과", hour, minute));
    }

    View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = preferences.edit();

            String food = binding.editText.getText().toString();
            String time = LocalDateTime.now().toString();

            editor.putString("food", food);
            editor.putString("time", time);
            editor.apply(); // 비동기로 수행

            helper.addRecord(new FoodRecord(food, time));

            displayRecord(food, time);
        }
    };
}
