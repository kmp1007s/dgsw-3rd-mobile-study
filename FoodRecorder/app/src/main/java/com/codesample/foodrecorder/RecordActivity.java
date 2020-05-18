package com.codesample.foodrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.codesample.foodrecorder.data.FoodRecord;
import com.codesample.foodrecorder.data.FoodRecordOpenHelper;
import com.codesample.foodrecorder.data.RecordAdapter;
import com.codesample.foodrecorder.databinding.ActivityRecordBinding;

public class RecordActivity extends AppCompatActivity implements RecordAdapter.OnItemClickListener{

    private ActivityRecordBinding binding;
    private FoodRecordOpenHelper helper;
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new FoodRecordOpenHelper(this, "db", null, 1);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new RecordAdapter(helper.getRecords(), this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onItemClick(View v, int position, FoodRecord record) {
        helper.delete(record.getId());
        adapter.setData(helper.getRecords());
        adapter.notifyDataSetChanged();
    }
}
