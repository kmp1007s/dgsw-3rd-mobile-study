package com.codesample.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.codesample.memo.databinding.ActivityEditorBinding;

import java.time.LocalDate;

public class EditorActivity extends AppCompatActivity {
    private ActivityEditorBinding binding;
    private MemoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = MemoDatabase.getInstance(getApplicationContext());

        binding.buttonSave.setOnClickListener(v -> {
            String title = binding.editTextTitle.getText().toString();
            String content = binding.editTextContent.getText().toString();

            if(title.isEmpty() || content.isEmpty()) return;

            String now = LocalDate.now().toString();
            Memo m = new Memo(now, title, content);
            new SaveTask().execute(m);
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
            db.getMemoDao().addMemo(memos[0]);
            return null;
        }
    }
}
