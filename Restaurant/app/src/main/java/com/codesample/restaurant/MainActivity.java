package com.codesample.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.codesample.restaurant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidgets();
    }

    private void initWidgets() {
        binding.btnMenu.setOnClickListener(this);
        binding.btnMap.setOnClickListener(this);
        binding.btnEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        View[] buttons = {binding.btnMenu, binding.btnMap, binding.btnEvent};
        View[] viewContents = {binding.menuContent, binding.mapContent, binding.eventContent};

        for(int i = 0; i < viewContents.length; i++) {
            if(v == buttons[i]) {viewContents[i].setVisibility(View.VISIBLE); continue;}
            viewContents[i].setVisibility(View.GONE);
        }
    }
}
