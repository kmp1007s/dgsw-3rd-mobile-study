package com.codesample.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.codesample.memo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MemoDatabase db;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = MemoDatabase.getInstance(getApplicationContext());
        adapter = new MemoAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadTask().execute();
    }

    class LoadTask extends AsyncTask<Void, Void, List<Memo>> {

        @Override
        protected void onPostExecute(List<Memo> memos) {
            super.onPostExecute(memos);
            adapter.updateData(memos);
        }

        @Override
        protected List<Memo> doInBackground(Void... voids) {
            return db.getMemoDao().getMemos();
        }
    }
}
