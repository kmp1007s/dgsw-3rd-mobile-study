package com.codesample.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.codesample.widget.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // inflate는 xml파일을 메모리상에 띄움
        setContentView(binding.getRoot());
        initWidgets();
    }

    private void initWidgets() {
        binding.editTextName.addTextChangedListener(this);
        binding.editTextPhone.addTextChangedListener(this);
        binding.radioButtonAdult.setOnClickListener(this);
        binding.radioButtonStudent.setOnClickListener(this);
        binding.checkBoxTerms.setOnCheckedChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("Main", "before=" + s + " start=" + start + ", count=" + count + ", after=" + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("Main", "on=" + s + " start=" + start + ", before=" + before + ", count=" + count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("Main", "after=" + s.toString());
        updateProgress();
    }

    @Override
    public void onClick(View v) {
        updateProgress();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateProgress();
    }

    private void updateProgress() {
        int progress = 0;

        if(binding.editTextName.getText().length() > 0) progress++;
        if(binding.editTextPhone.getText().length() > 0) progress++;
        if(binding.radioButtonAdult.isChecked() || binding.radioButtonStudent.isChecked()) progress++;
        if(binding.checkBoxTerms.isChecked()) progress++;

        binding.progressBar.setProgress(progress);
        if(progress == 4) binding.buttonApply.setVisibility(View.VISIBLE);
        else binding.buttonApply.setVisibility(View.INVISIBLE);
    }
}
