package com.codesample.foodrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.codesample.foodrecorder.data.FoodRecord;
import com.codesample.foodrecorder.data.FoodRecordDatabase;
import com.codesample.foodrecorder.data.FoodRecordOpenHelper;
import com.codesample.foodrecorder.data.RecordAdapter;
import com.codesample.foodrecorder.databinding.ActivityRecordBinding;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements RecordAdapter.OnItemClickListener{

    private ActivityRecordBinding binding;
//    private FoodRecordOpenHelper helper;
    private RecordAdapter adapter;
    private FoodRecordDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        helper = new FoodRecordOpenHelper(this, "db", null, 1);
        db = FoodRecordDatabase.getInstance(getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new RecordAdapter(null, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(manager);
        new LoadTask().execute();
    }

    @Override
    public void onItemClick(View v, int position, FoodRecord record) {
        new DeleteTask().execute(record);
//        helper.delete(record.getId());
//        adapter.setData(helper.getRecords());
//        adapter.notifyDataSetChanged();
    }
    private class LoadTask extends AsyncTask<Void, Void, List<FoodRecord>> {
        @Override
        protected List<FoodRecord> doInBackground(Void... voids) {
            return db.foodRecordDAO().getRecords();
        }
        @Override
        protected void onPostExecute(List<FoodRecord> foodRecords) {
            super.onPostExecute(foodRecords);
            adapter.setData((ArrayList<FoodRecord>) foodRecords);
            adapter.notifyDataSetChanged();
        }
    }

    private class DeleteTask extends AsyncTask<FoodRecord, Void, List<FoodRecord>>{

        @Override
        protected List<FoodRecord> doInBackground(FoodRecord... args) {
            int result = db.foodRecordDAO().delete(args[0]);
            if(result == 1) {
                return db.foodRecordDAO().getRecords();
            } else
                return null;
        }
        @Override
        protected void onPostExecute(List<FoodRecord> foodRecords) {
            super.onPostExecute(foodRecords);
            if(foodRecords != null) {
                adapter.setData((ArrayList<FoodRecord>) foodRecords);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
