package com.codesample.foodrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.codesample.foodrecorder.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

            binding.textViewRecord.setText(time + " - " + food);
            binding.textViewElapsed.setText("0시간 0분 경과");

            editor.putString("food", food);
            editor.putString("time", time);
            editor.apply(); // 비동기로 수행
        }
    };
}
