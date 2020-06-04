package com.codesample.memo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.codesample.memo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MemoAdapter.OnListItemClickListener {

    private ActivityMainBinding binding;
    private MemoDatabase db;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = MemoDatabase.getInstance(getApplicationContext());
        adapter = new MemoAdapter(this);
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

    @Override
    public void onListItemClick(Memo memo) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("number", memo.number);
        intent.putExtra("title", memo.title);
        intent.putExtra("content", memo.content);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClick(Memo memo) {
        new AlertDialog
                .Builder(this)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("예", (d, w) -> {
                    new DeleteTask().execute(memo);
                })
                .setNegativeButton("아니오", (d, w) -> {})
                .create()
                .show();
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

    class DeleteTask extends AsyncTask<Memo, Void, List<Memo>> {
        @Override
        protected void onPostExecute(List<Memo> memos) {
            super.onPostExecute(memos);
            adapter.updateData(memos);
        }

        @Override
        protected List<Memo> doInBackground(Memo... memos) {
            db.getMemoDao().deleteMemo(memos[0]);
            return db.getMemoDao().getMemos();
        }
    }
}
