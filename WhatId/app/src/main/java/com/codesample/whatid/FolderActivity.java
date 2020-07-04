package com.codesample.whatid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codesample.whatid.adapter.FolderAdapter;
import com.codesample.whatid.data.AppDatabase;
import com.codesample.whatid.data.Folder;
import com.codesample.whatid.databinding.ActivityFolderBinding;

import java.util.List;

public class FolderActivity extends AppCompatActivity implements FolderAdapter.OnListItemClickListener{

    private ActivityFolderBinding binding;
    private AppDatabase db;
    private FolderAdapter adapter;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(getApplicationContext());
        adapter = new FolderAdapter(this);

        userId = getIntent().getStringExtra("userId");

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingActionButton.setOnClickListener(v -> {
            final EditText name = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("추가")
                    .setMessage("추가할 폴더 이름을 입력해주세요")
                    .setView(name)
                    .setPositiveButton("완료", (d, w) -> {
                        String folderName = name.getText().toString();

                        Folder folder = new Folder(folderName, userId);
                        if(folder != null) new AddTask().execute(folder);
                    })
                    .setNegativeButton("취소", (d, w) -> {

                    })
                    .create()
                    .show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadTask().execute();
    }

    @Override
    public void onListItemClick(Folder folder) {
           Intent intent = new Intent(this, InnerFolderActivity.class);
           intent.putExtra("folderId", folder.id);
           intent.putExtra("userId", userId);
           startActivity(intent);
    }

    @Override
    public void onListItemLongClick(Folder folder) {
        new AlertDialog
                .Builder(this)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("예", (d, w) -> {
                    new DeleteTask().execute(folder);
                })
                .setNegativeButton("아니오", (d, w) -> {})
                .create()
                .show();
    }

    @Override
    public void onEditClick(Folder folder) {
        final EditText name = new EditText(this);
        name.setText(folder.name);

        new AlertDialog.Builder(this)
                .setTitle("수정")
                .setMessage("수정할 이름을 입력해주세요")
                .setView(name)
                .setPositiveButton("완료", (d, w) -> {
                    String folderName = name.getText().toString();
                    folder.name = folderName;

                    new SaveTask().execute(folder);
                })
                .setNegativeButton("취소", (d, w) -> {

                })
                .create()
                .show();
    }

    class LoadTask extends AsyncTask<Void, Void, List<Folder>> {
        @Override
        protected void onPostExecute(List<Folder> folders) {
            super.onPostExecute(folders);
            adapter.updateData(folders);
        }

        @Override
        protected List<Folder> doInBackground(Void... voids) {
            return db.getFolderDAO().getFoldersByUser(userId);
        }
    }

    class AddTask extends AsyncTask<Folder, Void, List<Folder>> {
        @Override
        protected void onPostExecute(List<Folder> folders) {
            super.onPostExecute(folders);
            adapter.updateData(folders);
        }

        @Override
        protected List<Folder> doInBackground(Folder... folders) {
            db.getFolderDAO().addFolder(folders[0]);
            return db.getFolderDAO().getFoldersByUser(userId);
        }
    }

    class SaveTask extends AsyncTask<Folder, Void, List<Folder>> {
        @Override
        protected void onPostExecute(List<Folder> folders) {
            super.onPostExecute(folders);
            adapter.updateData(folders);
        }

        @Override
        protected List<Folder> doInBackground(Folder... folders) {
            db.getFolderDAO().saveFolder(folders[0]);
            return db.getFolderDAO().getFoldersByUser(userId);
        }
    }

    class DeleteTask extends AsyncTask<Folder, Void, List<Folder>> {
        @Override
        protected void onPostExecute(List<Folder> folders) {
            super.onPostExecute(folders);
            adapter.updateData(folders);
        }

        @Override
        protected List<Folder> doInBackground(Folder... folders) {
            db.getFolderDAO().deleteFolder(folders[0]);
            return db.getFolderDAO().getFoldersByUser(userId);
        }
    }
}
