package com.codesample.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Delete;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.codesample.memo.databinding.ActivityEditorBinding;

import java.time.LocalDate;

public class EditorActivity extends AppCompatActivity {
    private ActivityEditorBinding binding;
    private MemoDatabase db;
    private int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        if(title!=null) binding.editTextTitle.setText(title);
        if(content!=null) binding.editTextContent.setText(content);

        if(number == 0) binding.buttonDelete.setVisibility(View.INVISIBLE);

        db = MemoDatabase.getInstance(getApplicationContext());

        binding.buttonSave.setOnClickListener(v -> {
            Memo m = getMemo();
            if(m!=null) new SaveTask().execute(m);
        });

        binding.buttonDelete.setOnClickListener(v -> {
            Memo m = getMemo();
            if(m!= null) new DeleteTask().execute(m);
        });
    }

    class SaveTask extends AsyncTask<Memo, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected Void doInBackground(Memo... memos) {
            if(number == 0)
                db.getMemoDao().addMemo(memos[0]);
            else
                db.getMemoDao().saveMemo(memos[0]);
            return null;
        }
    }

    class DeleteTask extends AsyncTask<Memo, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected Void doInBackground(Memo... memos) {
            db.getMemoDao().deleteMemo(memos[0]);
            return null;
        }
    }

    private Memo getMemo() {
        String userTitle = binding.editTextTitle.getText().toString();
        String userContent = binding.editTextContent.getText().toString();

        if(userTitle.isEmpty() || userContent.isEmpty()) return null;

        String now = LocalDate.now().toString();
        Memo m = new Memo(now, userTitle, userContent);
        m.number = number;
        return m;
    }
}
